package lsm.mongo;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lsm.mongo.util.DomainUtil;
import lsm.mongo.util.MongoUtil;
import lsm.util.MapObjectUtilV1;
import org.bson.BsonType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.util.*;

/**
 * Created by lishenming on 2017/3/29.
 */
public class MongoDBJDBC {

    private MongoCollection<Document> jdbcTestCollection = MongoUtil.instance.getDBCollection("test", "jdbcTestCollection");

    @Test
    public void testMongo() throws Exception {
        /**
         * 删除集合
         */
        jdbcTestCollection.drop();

        /**
         *  第一种方式 key-value {"name" : "mongo","time" : ISODate("2017-03-29T07:25:48.144Z"),"info" : {"ver" : "3.4.2"}}
         */
        Document document1 = new Document("name", "mongo").append("time", new Date()).append("info", new Document("ver", "3.4.2")).append("by", "lishenming").append("like", null);
        Document document2 = new Document("name", "mongo").append("time", new Date()).append("info", new Document("ver", "3.4.3")).append("by", "158");
        List<String> courses = new ArrayList<>();
        courses.add("math");
        courses.add("english");
        courses.add("chinese");
        courses.add("physics");
        courses.add("chemistry");
        Document document3 = new Document("name", "testUpdate").append("age1", 20).append("age2", 20).
                append("age3", 20).append("courses", courses).append("courses1", courses).append("courses2", courses).append("courses3", courses).
                append("info", new Document("ver", "3.4.2").append("time", 25)).append("test", "test").append("createTime", new Date());
        Document document4 = new Document("name", "lishenming").append("age", 20).append("info", new Document("ver", "3.4.3")).append("by", "158");
        /**
         *  第二种方式 直接插入一个map
         */
        Map<String, Object> map = MapObjectUtilV1.objectToMap(DomainUtil.getUser());
        Document mapDoc = new Document(map);

        List<Document> documents = new ArrayList<>();
        documents.add(document1);
        documents.add(document2);
        documents.add(document3);

        // 单条插入文档
        jdbcTestCollection.insertOne(mapDoc);
        // 批量插入
        jdbcTestCollection.insertMany(documents);

        /**
         * 条件查询
         */
        System.out.println("------------------------test and----------------------------------------------------");
        List<String> queryList = new ArrayList<>();
        queryList.add("mongo");
        Document doc2 = jdbcTestCollection.find(Filters.and(Filters.in("name", queryList), Filters.type("by", BsonType.STRING))).first();
        System.out.println(doc2.toJson());

        //该查询将不仅仅匹配 null 值,还会匹配不存在的字段
        System.out.println("-----------------------test eq null-----------------------------------------------");
        FindIterable<Document> doc3 = jdbcTestCollection.find(Filters.eq("like", null))
                .sort(Sorts.ascending("by"));
        for (Document document : doc3) {
            System.out.println(document.toJson());
        }
        //仅仅匹配 null 值
        System.out.println("-----------------------test filed is null-----------------------------------------------");
        FindIterable<Document> doc4 = jdbcTestCollection.find(Filters.type("like", BsonType.NULL));
        for (Document document : doc4) {
            System.out.println(document.toJson());
        }
        //存在性筛查
        System.out.println("-----------------------test filed is not exit-----------------------------------------------");
        FindIterable<Document> doc5 = jdbcTestCollection.find(Filters.exists("like", false));
        for (Document document : doc5) {
            System.out.println(document.toJson());
        }

        /**
         * 更新文档 updateOne
         * set:更新单个字段
         */
        System.out.println("-----------------------test updateOne set-----------------------------------------------");
        UpdateResult updateResult = jdbcTestCollection.updateOne(Filters.eq("name", "mongo"), Updates.set("age", 20));
        System.out.println(updateResult);


        /**
         * 投影
         * include 要显示文档的字段
         * exclude 不要要显示的字段
         */

        System.out.println("-----------------------test Projections include-----------------------------------------------");
        FindIterable<Document> doc6 = jdbcTestCollection.find().projection(Projections.include("name", "age"));
        for (Document document : doc6) {
            System.out.println(document.toJson());
        }

        /**
         * 更新文档 updateMany
         * combine(List<Bson> updates) : 更新多个字段
         * rename(String fieldName, String newFieldName) : 改字段名
         * unset(String fieldName) : 删除键
         * inc(String fieldName, Number number) : 对文档的某个值为数字型（只能为满足要求的数字）的键进行增减的操作
         * mul(String fieldName, Number number) : 对文档的某个值为数字型（只能为满足要求的数字）的键进行乘的操作
         * min(String fieldName, TItem value) : 如果原始数据更大则不修改，否则修改为指定的值
         * max(String fieldName, TItem value) : 与min相反
         * currentDate(String fieldName) ： 修改为目前的时间
         * currentTimestamp(String fieldName)
         * addToSet(String fieldName, TItem value) ： 给数组类型键值添加一个元素时，避免在数组中产生重复数据
         * addEachToSet(String fieldName, List<TItem> values)
         * push(String fieldName, TItem value) ： 向文档的某个数组类型的键添加一个数组元素，不过滤重复的数据。添加时键存在，要求键值类型必须是数组；键不存在，则创建数组类型的键
         * pushEach(String fieldName, List<TItem> values)
         * pull(String fieldName, TItem value) ： 从数组中删除满足条件的元素
         * pullAll(String fieldName, List<TItem> values)
         * popFirst(String fieldName) ： 从数组的头删除数组中的元素
         * popLast(String fieldName) ： 从数组的尾删除数组中的元素
         */
        System.out.println("-----------------------test updateMany combine rename-----------------------------------------------");
        List<Bson> updatesList = new ArrayList<>();
        updatesList.add(Updates.unset("test"));
        updatesList.add(Updates.rename("name", "name2"));
        updatesList.add(Updates.inc("age1", -5));
        updatesList.add(Updates.mul("age2", 2));
        updatesList.add(Updates.min("age3", 16));
        updatesList.add(Updates.currentTimestamp("createTime"));
        updatesList.add(Updates.addToSet("courses", "11"));
        updatesList.add(Updates.push("testPush", "111"));
        updatesList.add(Updates.pull("courses1", "math"));
        updatesList.add(Updates.popFirst("courses2"));
        updatesList.add(Updates.popLast("courses3"));
        UpdateResult updateResult1 = jdbcTestCollection.updateMany(Filters.eq("name", "testUpdate"), Updates.combine(updatesList));
        System.out.println(updateResult1);

        /**
         * 投影
         * include 要显示文档的字段
         * exclude 不要要显示的字段
         */

        System.out.println("-----------------------test Projections exclude-----------------------------------------------");
        FindIterable<Document> doc7 = jdbcTestCollection.find().projection(Projections.exclude("_id", "by"));
        for (Document document : doc7) {
            System.out.println(document.toJson());
        }

        /**
         * 删除
         */
        System.out.println("-----------------------test deleteOne deleteMany-----------------------------------------------");
        DeleteResult deleteResult = jdbcTestCollection.deleteOne(Filters.eq("name", "mongo"));
        System.out.println(deleteResult);
        DeleteResult deleteResult1 = jdbcTestCollection.deleteMany(Filters.eq("name", "mongo"));
        System.out.println(deleteResult1.getDeletedCount());


        /**
         * 程序块，一次执行多条语句
         */
        //按照语句先后顺序执行
        System.out.println("-----------------------test bulkWrite order-----------------------------------------------");
        jdbcTestCollection.bulkWrite(
                Arrays.asList(
                        new InsertOneModel<>(new Document("_id", 4)),
                        new InsertOneModel<>(new Document("_id", 5)),
                        new InsertOneModel<>(new Document("_id", 6)),
                        new UpdateOneModel<>(new Document("_id", 1), new Document("$set", new Document("x", 2))),
                        new DeleteOneModel<>(new Document("_id", 4)),
                        new ReplaceOneModel<>(new Document("_id", 6), new Document("_id", 6).append("x", 4))
                )
        );
        for (Document cur : jdbcTestCollection.find()) {
            System.out.println(cur.toJson());
        }
        System.out.println("-----------------------test bulkWrite disorder-----------------------------------------------");
        // 不按照语句先后顺序执行
        jdbcTestCollection.bulkWrite(
                Arrays.asList(
                        new InsertOneModel<>(new Document("_id", 14)),
                        new InsertOneModel<>(new Document("_id", 15)),
                        new InsertOneModel<>(new Document("_id", 16)),
                        new UpdateOneModel<>(new Document("_id", 11), new Document("$set", new Document("x", 12))),
                        new DeleteOneModel<>(new Document("_id", 14)),
                        new ReplaceOneModel<>(new Document("_id", 16), new Document("_id", 16).append("x", 14))
                ), new BulkWriteOptions().ordered(false)
        );
        for (Document cur : jdbcTestCollection.find()) {
            System.out.println(cur.toJson());
        }

        /**
         * 	聚合
         */
        System.out.println("-----------------------test aggregate match-----------------------------------------------");
        AggregateIterable<Document> documents1 = jdbcTestCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("name", "lishenming")),
                        Aggregates.project(Document.parse("{'_id': 0, 'myName':'$name', 'age':{'$add':['$age', 10]}}"))
                )
        );
        for (Document document : documents1) {
            System.out.println(document.toJson());
        }

    }


}
