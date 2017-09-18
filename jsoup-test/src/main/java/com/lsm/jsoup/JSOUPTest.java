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
}
