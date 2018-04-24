package lsm.util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by shenmingli on 2018/4/24.
 */
public class WriteExcelUtils {

    public static void writeExcel(Map<String, String> cellsMap, String filePath, String sheetName) throws Exception {

        FileOutputStream fos = new FileOutputStream(new File(filePath));

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();

        //新建工作表
        XSSFSheet sheet = workbook.createSheet(sheetName);
        int i = 0;

        for (Map.Entry<String, String> entry : cellsMap.entrySet()){
            //创建行
            XSSFRow row = sheet.createRow(i++);

            XSSFCell cellKey = row.createCell(0);
            //给单元格赋值
            cellKey.setCellValue(entry.getKey());

            XSSFCell cellValue = row.createCell(1);
            //给单元格赋值
            cellValue.setCellValue(entry.getValue());
        }
        //创建输出流
        workbook.write(fos);
        workbook.close();
        fos.close();
    }
}
