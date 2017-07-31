package lsm.util;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by shenming.li on 2017/6/8.
 */
public class ReadExcelUtil {

    public static List<Map<String, String>> readExcel(MultipartFile file) throws Exception{
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            return readWorkbook(workbook);
    }

    public static List<Map<String, String>> readExcel(String filepath) throws Exception{
        InputStream inputStream = new FileInputStream(new File(filepath));
        Workbook workbook = WorkbookFactory.create(inputStream);
        return readWorkbook(workbook);
    }

    public static List<Map<String, String>> readWorkbook(Workbook workbook) {
        List<Map<String, String>> rowList = new ArrayList<>();
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet sheet = workbook.getSheetAt(numSheet);
            if (sheet == null){
                continue;
            }
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                Map<String, String> rowMap = new HashMap<>();
                if (row != null) {
                    for (int cellNum = 0; cellNum <  row.getLastCellNum(); cellNum++){
                        Cell cell = row.getCell(cellNum);
                        rowMap.put(sheet.getSheetName() + "-" + CellReference.convertNumToColString(cellNum) + (rowNum + 1), getValue(cell));
                    }
                }
                rowList.add(rowMap);
            }
        }
        return rowList;
    }

    @SuppressWarnings("static-access")
    public static String getValue(Cell cell) {
        if (cell == null){
            return "";
        }
        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {

            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            // 处理日期
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = null;
                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                    sdf = new SimpleDateFormat("HH:mm");
                } else {// 日期
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                Date date = cell.getDateCellValue();
                return sdf.format(date);
            } else {
                DecimalFormat df = new DecimalFormat("0"); // 避免科学计数法显示。
                return df.format(cell.getNumericCellValue());
            }
            /*return String.valueOf(new BigDecimal(cell.getNumericCellValue())).trim();*/
        } else  if (cell.getCellType() == cell.CELL_TYPE_FORMULA){
            //当单元格为公式时，特殊处理
            try {
                return String.valueOf(cell.getStringCellValue()).trim();
            } catch (IllegalStateException e) {
                cell.setCellType(cell.CELL_TYPE_NUMERIC);
                return String.valueOf(cell.getNumericCellValue()).trim();
            }
        }else{

            return String.valueOf(cell.getStringCellValue());
        }
    }

    @Test
    public void test() throws Exception {
        List<Map<String, String>> mapList = readExcel("C:\\Users\\shenming.li\\Desktop\\import.xls");
        for (Map<String, String> stringStringMap : mapList) {
            for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                System.out.println(stringStringEntry.getKey() + " : " + stringStringEntry.getValue());
            }
        }
    }

}
