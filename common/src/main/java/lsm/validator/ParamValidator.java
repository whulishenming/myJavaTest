package lsm.validator;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author lishenming
 * @create 2017-11-08
 **/

public class ParamValidator {

    public <T> boolean notNullValidator1(T t){

        Class<?> clazz = t.getClass();

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                if (field.get(t) == null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public <T> boolean notNullValidator2(T t){

        Class<?> clazz = t.getClass();

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {

            String fieldName = field.getName();
            try {

                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

//                Class<?> clazz = t.getClass();

                Method getMethod = clazz.getMethod(getMethodName);
                /*PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);

                Method getMethod = pd.getReadMethod();*/

                if (getMethod.invoke(t) == null) {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;
    }
}
