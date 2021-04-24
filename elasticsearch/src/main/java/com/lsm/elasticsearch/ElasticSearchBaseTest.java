package com.lsm.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/11/7 15:34
 **/

public class ElasticSearchBaseTest {

    public RestHighLevelClient client;

    @Before
    public void init() {
        client = RestClients.create();
    }

    @After
    public void close() throws Exception {
        client.close();
    }
}
