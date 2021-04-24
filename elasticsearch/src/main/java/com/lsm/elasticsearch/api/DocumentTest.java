package com.lsm.elasticsearch.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import com.lsm.elasticsearch.ElasticSearchBaseTest;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/11/7 15:36
 **/

public class DocumentTest extends ElasticSearchBaseTest {

    @Test
    public void index() throws Exception {
        Map<String, Object> paramMap = new HashMap<>(10);
        paramMap.put("userName", "lsm");
        paramMap.put("age", 28);
        paramMap.put("birthDay", LocalDateTime.of(1992, 1, 16, 10, 8));

        IndexRequest indexRequest =
            new IndexRequest("usersearch".toLowerCase()).id("9nTORG4B3ADP92oPzX5M").source(paramMap)
                // .opType(DocWriteRequest.OpType.CREATE)
                .timeout(new TimeValue(1, TimeUnit.SECONDS));

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        paramMap.put("userName", "lsm2");
        IndexRequest indexRequest2 = new IndexRequest("usersearch".toLowerCase()).id("222222").source(paramMap)
            .opType(DocWriteRequest.OpType.CREATE).timeout(new TimeValue(1, TimeUnit.SECONDS));

        IndexResponse indexResponse2 = client.index(indexRequest2, RequestOptions.DEFAULT);

        boolean success = indexResponse.getResult() == DocWriteResponse.Result.CREATED;

        System.out.println(success);
    }

    /**
     * fetchSourceContext 默认false，可以过滤field
     */
    @Test
    public void get() throws Exception {
        GetRequest getRequest = new GetRequest().index("usersearch").id("9nTORG4B3ADP92oPzX5M")
            .fetchSourceContext(new FetchSourceContext(true, new String[] {"birthDay"}, Strings.EMPTY_ARRAY));

        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        if (getResponse.isExists()) {
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();

            System.out.println(JSONObject.toJSONString(sourceAsMap));
        }
    }

    @Test
    public void exists() throws Exception {
        GetRequest getRequest = new GetRequest().index("usersearch").id("9nTORG4B3ADP92oPzX5M")
            .fetchSourceContext(new FetchSourceContext(false));

        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);

        System.out.println(exists);
    }

    @Test
    public void delete() throws Exception {
        DeleteRequest deleteRequest = new DeleteRequest().index("usersearch").id("lnNPO24B3ADP92oPjtPN")
            .timeout(TimeValue.timeValueMillis(100)).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
            System.out.println("not found");

        } else if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
            System.out.println("success");
        }
    }

    /**
     * document 必须存才
     */
    @Test
    public void update() throws Exception {
        Map<String, Object> map = new HashMap<>(1);
        map.put("city", "nanjing");
        map.put("city2", "nanjing2");

        UpdateRequest updateRequest = new UpdateRequest().index("usersearch").id("9nTORG4B3ADP92oPzX5M").doc(map)
            .setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL).retryOnConflict(3);

        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

        System.out.println(updateResponse);
    }

    @Test
    public void upsert() throws Exception {
        Map<String, Object> map = new HashMap<>(1);
        map.put("city", "shanghai");
        map.put("city2", "shanghai2");
        map.put("city3", "shanghai2");

        UpdateRequest updateRequest = new UpdateRequest().index("usersearch").id("9nTORG4B3ADP92oPzX5M").doc(map)
            .docAsUpsert(true).setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL).retryOnConflict(3);

        System.out.println(updateRequest.toString());

        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("相当于index");
        } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            System.out.println("相当于update");
        } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
            System.out.println("没有任何更新，该field已经存在且值相同");
        }
    }

    /**
     * 某一条语句报错不会影响整体执行
     */
    @Test
    public void bulk() throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new DeleteRequest("usersearch", "oHNPO24B3ADP92oPkNOF"))
            .add(new UpdateRequest("usersearch", "pHNPO24B3ADP92oPkdM").doc(XContentType.JSON, "other", "test"))
            .add(new IndexRequest("usersearch").id("4").source(XContentType.JSON, "field", "baz"));

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
        }

        // getFailure
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            if (bulkItemResponse.isFailed()) {
                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
            }
        }

        for (BulkItemResponse item : bulkResponse.getItems()) {
            DocWriteResponse itemResponse = item.getResponse();

            switch (item.getOpType()) {
                case CREATE:
                    IndexResponse indexResponse = (IndexResponse)itemResponse;
                    System.out.println(indexResponse);
                    break;
                case UPDATE:
                    UpdateResponse updateResponse = (UpdateResponse)itemResponse;
                    System.out.println(updateResponse);
                    break;
                case DELETE:
                    DeleteResponse deleteResponse = (DeleteResponse)itemResponse;
                    System.out.println(deleteResponse);
                    break;
                default:
                    System.out.println("error");
            }
        }
    }

    @Test
    public void bulkWithBulkProcessor() {
        BulkProcessor bulkProcessor = bulkProcessor();
        bulkProcessor.add(new IndexRequest("usersearch").id("12197").source(XContentType.JSON, "field", "baz1"))
            .add(new IndexRequest("usersearch").id("121918").source(XContentType.JSON, "field", "baz2"))
            .add(new IndexRequest("usersearch").id("121919").source(XContentType.JSON, "field", "baz3"));

        bulkProcessor.flush();

        // close method flush the requests added to the processor before closing the processor and also forbid any new
        // request to be added to it
        bulkProcessor.close();
    }

    private BulkProcessor bulkProcessor() {

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                System.out.println(request.numberOfActions());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                if (response.hasFailures()) {
                    System.out.println(response.buildFailureMessage());
                } else {
                    System.out.println(response.getTook().getMillis());
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                System.out.println(ExceptionUtils.getFullStackTrace(failure));
            }
        };

        BulkProcessor.Builder builder = BulkProcessor.builder(
            (request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener), listener);
        // Set when to flush a new bulk request based on the number of actions currently added (defaults to 1000, use -1
        // to disable it)
        builder.setBulkActions(2);
        // Set when to flush a new bulk request based on the size of actions currently added (defaults to 5Mb, use -1 to
        // disable it)
        builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
        // Set the number of concurrent requests allowed to be executed (default to 1, use 0 to only allow the execution
        // of a single request)
        builder.setConcurrentRequests(0);
        // Set a flush interval flushing any BulkRequest pending if the interval passes (defaults to not set)
        builder.setFlushInterval(TimeValue.timeValueMinutes(1));
        // TimeValue.timeValueSeconds(10L)
        builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));

        return builder.build();
    }

    @Test
    public void multiGet() throws Exception {
        MultiGetRequest multiGetRequest = new MultiGetRequest()
            .add(new MultiGetRequest.Item("usersearch", "tnNPO24B3ADP92oPk9Pg")
                .fetchSourceContext(new FetchSourceContext(true, Strings.EMPTY_ARRAY, Strings.EMPTY_ARRAY)))
            .add(new MultiGetRequest.Item("usersearch", "v3NPO24B3ADP92oPldNC"));

        MultiGetResponse multiGetItemResponses = client.mget(multiGetRequest, RequestOptions.DEFAULT);

        for (MultiGetItemResponse itemResponse : multiGetItemResponses.getResponses()) {
            GetResponse getResponse = itemResponse.getResponse();
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();

            System.out.println(JSONObject.toJSONString(sourceAsMap));
        }
    }

    @Test
    public void deleteByQuery() throws Exception {
        DeleteByQueryRequest deleteByQueryRequest =
            new DeleteByQueryRequest("usersearch").setQuery(new TermQueryBuilder("userName", "test48"));

        BulkByScrollResponse bulkResponse = client.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);

        long totalDocs = bulkResponse.getTotal();
        long deletedDocs = bulkResponse.getDeleted();

        System.out.println("");

    }
}
