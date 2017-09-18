package com.lsm.jsoup.designMode.strategyPattern;

@FunctionalInterface
public interface ValidationStrategy {

    boolean execute(String s);
}
