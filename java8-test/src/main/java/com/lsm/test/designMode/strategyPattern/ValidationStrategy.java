package com.lsm.test.designMode.strategyPattern;

@FunctionalInterface
public interface ValidationStrategy {

    boolean execute(String s);
}
