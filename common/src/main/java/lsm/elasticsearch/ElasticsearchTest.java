package lsm.elasticsearch;


import com.alibaba.fastjson.JSONObject;
import lsm.domain.User;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by lishenming on 2017/7/28.
 * elasticsearch java 5.5 api
 */
public class ElasticsearchTest {

    private TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.31.80"), 9300));
    }



    @Test
    public void testIndexByPrepareIndex() throws IOException {

        /**
         * 1. XContentBuilder
         * 2. String, XContentType.JSON
         */
        IndexResponse response = client.prepareIndex("testindex", "user")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "lishenming")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject())
                .get();

       /* User user = new User("lishenming", 25, Arrays.asList("movies", "pingpong"), new Date());

        IndexResponse response = client.prepareIndex("testindex", "user")
                .setSource(JSONObject.toJSONString(user), XContentType.JSON)
                .get();*/

        // [index=testindex,type=user,id=AV2YeIrcP7bcz9wB6lJa,version=1,result=created,shards={"total":2,"successful":1,"failed":0}]
        System.out.println(response);
    }

    @Test
    public void testIndexByIndexRequest() throws IOException, ExecutionException, InterruptedException {

        User user = new User("lishenming", 25, Arrays.asList("movies", "pingpong"), new Date());

        IndexRequest indexRequest = new IndexRequest("index", "type", "1").source(JSONObject.toJSONString(user), XContentType.JSON);

        IndexResponse response = client.index(indexRequest).get();

        System.out.println(response);
    }

    @Test
    public void testGetByPrepareGet() {

        GetResponse response = client.prepareGet("testindex", "user", "AV2YgNy9P7bcz9wB6lJf").get();

       /* GetResponse response = client.prepareGet()
                .setIndex("testindex")
                .setType("user")
                .setId("AV2YgNy9P7bcz9wB6lJf")
                .get();*/
        // {"_index":"testindex","_type":"user","_id":"AV2YgNy9P7bcz9wB6lJf","_version":1,"found":true,"_source":{"age":25,"createTime":1501502166113,"likes":["movies","pingpong"],"name":"lishenming"}}
        System.out.println(response);

    }

    @Test
    public void testGetByGetRequest() throws ExecutionException, InterruptedException {

        GetRequest getRequest = new GetRequest("testindex", "user", "1");

        GetResponse response = client.get(getRequest).get();

        System.out.println(response);
    }

    @Test
    public void testDeleteByPrepareDelete() {

        DeleteResponse response = client.prepareDelete("testindex", "user", "1").get();

        // DeleteResponse[index=testindex,type=user,id=AV2YeklbP7bcz9wB6lJb,version=2,result=deleted,shards=ShardInfo{total=2, successful=1, failures=[]}]
        System.out.println(response);

    }

    @Test
    public void testDeleteByDeleteRequest() throws ExecutionException, InterruptedException {

        DeleteRequest deleteRequest = new DeleteRequest("index", "type", "1");

        DeleteResponse response = client.delete(deleteRequest).get();

        System.out.println(response);

    }

    @Test
    public void testDeleteByQueryApi() {

        BulkByScrollResponse response =
                DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                        .filter(QueryBuilders.matchQuery("name", "lishenming"))  // query
                        .source("testindex")  // index
                        .get(); // execute the operation

        // BulkIndexByScrollResponse[took=529.8ms,timed_out=false,sliceId=null,updated=0,created=0,deleted=1,batches=1,versionConflicts=0,noops=0,retries=0,throttledUntil=0s,bulk_failures=[],search_failures=[]]
        System.out.println(response);

    }

    @Test
    public void testUpdateByUpdateRequest() throws IOException, ExecutionException, InterruptedException {
        delete();
        index();
        /**
         * 1. XContentBuilder
         * 2. String, XContentType.JSON
         * 3. Map<String, String> docMap
         */

        UpdateRequest updateRequest = new UpdateRequest("testindex", "user", "1").doc(jsonBuilder()
                .startObject()
                .field("gender", "male")
                .endObject());
       /* updateRequest.index("testindex");
        updateRequest.type("user");
        updateRequest.id("1");*/

      /*  User user = new User("lishenming", 25, Arrays.asList("movies", "pingpong"), new Date());
        updateRequest.doc(JSONObject.toJSONString(user), XContentType.JSON);

        Map<String, String> docMap = new HashMap<>();
        updateRequest.doc(docMap);*/

        UpdateResponse response = client.update(updateRequest).get();

        // UpdateResponse[index=testindex,type=user,id=1,version=2,result=updated,shards=ShardInfo{total=2, successful=1, failures=[]}]
        System.out.println(response);

    }

    @Test
    public void updateByPrepareUpdate() throws IOException {
        delete();
        index();
        User user = new User("lishenming", 25, Arrays.asList("movies", "pingpong"), new Date());

        UpdateResponse response = client.prepareUpdate("testindex", "user", "1").setDoc(JSONObject.toJSONString(user), XContentType.JSON).get();
        // UpdateResponse[index=testindex,type=user,id=1,version=5,result=updated,shards=ShardInfo{total=2, successful=1, failures=[]}]
        System.out.println(response);
    }

    @Test
    /**
     * If the document does not exist, the one in indexRequest will be added
     */
    public void updateWithUpsert() throws IOException, ExecutionException, InterruptedException {
        delete();

        User user = new User("lishenming", 25, Arrays.asList("movies", "pingpong"), new Date());

        IndexRequest indexRequest = new IndexRequest("testindex", "user", "1").source(JSONObject.toJSONString(user), XContentType.JSON);

        UpdateRequest updateRequest = new UpdateRequest("testindex", "user", "1").doc(jsonBuilder()
                .startObject()
                .field("gender", "male")
                .endObject()).upsert(indexRequest);

        UpdateResponse response = client.update(updateRequest).get();

        System.out.println(response);
    }

    private void index() throws IOException {
         client.prepareIndex("testindex", "user", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "lishenming")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject())
                .get();
    }

    private void delete() {
        client.prepareDelete("testindex", "user", "1").get();
    }


}
