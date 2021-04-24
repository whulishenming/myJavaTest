package lsm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;
import lsm.zookeeper.HanMeiMeiDemo;
import org.apache.poi.ss.usermodel.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishenming
 * @date 2018-12-05 17:20
 **/
@Slf4j
public class HanMeiMei {

    public static void main(String[] args) throws Exception {
        Workbook workbook = WorkbookFactory.create(new File("/Users/lishenming/Downloads/hanmeimei202012.xls"));
        Map<String, HanMeiMeiDemo> map1 = new HashMap<>();
        Map<String, HanMeiMeiDemo> map2 = new HashMap<>();

        Sheet sheet = workbook.getSheetAt(1);

        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }

            Cell cellA = row.getCell(0);
            Cell cellB = row.getCell(1);
            Cell cellC = row.getCell(2);
            if (cellA != null) {
                String cellAValue = cellA.getStringCellValue().trim();
                HanMeiMeiDemo hanMeiMeiDemo = map1.get(cellAValue);
                double value = 0.0d;
                if (hanMeiMeiDemo != null) {
                    value = hanMeiMeiDemo.getValue();
                }
                if (cellB == null) {
                    map1.putIfAbsent(cellAValue, new HanMeiMeiDemo(value, ""));
                } else {
                    if (cellB.getCellType() == CellType.STRING) {
                        map1.putIfAbsent(cellAValue, new HanMeiMeiDemo(value +
                            Double.parseDouble(cellB.getStringCellValue()), cellC.getStringCellValue().trim()));
                    } else {
                        map1.putIfAbsent(cellAValue,
                            new HanMeiMeiDemo(cellB.getNumericCellValue() + value, cellC.getStringCellValue().trim()));

                    }

                }

            }

            Cell cellF = row.getCell(5);
            Cell cellG = row.getCell(6);
            Cell cellH = row.getCell(7);

            try {
                if (cellG != null) {
                    if (cellG.getCellType() == CellType.NUMERIC) {
                        map2.putIfAbsent(cellF.getStringCellValue().trim(),
                            new HanMeiMeiDemo(cellG.getNumericCellValue(), cellH.getStringCellValue().trim()));
                    } else {
                        if (cellG.getStringCellValue() != null && !"".equals(cellG.getStringCellValue())) {
                            map2.putIfAbsent(cellF.getStringCellValue().trim(),
                                new HanMeiMeiDemo(Double.parseDouble(cellG.getStringCellValue()), cellH.getStringCellValue().trim()));
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        int count = 0;

        map1.forEach((k, v) -> {
            if (v == null) {
                System.out.println(k + "-----" + "null" + "-----" + map2.remove(k));
            } else if (map2.get(k) == null) {
                System.out.println(k + "-----" + v.getValue() + "-----" + v.getName() + "-----" + "我的统计没有这个车牌");
            } else if (v.getValue() - map2.get(k).getValue() > 5.0 || v.getValue() - map2.get(k).getValue() < -5.0) {
                System.out.println(k + "-----" + v.getValue() + "-----" + v.getName() + "-----" + map2.remove(k).getValue());
            }else{
                map2.remove(k);
            }
        });

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------");

        map2.forEach((k, v) -> System.out.println(k + "-----" + v.getValue()+ "-----" + v.getName()));

    }

}
