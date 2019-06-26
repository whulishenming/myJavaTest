package lsm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-04-03 13:47
 **/

public class BeanUtils {

    /**
     * 属性copy,不存在的属性直接跳过
     *
     * @param source 来源
     * @param target 目的地
     */
    public static void copyProperties(Object source, Object target, List<String> ignoreProperties) throws IllegalAccessException {
        if (source == null) {
            return;
        }

        if (target == null) {
            return;
        }

        Field[] declaredFields = target.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldName = field.getName();

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (EmptyUtils.isNotEmpty(ignoreProperties)) {
                if (ignoreProperties.contains(fieldName)) {
                    continue;
                }
            }

            ReflectUtils.setFieldValue(target, fieldName, ReflectUtils.getFieldValue(field.getName(), source));
        }
    }
}
