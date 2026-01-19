package io.xone.chain.onenft.common.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private final Set<String> acceptedValues = new HashSet<>();

    @Override
    public void initialize(EnumValue annotation) {
        Class<? extends Enum<?>> enumClass = annotation.enumClass();
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        String methodName = annotation.method();

        try {
            Method method = enumClass.getMethod(methodName);
            for (Enum<?> enumConstant : enumConstants) {
                Object value = method.invoke(enumConstant);
                acceptedValues.add(value.toString());
            }
        } catch (Exception e) {
            // Fallback to name() if method not found or error
            for (Enum<?> enumConstant : enumConstants) {
                acceptedValues.add(enumConstant.name());
            }
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull or @NotBlank for null checks
        }

        if (value instanceof Collection) {
            for (Object item : (Collection<?>) value) {
                if (item != null && !acceptedValues.contains(item.toString())) {
                    return false;
                }
            }
            return true;
        }
        
        if (value instanceof String) {
             if (StringUtils.isEmpty(value)) {
                 return true; // Let @NotBlank handle empty strings if needed
             }
        }

        return acceptedValues.contains(value.toString());
    }
}