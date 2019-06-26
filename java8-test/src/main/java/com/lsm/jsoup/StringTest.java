package com.lsm.jsoup;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author lishenming
 * @date 2018/11/15 17:19
 **/

public class StringTest {

    @Test
    public void test() {
        StringJoiner joiner = new StringJoiner("，", "", "。");
        joiner.add("到发车时间后，不支持退票");
        joiner.add("发车前60分钟以内办理退票，按票面额30%扣除手续费");
        joiner.add("发车前60分钟（含）至120分钟办理退票，按票面额20%扣除手续费");
        joiner.add("发车前120分钟办理退票，不收取手续费");

        String join = joiner.toString();

        System.out.println(join);

        String join1 = String.join("&", "a=1", "b=2", "c=3");
        System.out.println("join1 = " + join1);

        List<String> paramList = Arrays.asList("param1=1", "param2=2");
        String join2 = String.join("&", paramList);
        System.out.println(join2);

        paramList.forEach(joiner::add);

        System.out.println(joiner.toString());

        StringJoiner merge = joiner.merge(new StringJoiner("delimiter", "prefix", "suffix"));
        merge.add("param5=5");
        merge.add("param6=6");

        System.out.println("merge=" + merge.toString());

        boolean contains = "sasas".contains(null);

        System.out.println(contains);

    }

}
