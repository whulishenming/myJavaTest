package lsm.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * @author lishenming
 * @date 2018/11/13 16:30
 **/

@Data
public class Pay {
    private String orderId;

    private Integer num;

    private Date date;

    private Calendar calendar;

    public static void main(String[] args) {
        Pay pay = new Pay();
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        pay.setCalendar(calendar);
        pay.setDate(now);
        pay.setNum(1);
        pay.setOrderId("1111");

        String jsonString = JSONObject.toJSONString(pay);

        System.out.println(jsonString);

        Pay pay1 = JSONObject.parseObject(jsonString, Pay.class);

        System.out.println(pay1);

    }
}
