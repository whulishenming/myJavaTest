package lsm.util;

import java.lang.reflect.Field;
import java.util.Objects;

import org.springframework.util.Assert;

/**
 * @author lishenming
 * @date 2018-12-05 13:39
 * 反射相关工具类
 **/
public class ReflectUtils {

    public static <T> Object getFieldValue(String fieldName, T t)
            throws IllegalAccessException {
        Assert.notNull(t);
        Assert.notNull(fieldName);

        Field[] declaredFields = t.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            if (Objects.equals(fieldName, field.getName())) {
                field.setAccessible(true);

                return field.get(t);
            }
        }

        return null;
    }

    public static <T> void setFieldValue(T t, String fieldName, Object value)
            throws IllegalAccessException {
        Assert.notNull(t);
        Assert.hasText(fieldName);

        Field[] declaredFields = t.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            if (Objects.equals(fieldName, field.getName())) {
                field.setAccessible(true);
                field.set(t, value);
                return;
            }
        }
    }

    public static Field getField(Class<?> clazz, String name){
        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            if (Objects.equals(name, field.getName())) {
                return field;
            }
        }
        return null;
    }



}
