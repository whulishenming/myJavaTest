package com.lsm.elasticsearch.api;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.lsm.elasticsearch.ElasticSearchBaseTest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/11/7 15:07
 * query 和 filter 的区别是 filter不参与算法
 **/

public class QueryTest extends ElasticSearchBaseTest {

    /**
     * id查询
     */
    @Test
    public void idsQuery() throws Exception {
        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.idsQuery().addIds("k3NPO24B3ADP92oPjtNU"));
        SearchRequest searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());
    }

    /**
     * 单字段条件 包含⽽不是相等, 可以用来查询数组中含有该值
     */
    @Test
    public void termQuery() throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.termQuery("age", 22));
        // id倒序
        searchSourceBuilder.sort("_id", SortOrder.DESC);
        SearchRequest searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());
    }

    /**
     * 处理空值
     */
    @Test
    public void existsQuery() throws Exception {
        ExistsQueryBuilder existsQueryBuilder = QueryBuilders.existsQuery("userName");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(existsQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());
    }

    /**
     * 多词语查询，查找符合词语列表的数据。 如果要查询的字段索引为not_analyzed类型，则terms查询非常类似于关系型数据库中的in查询
     */
    @Test
    public void termsQuery() throws Exception {
        TermsQueryBuilder termsQueryBuilder =
            QueryBuilders.termsQuery("userName", Arrays.asList("test3", "test31", "test13"));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(termsQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());
    }



    /**
     * 前缀匹配
     */
    @Test
    public void prefixQuery() throws Exception {
        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.prefixQuery("userName", "test1"));
        SearchRequest searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());
    }

    /**
     * 范围查询
     */
    @Test
    public void rangeQuery() throws Exception {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("birthDay")
            .gt(LocalDateTime.of(1992, 1, 1, 0, 0, 0)).lt(LocalDateTime.of(1993, 1, 1, 0, 0, 0));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(rangeQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());
    }

    /**
     * 通配符查询 1. * 代表任意（包括0个）多个字符 2. ? 代表任意一个字符
     */
    @Test
    public void wildcardQuery() throws Exception {
        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("userName", "test2*");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(wildcardQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());
    }

    /**
     * 正则表达式查询
     */
    @Test
    public void regexpQuery() throws Exception {
        // 这里的.号表示任意一个字符
        RegexpQueryBuilder regexpQueryBuilder = QueryBuilders.regexpQuery("userName", ".*1.*");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(regexpQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());
    }

    /**
     * 多字段组合条件匹配 Bool（布尔）查询是一种复合型查询，它可以结合多个其他的查询条件。主要有3类逻辑查询 1. must：返回的文档必须满足must子句的条件，并且参与计算分值 2.
     * filter：返回的文档必须满足filter子句的条件。但是不会像Must一样，参与计算分值，因此效率会比较高 3.
     * should：返回的文档可能满足should子句的条件。在一个Bool查询中，如果没有must或者filter，有一个或者多个should子句，那么只要满足一个就可以 4.
     * must_not：返回的文档必须不满足must_not定义的条件。
     */
    @Test
    public void boolQuery() throws Exception {
        BoolQueryBuilder must = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("age", 22))
            .must(QueryBuilders.termQuery("userName", "test2"));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(must);
        SearchRequest searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());

        System.out.println("------------------------------------------------");

        BoolQueryBuilder should = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("age", 22))
            .should(QueryBuilders.termQuery("age", 20));
        searchSourceBuilder = new SearchSourceBuilder().query(should);
        searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);
        searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());

        System.out.println("------------------------------------------------");

        BoolQueryBuilder mustNot = QueryBuilders.boolQuery().mustNot(QueryBuilders.rangeQuery("age").gt(20));
        searchSourceBuilder = new SearchSourceBuilder().query(mustNot);
        searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());

        System.out.println("------------------------------------------------");

        BoolQueryBuilder filter = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("age").lt(24))
            .filter(QueryBuilders.rangeQuery("age").gt(22));
        searchSourceBuilder = new SearchSourceBuilder().query(filter);
        searchRequest = new SearchRequest("usersearch").source(searchSourceBuilder);

        searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printSearchHits(searchResponse.getHits());
    }

    private void printSearchHits(SearchHits searchHits) {
        for (SearchHit next : searchHits) {
            Map<String, Object> sourceAsMap = next.getSourceAsMap();
            System.out.println(JSONObject.toJSONString(sourceAsMap));
        }
    }

}
