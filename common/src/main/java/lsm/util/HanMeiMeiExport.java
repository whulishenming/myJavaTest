package lsm.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsm.util.excel.ExcelFieldAnnotation;

/**
 * @author lishenming
 * @date 2018-12-05 13:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HanMeiMeiExport {
    @ExcelFieldAnnotation(columnIndex = 0, columnName = "车牌")
    private String carNo;

    @ExcelFieldAnnotation(columnIndex = 1, columnName = "她算的金额")
    private double herNum;

    @ExcelFieldAnnotation(columnIndex = 3, columnName = "name")
    private String name;

    @ExcelFieldAnnotation(columnIndex = 2, columnName = "我算的金额")
    private double myNum;

    private String otherField;
}
