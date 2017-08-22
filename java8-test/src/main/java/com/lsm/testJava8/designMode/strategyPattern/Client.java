package com.lsm.testJava8.designMode.strategyPattern;

import org.junit.Test;

public class Client {
    @Test
    public void test() {
        Validator numericValidator =  new Validator((String s) -> s.matches("[a-z]+"));

        boolean b1 = numericValidator.validate("aaaa");
        System.out.println(b1);

        Validator lowerCaseValidator = new Validator((String s) -> s.matches("\\d+"));

        boolean b2 = lowerCaseValidator.validate("bbbb");
        System.out.println(b2);
    }
}
