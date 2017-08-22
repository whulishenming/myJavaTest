package com.lsm.testJava8.designMode.strategyPattern;

@FunctionalInterface
public interface ValidationStrategy {

    boolean execute(String s);
}
