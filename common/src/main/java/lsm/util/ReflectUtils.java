package lsm.util;

import org.springframework.util.Assert;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author lishenming
 * @date 2018-12-05 13:39
 * 反射相关工具类
 **/
public class ReflectUtils {

    public static <T> Object getFieldValue(String fieldName, T t)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Assert.notNull(t);
        Assert.notNull(fieldName);

        Class<?> clazz = t.getClass();

        PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);

        Method getMethod = pd.getReadMethod();

        return getMethod.invoke(t);
    }

    public static <T> void setFieldValue(T t, String fieldName, Object value) throws Exception{
        Assert.notNull(t);
        Assert.hasText(fieldName);

        Class<?> clazz = t.getClass();

        PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);

        Method writeMethod = pd.getWriteMethod();

        writeMethod.invoke(t, value);
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
