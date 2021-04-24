package com.lsm.hbase.ddl;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import com.lsm.hbase.HBaseBaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/8/1 21:37
 **/
@Slf4j
public class TableTest extends HBaseBaseTest {

    @Test
    public void tableExists() throws Exception {
        boolean test = admin.tableExists(TableName.valueOf("test"));

        log.info(String.valueOf(test));
    }

    @Test
    public void createTable() throws Exception {
        TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf("test2"));

        ColumnFamilyDescriptor family = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("data")).build();

        tableDescriptor.setColumnFamily(family);
        admin.createTable(tableDescriptor.build());

    }

    @Test
    public void dropTable() throws Exception {
        TableName test2 = TableName.valueOf("test2");
        admin.disableTable(test2);
        admin.deleteTable(test2);
    }
}
