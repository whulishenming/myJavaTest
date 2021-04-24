package com.lsm.elasticsearch.api.aggregations;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import com.lsm.elasticsearch.ElasticSearchBaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/11/28 14:45 Pipeline Aggregations 是一组工作在其他聚合计算结果而不是文档集合的聚合, 有以下两种类型 1. Parent :
 *       以父聚合的结果作为输入，对父聚合的结果进行聚合计算。可以计算出新的桶或是新的聚合结果加入到现有的桶中 2. Sibling :
 *       以兄弟聚合（同级聚合）的结果作为输入，对兄弟聚合的结果进行聚合计算。计算出一个新的聚合结果，结果与兄弟聚合的结果同级
 **/

@Slf4j
public class PipelineAggregationsTest extends ElasticSearchBaseTest {

    /**
     * Sibling Pipeline Aggregation ，计算指定聚合结果的平均数
     */
    @Test
    public void avgBucket() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("jobs").field("job.keyword")
            .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"));

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(termsAggregationBuilder)
                .aggregation(new AvgBucketPipelineAggregationBuilder("avg_salary_by_job", "jobs>avg_salary"));

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SimpleValue simpleValue = searchResponse.getAggregations().get("avg_salary_by_job");

        System.out.println(String.format("每个工种平均工资的平均工资={%s}", simpleValue.value()));
    }

    /**
     * parent Pipeline Aggregation ，计算指定聚合结果的导数 DerivativePipelineAggregation must have a histogram, date_histogram or
     * auto_date_histogram as parent
     */
    @Test
    public void derivativeBucket() throws Exception {
        HistogramAggregationBuilder histogramAggregation = AggregationBuilders.histogram("age").field("age").interval(5)
            .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"))
            .subAggregation(new DerivativePipelineAggregationBuilder("derivative_salary_by_job", "avg_salary"));

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(histogramAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Derivative derivative = searchResponse.getAggregations().get("derivative_salary_by_job");

        System.out.println(String.format("每个工种平均工资的导数={%s}", derivative.value()));
    }

    /**
     * Sibling 类型的 Pipeline Aggregation ，计算指定聚合结果的最大值
     */
    @Test
    public void maxBucket() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("jobs").field("job.keyword")
            .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"));

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(termsAggregationBuilder)
                .aggregation(new MaxBucketPipelineAggregationBuilder("max_salary_by_job", "jobs>avg_salary"));

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        BucketMetricValue bucketMetricValue = searchResponse.getAggregations().get("max_salary_by_job");

        System.out.println(String.format("每个工种平均工资的最大值={%s}", bucketMetricValue.value()));
    }

    /**
     * Sibling 类型的 Pipeline Aggregation ，计算指定聚合结果的最小值
     */
    @Test
    public void minBucket() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("jobs").field("job.keyword")
            .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"));

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(termsAggregationBuilder)
                .aggregation(new MinBucketPipelineAggregationBuilder("min_salary_by_job", "jobs>avg_salary"));

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        BucketMetricValue bucketMetricValue = searchResponse.getAggregations().get("min_salary_by_job");

        System.out.println(String.format("每个工种平均工资的最小值={%s}", bucketMetricValue.value()));
    }

    /**
     * Sibling 类型的 Pipeline Aggregation ，计算指定聚合结果的总和
     */
    @Test
    public void sumBucket() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("jobs").field("job.keyword")
            .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"));

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(termsAggregationBuilder)
                .aggregation(new SumBucketPipelineAggregationBuilder("sum_salary_by_job", "jobs>avg_salary"));

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SimpleValue simpleValue = searchResponse.getAggregations().get("sum_salary_by_job");

        System.out.println(String.format("每个工种平均工资的总和={%s}", simpleValue.value()));
    }

    /**
     * Sibling 类型的 Pipeline Aggregation ，对指定聚合结果进行 Stats Aggregation 计算
     */
    @Test
    public void statsBucket() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("jobs").field("job.keyword")
            .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"));

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(termsAggregationBuilder)
                .aggregation(new StatsBucketPipelineAggregationBuilder("stats_salary_by_job", "jobs>avg_salary"));

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        StatsBucket statsBucket = searchResponse.getAggregations().get("stats_salary_by_job");

        double avg = statsBucket.getAvg();
        long count = statsBucket.getCount();
        double max = statsBucket.getMax();
        double min = statsBucket.getMin();
        double sum = statsBucket.getSum();

    }

    /**
     * Sibling 类型的 Pipeline Aggregation ，对指定聚合结果进行 Extended Stats Aggregation 计算
     */
    @Test
    public void extendedStatsBucket() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("jobs").field("job.keyword")
            .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0)
            .aggregation(termsAggregationBuilder).aggregation(
                new ExtendedStatsBucketPipelineAggregationBuilder("extended_stats_salary_by_job", "jobs>avg_salary"));

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        ExtendedStatsBucket extendedStatsBucket = searchResponse.getAggregations().get("extended_stats_salary_by_job");

        double avg = extendedStatsBucket.getAvg();
        long count = extendedStatsBucket.getCount();
        double max = extendedStatsBucket.getMax();
        double min = extendedStatsBucket.getMin();
        double sum = extendedStatsBucket.getSum();
        double sumOfSquares = extendedStatsBucket.getSumOfSquares();

    }

    /**
     * Sibling 类型的 Pipeline Aggregation ，对指定聚合结果进行 Percentiles Aggregation 计算
     */
    @Test
    public void percentilesBucket() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("jobs").field("job.keyword")
            .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"));

        PercentilesBucketPipelineAggregationBuilder percentilesBucketPipelineAggregation =
            new PercentilesBucketPipelineAggregationBuilder("percentiles_salary_by_job", "jobs>avg_salary")
                .setPercents(new double[] {1.0, 5.0, 10.0, 20.0, 30.0, 75.0, 95.0, 99.0});

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0)
            .aggregation(termsAggregationBuilder).aggregation(percentilesBucketPipelineAggregation);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        ParsedPercentilesBucket percentilesBucket = searchResponse.getAggregations().get("percentiles_salary_by_job");

        percentilesBucket.forEach(bucket -> {
            System.out.println(String.format("{%s}:{%s}", bucket.getPercent(), bucket.getValue()));
        });
    }
}
