package com.lsm.hbase.dml;

import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lsm.hbase.HBaseBaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/8/1 22:33
 **/

@Slf4j
public class DataTest extends HBaseBaseTest {

    private static final byte[] COLUMN_FAMILY_BYTES = Bytes.toBytes("cf");

    private static final byte[] COLUMN_BYTES = Bytes.toBytes("name");

    private Table table;

    @Before
    public void getTable() throws Exception {
        table = connection.getTable(TableName.valueOf("test"));
    }

    @After
    public void closeTable() throws Exception {
        table.close();
    }

    @Test
    public void addRowData() throws Exception {
        byte[] rowKey = Bytes.toBytes("10001");
        String value = "zhangsan";

        Put put = new Put(rowKey);

        Cell cell = CellBuilderFactory.create(CellBuilderType.DEEP_COPY)
                    .setFamily(COLUMN_FAMILY_BYTES)
                    .setType(Cell.Type.Put)
                    .setRow(rowKey)
                    .setQualifier(COLUMN_BYTES)
                    .setValue(Bytes.toBytes(value))
                    .setTimestamp(System.currentTimeMillis())
                    .build();

        put.add(cell);

        table.put(put);
    }

    @Test
    public void deleteRow() throws Exception{
        byte[] rowKey = Bytes.toBytes("10001");
        Delete delete = new Delete(rowKey);

        table.delete(delete);
    }

    @Test
    public void getAllRows() throws Exception {
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);

        for (Result result : results) {
            printResult(result);
        }

    }

    @Test
    public void getRow() throws Exception{
        byte[] rowKey = Bytes.toBytes("10001");
        Get get = new Get(rowKey);

        get.addColumn(COLUMN_FAMILY_BYTES, COLUMN_BYTES);

        Result result = table.get(get);
        printResult(result);
    }

    private void printResult(Result result) {
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            log.info("rowKey:{}", Bytes.toString(CellUtil.cloneRow(cell)));
            log.info("columnFamily:{}", Bytes.toString(CellUtil.cloneFamily(cell)));
            log.info("qualifier:{}", Bytes.toString(CellUtil.cloneQualifier(cell)));
            log.info("value:{}", Bytes.toString(CellUtil.cloneValue(cell)));
            log.info("timestamp:{}", cell.getTimestamp());
        }
    }
}
