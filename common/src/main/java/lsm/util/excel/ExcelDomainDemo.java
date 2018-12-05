package lsm.util.excel;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lishenming
 * @date 2018-12-05 13:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelDomainDemo {
    @ExcelFieldAnnotation(columnIndex = 0, columnName = "姓名")
    private String name;

    @ExcelFieldAnnotation(columnIndex = 1, columnName = "身高")
    private double height;

    @ExcelFieldAnnotation(columnIndex = 3, columnName = "生日")
    private Date birthday;

    @ExcelFieldAnnotation(columnIndex = 2, columnName = "是否结婚")
    private Boolean isMarried;

    private String otherField;

    @ExcelFieldAnnotation(columnIndex = 4, columnName = "创建时间")
    private Date createTime;
}
