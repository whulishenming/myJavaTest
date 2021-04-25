package com.lsm.elasticsearch.api.aggregations;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import com.lsm.elasticsearch.ElasticSearchBaseTest;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/11/26 18:25 结构化聚合,在聚合中定义子聚合
 **/

public class StructuringAggregationTest extends ElasticSearchBaseTest {

    /**
     * 查询每个职位的最高工资
     */
    @Test
    public void avgBucket() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("Job_salary_stats")
            .field("job.keyword").subAggregation(AggregationBuilders.avg("avgSalary").field("salary"));

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(termsAggregationBuilder);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Terms salaryStats = searchResponse.getAggregations().get("Job_salary_stats");

        for (Terms.Bucket entry : salaryStats.getBuckets()) {
            Max maxSalary = entry.getAggregations().get("maxSalary");
            System.out.println(String.format("{}的最高工资为{%s}", entry.getKeyAsString(), maxSalary.getValue()));
        }
    }

    /**
     * 查询每个职位下每个性别的最高工资
     */
    @Test
    public void structuringAggregation2() throws Exception {
        TermsAggregationBuilder termsAggregationBuilder =
            AggregationBuilders.terms("Job_salary_stats").field("job.keyword").subAggregation(AggregationBuilders
                .terms("genders").field("gender").subAggregation(AggregationBuilders.max("maxSalary").field("salary")));

        SearchSourceBuilder searchSourceBuilder =
            new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(0).aggregation(termsAggregationBuilder);

        SearchRequest searchRequest = new SearchRequest("employees").source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Terms salaryStats = searchResponse.getAggregations().get("Job_salary_stats");

        for (Terms.Bucket entry : salaryStats.getBuckets()) {
            Terms genders = entry.getAggregations().get("genders");
            for (Terms.Bucket bucket : genders.getBuckets()) {
                Max maxSalary = bucket.getAggregations().get("maxSalary");
                System.out.println(String.format("{%s}-{%s}的最高工资为{}", entry.getKeyAsString(), bucket.getKeyAsString(),
                    maxSalary.getValue()));
            }

        }
    }
}
