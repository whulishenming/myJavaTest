package com.lsm.hbase.ddl;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import com.lsm.hbase.HBaseBaseTest;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/8/1 22:32
 **/

public class ColumnFamilyTest extends HBaseBaseTest {

    @Test
    public void addColumnFamily() throws Exception {
        ColumnFamilyDescriptor family = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("info")).build();

        admin.addColumnFamily(TableName.valueOf("test2"), family);
    }

    @Test
    public void deleteColumnFamily() throws Exception {
        admin.deleteColumnFamily(TableName.valueOf("test2"), Bytes.toBytes("info"));
    }
}
