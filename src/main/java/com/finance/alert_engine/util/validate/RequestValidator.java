package com.finance.alert_engine.util.validate;

import com.finance.alert_engine.custom.exception.model.UserException;

import java.lang.reflect.Field;
import java.util.List;

public class RequestValidator {

    public static void validate(Object object, List<String> fields) {
        if (object == null) {
            throw new UserException("U001", "Object cannot be null");
        }

        Class<?> clazz = object.getClass();

        for (String fieldName : fields) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(object);

                if (value == null) {
                    throw new UserException("U002" , fieldName + " is required and cannot be null");
                }

                if (value instanceof String && ((String) value).trim().isEmpty()) {
                    throw new UserException("U003", fieldName + " is required and cannot be empty");
                }

            } catch (NoSuchFieldException e) {
                throw new UserException("U004", fieldName + "' does not exist in " + clazz.getSimpleName());
            } catch (IllegalAccessException e) {
                throw new UserException(fieldName , "Unknown");
            }
        }
    }
}
