package lsm.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lishenming
 * @date 2018-12-05 11:18
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelFieldAnnotation {

    // 列索引
    int columnIndex() default 0;
    // 列名
    String columnName() default "";

}
