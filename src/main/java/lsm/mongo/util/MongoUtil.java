package lsm.mongo.util;


import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;

/**
 * Created by lishenming on 2017/3/29.
 */
public enum MongoUtil {

    /**
     * 定义一个枚举的元素，它代表此类的一个实例
     */
    instance;

    private MongoClient mongoClient;

    static {
        /**
         * 通过 CompositeConfiguration 读取配置文件
         */
        CompositeConfiguration config = new CompositeConfiguration();
        try {
            config.addConfiguration(new PropertiesConfiguration("mongodb.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        String ip = config.getString("host");
        int port = config.getInt("port");

        MongoClientOptions.Builder build = new MongoClientOptions.Builder();

        //如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
        build.threadsAllowedToBlockForConnectionMultiplier(50);
        build.connectTimeout(1*60*1000);
        build.maxWaitTime(2*60*1000);
        build.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
        build.socketTimeout(0);// 套接字超时时间，0无限制
        build.writeConcern(WriteConcern.MAJORITY);//
//        MongoClientURI uri = new MongoClientURI("mongodb://lishenming:lishenming@127.0.0.1:27017/?authSource=lishenming",build);
//        mongoClient = new MongoClient(uri);
        instance.mongoClient = new MongoClient(new ServerAddress(ip, port), build.build());
    }

    /**
     * 获取db
     * @param database
     * @return
     */
    public MongoDatabase getDB(String database){

        return mongoClient.getDatabase(database);
    }

    /**
     * 获取集合
     * @param database
     * @param collection
     * @return
     */
    public MongoCollection<Document> getDBCollection(String database, String collection){

        return getDB(database).getCollection(collection);
    }

}
