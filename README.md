# OneChain NFT Backend

OneChain NFT 平台后端服务，基于 Spring Boot 2.3 构建。

## 环境要求

| 工具 | 版本 |
|------|------|
| JDK  | 1.8+ |
| Maven | 3.6+ |
| MySQL | 5.7+ |
| Redis | 6.0+ |

## 快速开始

### 1. 数据库初始化

创建数据库并导入表结构：

```sql
CREATE DATABASE nft_new DEFAULT CHARACTER SET utf8mb4;
```

```bash
mysql -u root -p nft_new < DDL.sql
```

### 2. 修改配置文件

编辑 `src/main/resources/bootstrap.yml`，根据实际环境修改以下配置：

```yaml
server:
  port: 8002          # 服务端口，可按需修改

spring:
  datasource:
    # 生产环境建议将 useSSL=false 改为 useSSL=true 以启用加密连接
    url: jdbc:mysql://127.0.0.1:3306/nft_new?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=false
    username: root        # 数据库用户名
    password: yourStrongPassword  # 数据库密码，生产环境请使用强密码

  redis:
    host: 127.0.0.1   # Redis 地址
    port: 6379        # Redis 端口

onechain:
  rpc-url: https://rpc-testnet.onelabs.cc:443
  keystore-path: /path/to/your/one.keystore  # 修改为实际的 keystore 路径
```

### 3. 构建项目

```bash
mvn clean package -DskipTests
```

### 4. 运行项目

**方式一：使用 Maven 直接运行（开发环境推荐）**

```bash
mvn spring-boot:run
```

**方式二：运行打包后的 JAR（生产环境推荐）**

```bash
# pom.xml 中 finalName 配置为 onechain-onenft，打包后文件名为 onechain-onenft.jar
java -jar target/onechain-onenft.jar
```

### 5. 验证启动

服务启动成功后，控制台会输出：

```
onechain-onenft Application started successfully!
```

访问 Swagger API 文档：

```
http://localhost:8002/swagger-ui/index.html
```

## 项目结构

```
src/main/java/io/xone/chain/onenft/
├── Application.java        # 启动入口
├── config/                 # 配置类
├── controller/             # 控制器层
├── service/                # 服务层
├── mapper/                 # 数据访问层
├── entity/                 # 实体类
├── request/                # 请求对象
├── resp/                   # 响应对象
├── enums/                  # 枚举类
├── common/                 # 公共组件
├── event/                  # 事件监听
├── listener/               # 监听器
└── move/                   # Move 合约交互
```

## 常见问题

**Q: 启动时报数据库连接错误？**  
A: 确认 MySQL 已启动，且 `bootstrap.yml` 中的数据库地址、用户名、密码配置正确，数据库 `nft_new` 已创建并导入了 `DDL.sql`。

**Q: 启动时报 Redis 连接错误？**  
A: 确认 Redis 服务已启动，默认连接 `127.0.0.1:6379`，如有修改请在 `bootstrap.yml` 中更新对应配置。

**Q: 端口被占用？**  
A: 修改 `bootstrap.yml` 中的 `server.port` 为其他端口，或终止占用该端口的进程。
