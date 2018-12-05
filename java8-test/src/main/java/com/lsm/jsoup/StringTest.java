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
        StringJoiner joiner = new StringJoiner("&", "http://www.test.com?", "");
        joiner.add("param1=1");
        joiner.add("param2=2");
        joiner.add("param3=3");
        joiner.add("param4=4");

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

    }

}
