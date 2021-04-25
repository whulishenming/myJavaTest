package com.lsm.elasticsearch.api.aggregations;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import com.lsm.elasticsearch.ElasticSearchBaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/11/26 10:02 度量聚合
 **/

public class MetricsAggregationTest extends ElasticSearchBaseTest {

    /**
     * 最小值聚合
     */
    @Test
    public void minAggregation() throws Exception {
        MinAggregationBuilder minAggregation = AggregationBuilders.min("min").field("age");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(minAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Min min = searchResponse.getAggregations().get("min");

        System.out.println(String.format("min={%s}", min.getValue()));
    }

    /**
     * 最大值聚合
     */
    @Test
    public void maxAggregation() throws Exception {
        MaxAggregationBuilder maxAggregation = AggregationBuilders.max("max").field("age");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(maxAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Max max = searchResponse.getAggregations().get("max");

        System.out.println(String.format("max={%s}", max.getValue()));
    }

    /**
     * 求和聚合
     */
    @Test
    public void sumAggregation() throws Exception {
        SumAggregationBuilder sumAggregation = AggregationBuilders.sum("sum").field("age");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(sumAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Sum sum = searchResponse.getAggregations().get("sum");

        System.out.println(String.format("sum={%s}", sum.getValue()));
    }

    /**
     * 平均值聚合
     */
    @Test
    public void avgAggregation() throws Exception {
        AvgAggregationBuilder avgAggregation = AggregationBuilders.avg("avg").field("age");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(avgAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Avg avg = searchResponse.getAggregations().get("avg");

        System.out.println(String.format("avg={%s}", avg.getValue()));
    }

    /**
     * 统计聚合 基于文档的某个值，计算出一些统计信息（min、max、sum、count、avg）, 用于计算的值可以是特定的数值型字段，也可以通过脚本计算而来
     */
    @Test
    public void statsAggregation() throws Exception {
        StatsAggregationBuilder statsAggregation = AggregationBuilders.stats("stats").field("age");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(statsAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Stats stats = searchResponse.getAggregations().get("stats");

        System.out.println(String.format("min={%s}, max={%s}, sum={%s}, avg={%s}, count={%s}", stats.getMin(),
            stats.getMax(), stats.getSum(), stats.getAvg(), stats.getCount()));
    }

    /**
     * 扩展统计聚合 基于文档的某个值，计算出一些统计信息（比普通的stats聚合多了sum_of_squares、variance、std_deviation、std_deviation_bounds）,
     * 用于计算的值可以是特定的数值型字段，也可以通过脚本计算而来
     */
    @Test
    public void extendedStatsAggregation() throws Exception {
        ExtendedStatsAggregationBuilder extendedStatsAggregation =
            AggregationBuilders.extendedStats("extendedStats").field("age");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(extendedStatsAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        ExtendedStats extendedStats = searchResponse.getAggregations().get("extendedStats");

        System.out.println(String.format("min={%s}, max={%s}, sum={%s}, avg={%s}, count={%s}, 平方和={%s}",
            extendedStats.getMin(), extendedStats.getMax(), extendedStats.getSum(), extendedStats.getAvg(),
            extendedStats.getCount(), extendedStats.getSumOfSquares()));
    }

    /**
     * 值计数聚合 计算聚合文档中某个值的个数, 用于计算的值可以是特定的数值型字段，也可以通过脚本计算而来 该聚合一般域其它 single-value
     * 聚合联合使用，比如在计算一个字段的平均值的时候，可能还会关注这个平均值是由多少个值计算而来
     */
    @Test
    public void valueCountAggregation() throws Exception {
        ValueCountAggregationBuilder valueCountAggregation = AggregationBuilders.count("count").field("age");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(valueCountAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        ValueCount count = searchResponse.getAggregations().get("count");

        System.out.println(String.format("count={%s}", count.getValue()));
    }

    /**
     * 百分比线统计
     */
    @Test
    public void percentilesAggregation() throws Exception {
        PercentilesAggregationBuilder percentilesAggregation = AggregationBuilders.percentiles("percentiles")
            .field("age").percentiles(1.0, 5.0, 10.0, 20.0, 30.0, 75.0, 95.0, 99.0);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(percentilesAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Percentiles percentiles = searchResponse.getAggregations().get("percentiles");

        for (Percentile percentile : percentiles) {
            System.out
                .println(String.format("percent={%s}, value={%s}", percentile.getPercent(), percentile.getValue()));
        }
    }

    /**
     * 查询各值所占百分比排名
     */
    @Test
    public void percentileRanksAggregation() throws Exception {

        PercentileRanksAggregationBuilder percentileRanksAggregation =
            AggregationBuilders.percentileRanks("percentileRanks", new double[] {25.0, 30.0, 42.0}).field("age");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(percentileRanksAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        PercentileRanks percentileRanks = searchResponse.getAggregations().get("percentileRanks");

        for (Percentile percentile : percentileRanks) {
            System.out
                .println(String.format("percent={%s}, value={%s}", percentile.getPercent(), percentile.getValue()));
        }
    }

    /**
     * 基数聚合 distinct count
     */
    @Test
    public void cardinalityAggregation() throws Exception {

        CardinalityAggregationBuilder cardinalityAggregation =
            AggregationBuilders.cardinality("cardinality").field("age");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            // 这个作用于所有统计
            .query(QueryBuilders.matchAllQuery()).size(0).aggregation(cardinalityAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Cardinality cardinality = searchResponse.getAggregations().get("cardinality");

        System.out.println(String.format("value={%s}", cardinality.getValue()));
    }

    /**
     * 最高匹配权值聚合 跟踪聚合中相关性最高的文档。该聚合一般用做 sub-aggregation，以此来聚合每个桶中的最高匹配的文档
     */
    @Test
    public void topHitsAggregation() throws Exception {

        TopHitsAggregationBuilder topHitsAggregation =
            AggregationBuilders.topHits("top").sort("salary", SortOrder.DESC).size(1);

        TermsAggregationBuilder termsAggregation =
            AggregationBuilders.terms("jobs").field("job.keyword").subAggregation(topHitsAggregation);

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(termsAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Terms jobs = searchResponse.getAggregations().get("jobs");
        for (Terms.Bucket bucket : jobs.getBuckets()) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            System.out.println(String.format("key={%s}, doc_count={%s}", key, docCount));
            TopHits topHits = bucket.getAggregations().get("top");
            for (SearchHit hit : topHits.getHits().getHits()) {
                System.out.println(String.format(" -> id={%s}, _source={%s}", hit.getId(), hit.getSourceAsString()));
            }
        }
    }
}
