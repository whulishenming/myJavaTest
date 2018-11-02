package com.lsm.jsoup;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

/**
 * 1. 从一个URL，文件或字符串中解析HTML；
 * 2. 使用DOM或CSS选择器来查找、取出数据；
 * 3. 可操作HTML元素、属性、文本；
 */
public class JSOUPTest {

    @Test
    // 解析和遍历一个HTML文档
    public void testParse(){
        String html = "<html><head><title>First parse</title></head><body><p>Parsed HTML into a doc.</p></body></html>";

        Document doc = Jsoup.parse(html);

        System.out.println(JSONObject.toJSONString(doc));


    }

    @Test
    // 从一个URL加载一个Document
    public void testConnect() {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://kaijiang.500.com/shtml/ssq/17105.shtml").get();
           /* Document doc = Jsoup.connect("http://example.com")
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .post();*/
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = doc.getElementsByClass("ball_box01").get(0).child(0).children();

        for (Element element : elements) {
            String ballNum = element.getElementsByTag("li").text();

            System.out.println(ballNum);
        }

    }

    @Test
    public void test() throws Exception {
        Document document = Jsoup.connect("https://nj.lianjia.com/ditiefang/li99666695s99667088/a2a3/?sug=%E5%A4%A9%E6%B6%A6%E5%9F%8E%E7%AB%99").timeout(50000).get();

        Element body = document.body();

        Elements elements = body.getElementsByClass("info clear");

        for (Element element : elements) {
            Elements children = element.children();
            
            Element titleElement = element.child(0).getAllElements().get(1);
            String houseTitle = titleElement.text();
            String houseHref = titleElement.attr("href");

            Element addressElement = element.child(1).getAllElements().get(1);
            String houseDesc = addressElement.text();

            Element floodElement = element.child(2).getAllElements().get(1);
            String houseFlood = floodElement.text();

            Element tagElement = element.child(4).getAllElements().get(0);
            String houseTag = tagElement.text();

            Elements priceElement = element.child(5).getAllElements();
            String totalPrice = priceElement.get(1).text();
            String unitPrice = priceElement.get(4).text();


            System.out.println(children);
        }

        System.out.println(elements);
    }
}
