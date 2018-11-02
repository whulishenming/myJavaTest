package lsm.string;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by za-lishenming on 2017/5/9.
 */
public class StringNullTest {
    public static void main(String[] args) {
        /*Integer i = null;
        System.out.println(i.toString()); // 空指针异常
        System.out.println(i + "");
        System.out.println(String.valueOf(i));*/


       /* List<String> strings = JSONObject.parseArray("[T0000102, T0000118]", String.class);

        System.out.println(strings);*/

        DateTime dt6 = new DateTime(new Date());

        System.out.println(dt6);
    }
}
