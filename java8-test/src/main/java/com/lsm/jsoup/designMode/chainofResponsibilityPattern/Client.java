package com.lsm.jsoup.designMode.chainofResponsibilityPattern;

import org.junit.Test;

import java.util.ArrayList;

public class Client {

    @Test
    public void test() {

        FilterChain filterChain = new FilterChain(new ArrayList<>());

        filterChain.addFilter((s) -> System.out.println("filter1 do with " + s));

        filterChain.addFilter((s) -> System.out.println("filter2 do with " + s));

        filterChain.addFilter((s) -> System.out.println("filter3 do with " + s));

        filterChain.addFilter((s) -> System.out.println("filter4 do with " + s));

        filterChain.doFilter("lambda");
    }
}
