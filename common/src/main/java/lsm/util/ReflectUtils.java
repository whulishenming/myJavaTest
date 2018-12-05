package lsm.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * @author lishenming
 * @date 2018-12-05 13:39
 * 反射相关工具类
 **/
public class ReflectUtils {

    public static <T> Object getFieldValueByName(String fieldName, T t) throws Exception{
        Class<?> clazz = t.getClass();

        PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);

        Method getMethod = pd.getReadMethod();

        return getMethod.invoke(t);
    }
}
