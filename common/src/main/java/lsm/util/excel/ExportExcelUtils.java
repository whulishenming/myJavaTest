package lsm.util.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import lsm.util.ReflectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.google.common.base.Charsets;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishenming
 * @date 2018-12-05 11:17
 * excel导出工具类
 **/
@Slf4j
public class ExportExcelUtils {

    public static void exportToResponse(List<?> dataList, String sheetName, String fileName,
            HttpServletResponse response) throws Exception {
        SXSSFWorkbook workbook = exportExcel(dataList, sheetName);

        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/binary;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes(), Charsets.ISO_8859_1.name()));
            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("export to response error", e);
        }

    }

    public static void exportToFile(List<?> dataList, String sheetName, String filePath) throws Exception {
        SXSSFWorkbook workbook = exportExcel(dataList, sheetName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath))) {
            workbook.write(fileOutputStream);
            workbook.close();
        } catch (Exception e) {
            log.error("write excel error", e);
        }
    }

    private static SXSSFWorkbook exportExcel(List<?> dataList, String sheetName) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // 居中显示
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        SXSSFSheet sheet = workbook.createSheet(sheetName);

        SXSSFRow titleRow = sheet.createRow(0);
        if (CollectionUtils.isNotEmpty(dataList)) {
            Field[] fields = dataList.get(0).getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelFieldAnnotation.class)) {
                    ExcelFieldAnnotation annotation = field.getAnnotation(ExcelFieldAnnotation.class);
                    createCell(titleRow, annotation.columnIndex(), annotation.columnName(), cellStyle);
                }
            }

            int rowIndex = 1;
            for (Object data : dataList) {
                SXSSFRow dataRow = sheet.createRow(rowIndex++);
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelFieldAnnotation.class)) {
                        ExcelFieldAnnotation annotation = field.getAnnotation(ExcelFieldAnnotation.class);
                        createCell(dataRow, annotation.columnIndex(), ReflectUtils.getFieldValue(field.getName(), data), cellStyle);
                    }
                }
            }
        }

        return workbook;
    }

    private static void createCell(Row row, int index, Object cellValue, CellStyle cellStyle) {
        Cell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean) cellValue);
        } else if (cellValue instanceof Double) {
            cell.setCellValue((Double) cellValue);
        } else if (cellValue instanceof Calendar) {
            cell.setCellValue((Calendar) cellValue);
        } else if (cellValue instanceof Date) {
            cell.setCellValue((Date) cellValue);
        } else {
            cell.setCellValue(cellValue.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        List<ExcelDomainDemo> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1992, Calendar.JULY, i % 28 + 1);

            ExcelDomainDemo excelDomainDemo = new ExcelDomainDemo("张山" + i, 170.1 + i, calendar.getTime(), i % 2 == 0, "test" + i,
                    new Date());
            list.add(excelDomainDemo);
        }

        exportToFile(list, "sheetName", "/Users/lishenming/Downloads/test.xlsx");
    }

}
