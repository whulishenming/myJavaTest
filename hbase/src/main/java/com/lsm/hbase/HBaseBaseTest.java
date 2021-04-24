package com.lsm.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.junit.After;
import org.junit.Before;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/8/1 21:34
 **/

public class HBaseBaseTest {
    public Connection connection;

    public Admin admin;

    @Before
    public void init() throws Exception {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "localhost");

        connection = ConnectionFactory.createConnection();

        admin = connection.getAdmin();
    }

    @After
    public void close() throws Exception {
        admin.close();

        connection.close();
    }
}
