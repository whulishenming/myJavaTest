package lsm.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishenming
 * @date 2018-12-05 17:20
 **/
@Slf4j
public class ReadExcelUtils {

    public static <T> List<T> readExcel(String filepath, Class<T> clazz) throws Exception{
        InputStream inputStream = new FileInputStream(new File(filepath));
        Workbook workbook = WorkbookFactory.create(inputStream);
        return readWorkbook(workbook, clazz);
    }

    public static <T> List<T> readExcel(MultipartFile file, Class<T> clazz) throws Exception{
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        return readWorkbook(workbook, clazz);
    }

    private static <T> List<T> readWorkbook(Workbook workbook, Class<T> clazz) throws Exception{
        List<T> list = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();

        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet sheet = workbook.getSheetAt(numSheet);
            if (sheet == null){
                continue;
            }
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                T t = clazz.newInstance();

                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(ExcelFieldAnnotation.class)) {
                        ExcelFieldAnnotation annotation = field.getAnnotation(ExcelFieldAnnotation.class);
                        Cell cell = row.getCell(annotation.columnIndex());
                        setFieldValue(t, field, cell);
                    }
                }
                list.add(t);
            }
        }

        return list;
    }

    private static <T> void setFieldValue(T t, Field field, Cell cell) throws Exception{
        if (field.getType() == int.class || field.getType() == Integer.class) {
            field.setInt(t, (int) cell.getNumericCellValue());
        }else if (field.getType() == Double.class || field.getType() == double.class) {
            field.setDouble(t, cell.getNumericCellValue());
        }else if (field.getType() == Date.class){
            field.set(t, cell.getDateCellValue());
        }else if (field.getType() == Boolean.class){
            field.set(t, cell.getBooleanCellValue());
        }else {
            field.set(t, cell.getStringCellValue());
        }
    }

    public static void main(String[] args) throws Exception{
        List<ExcelDomainDemo> list = readExcel("/Users/lishenming/Downloads/test.xls", ExcelDomainDemo.class);
        System.out.println(list);
    }

}
