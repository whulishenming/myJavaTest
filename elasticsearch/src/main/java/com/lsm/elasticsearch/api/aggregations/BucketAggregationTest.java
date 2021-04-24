package com.lsm.elasticsearch.api.aggregations;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.*;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.missing.MissingAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import com.lsm.elasticsearch.ElasticSearchBaseTest;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/11/8 10:49 分组
 **/
public class BucketAggregationTest extends ElasticSearchBaseTest {

    /**
     * 过滤聚合 基于一个条件，来对当前的文档进行过滤的聚合
     */
    @Test
    public void filterAggregation() throws Exception {
        FilterAggregationBuilder filterAggregation =
            AggregationBuilders.filter("agg", QueryBuilders.termQuery("gender", "male"));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(filterAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Filter agg = searchResponse.getAggregations().get("agg");

        System.out.println(agg.getDocCount());
    }

    /**
     * 多过滤聚合 基于多个过滤条件，来对当前文档进行【过滤】的聚合，每个过滤都包含所有满足它的文档（多个bucket中可能重复）
     */
    @Test
    public void filtersAggregation() throws Exception {
        FiltersAggregationBuilder filtersAggregation = AggregationBuilders.filters("agg",
            new FiltersAggregator.KeyedFilter("men", QueryBuilders.termQuery("gender", "male")),
            new FiltersAggregator.KeyedFilter("women", QueryBuilders.termQuery("gender", "female")));

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(filtersAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Filters agg = searchResponse.getAggregations().get("agg");

        for (Filters.Bucket entry : agg.getBuckets()) {
            String key = entry.getKeyAsString();
            long docCount = entry.getDocCount();
            System.out.println(String.format("key [{%s}], doc_count [{%s}]", key, docCount));
        }
    }

    /**
     * 基于字段数据的单桶聚合 创建当前文档集上下文中缺少字段值的所有文档的bucket（桶）（有效地，丢失了一个字段或配置了NULL值集），
     * 此聚合器通常与其他字段数据桶聚合器（例如范围）结合使用，以返回由于缺少字段数据值而无法放置在任何其他存储区中的所有文档的信息
     */
    @Test
    public void missingAggregation() throws Exception {
        MissingAggregationBuilder missingAggregation = AggregationBuilders.missing("agg").field("age");

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(missingAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Missing agg = searchResponse.getAggregations().get("agg");

        System.out.println(agg.getDocCount());
    }

    /**
     * 词元聚合 基于某个field，该 field 内的每一个【唯一词元】为一个桶，并计算每个桶内文档个数。 Text 字段设置 "fielddata": true，才支持terms aggregation 或者用
     * text.keyword
     */
    @Test
    public void termsAggregation() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("genders").field("gender");

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(termsAggregationBuilder);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Terms genders = searchResponse.getAggregations().get("genders");

        for (Terms.Bucket entry : genders.getBuckets()) {
            System.out.println(entry.getKey() + ":" + entry.getDocCount());
        }
    }

    /**
     * 范围聚合
     */
    @Test
    public void rangeAggregation() throws Exception {
        RangeAggregationBuilder rangeAggregation = AggregationBuilders.range("agg").field("age")
            .addUnboundedFrom(">20", 20).addUnboundedTo("<=25", 25).addRange("(30, 32]", 30, 32);

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(rangeAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Range agg = searchResponse.getAggregations().get("agg");

        for (Range.Bucket bucket : agg.getBuckets()) {
            System.out.println(bucket.getKeyAsString() + ":" + bucket.getDocCount());

        }
    }

    /**
     * 直方图统计 根据值分段统计
     */
    @Test
    public void histogramAggregation() throws Exception {
        HistogramAggregationBuilder histogramAggregation =
            AggregationBuilders.histogram("agg").field("age").interval(20);

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(histogramAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Histogram agg = searchResponse.getAggregations().get("agg");

        for (Histogram.Bucket entry : agg.getBuckets()) {
            System.out.println(entry.getKey() + ":" + entry.getDocCount());
        }
    }
}
