import os
import re

ENTITY_DIR = "/Users/it/Documents/gdan/sts_workspace/onechain-nft-backend/src/main/java/io/xone/chain/onenft/entity"
OUTPUT_FILE = "/Users/it/Documents/gdan/sts_workspace/onechain-nft-backend/DDL.sql"

def camel_to_snake(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()

def java_type_to_mysql(java_type):
    if java_type in ['Long', 'long']:
        return 'BIGINT'
    if java_type in ['Integer', 'int']:
        return 'INT'
    if java_type in ['String']:
        return 'VARCHAR(255)'
    if java_type in ['BigDecimal']:
        return 'DECIMAL(20, 9)'
    if java_type in ['LocalDateTime', 'Date']:
        return 'DATETIME'
    if java_type in ['Boolean', 'boolean']:
        return 'TINYINT(1)'
    if java_type in ['Double', 'double']:
        return 'DOUBLE'
    if java_type in ['Float', 'float']:
        return 'FLOAT'
    return 'VARCHAR(255)' # Default fallback

def parse_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Extract class name
    class_name_match = re.search(r'public class (\w+)', content)
    if not class_name_match:
        return None
    class_name = class_name_match.group(1)

    # Extract table name
    table_name_match = re.search(r'@TableName\("([^"]+)"\)', content)
    table_name = table_name_match.group(1) if table_name_match else camel_to_snake(class_name)

    # Extract table comment
    table_comment = ""
    api_model_match = re.search(r'@ApiModel\(.*description\s*=\s*"([^"]+)".*\)', content)
    if api_model_match:
       table_comment = api_model_match.group(1)
    
    # Extract fields
    fields = []
    # Simplified regex for fields - assumes standard formatting as seen in examples
    # Matches: private Type name;
    # And captures preceeding annotations
    
    # Split by lines to process line by line to keep context
    lines = content.split('\n')
    
    current_field = {}
    
    for i, line in enumerate(lines):
        line = line.strip()
        if line.startswith('private '):
            # Parse field definition
            # private Long id;
            # private static final long serialVersionUID = 1L; -> skip static
            if ' static ' in line:
                current_field = {}
                continue
            
            parts = line.replace(';', '').split()
            if len(parts) >= 3:
                field_type = parts[1]
                field_name = parts[2]
                
                current_field['name'] = field_name
                current_field['type'] = field_type
                
                # Check for list/generics like List<Something> and skip them as they are not columns usually
                if '<' in field_type:
                    current_field = {}
                    continue

                # Process collected annotations for this field
                # If column name not set via annotation, calc it
                if 'column_name' not in current_field:
                    current_field['column_name'] = field_name # Default, checking camelCase below
                
                fields.append(current_field)
                current_field = {}
        
        elif line.startswith('@TableId'):
            # @TableId(value = "id", type = IdType.AUTO)
            match = re.search(r'value\s*=\s*"([^"]+)"', line)
            column_name = match.group(1) if match else None
            
            # Also match @TableId("value")
            if not column_name:
                 match_simple = re.search(r'@TableId\("([^"]+)"\)', line)
                 if match_simple:
                     column_name = match_simple.group(1)
            
            if column_name:
                current_field['column_name'] = column_name

            current_field['is_pk'] = True
            if 'IdType.AUTO' in line:
                current_field['is_auto'] = True

        elif line.startswith('@TableField'):
             # @TableField("wallet_address")
             match = re.search(r'@TableField\("([^"]+)"\)', line)
             if match:
                 current_field['column_name'] = match.group(1)
             
             # @TableField(exist = false)
             if 'exist = false' in line or 'exist=false' in line:
                 current_field = {} # Clear current field attributes as we ignore this field
                 continue

        elif line.startswith('@ApiModelProperty'):
            # @ApiModelProperty("用户名")
            match = re.search(r'@ApiModelProperty\("([^"]+)"\)', line)
            if match:
                current_field['comment'] = match.group(1)
        
        # Consider handling @TableLogic if needed

    return {
        'table_name': table_name,
        'comment': table_comment,
        'fields': fields
    }

def generate_create_sql(table_info):
    sql = f"DROP TABLE IF EXISTS `{table_info['table_name']}`;\n"
    sql += f"CREATE TABLE `{table_info['table_name']}` (\n"
    
    pk_columns = []
    lines = []
    
    for field in table_info['fields']:
        col_name = field.get('column_name', camel_to_snake(field['name']))
        col_type = java_type_to_mysql(field['type'])
        
        # Special handling for text content or long strings
        if field['name'].lower() in ['description', 'content', 'data', 'params', 'message', 'param', 'value', 'extra']:
             col_type = 'TEXT'

        line = f"  `{col_name}` {col_type}"
        
        if field.get('is_pk'):
            pk_columns.append(col_name)
            line += " NOT NULL"
            if field.get('is_auto'):
                line += " AUTO_INCREMENT"
        else:
            # Most fields are nullable in java entities unless validated otherwise
            line += " NULL"

        if field.get('comment'):
            line += f" COMMENT '{field['comment']}'"
            
        lines.append(line)
        
    if pk_columns:
        pk_str = ", ".join([f"`{pk}`" for pk in pk_columns])
        lines.append(f"  PRIMARY KEY ({pk_str})")
        
    sql += ",\n".join(lines)
    sql += "\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
    if table_info['comment']:
        sql += f" COMMENT='{table_info['comment']}'"
    sql += ";\n\n"
    
    return sql

def main():
    if not os.path.exists(ENTITY_DIR):
        print(f"Directory not found: {ENTITY_DIR}")
        return

    all_sql = ""
    files = [f for f in os.listdir(ENTITY_DIR) if f.endswith('.java')]
    files.sort()
    
    for filename in files:
        filepath = os.path.join(ENTITY_DIR, filename)
        print(f"Processing {filename}...")
        try:
            table_info = parse_file(filepath)
            if table_info and table_info['fields']:
                all_sql += generate_create_sql(table_info)
        except Exception as e:
            print(f"Error processing {filename}: {e}")

    with open(OUTPUT_FILE, 'w', encoding='utf-8') as f:
        f.write(all_sql)
    print(f"DDL generated in {OUTPUT_FILE}")

if __name__ == "__main__":
    main()
