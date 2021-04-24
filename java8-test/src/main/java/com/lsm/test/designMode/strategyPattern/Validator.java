package com.lsm.test.designMode.strategyPattern;

public class Validator {

    private final ValidationStrategy strategy;

    public Validator(ValidationStrategy v) {
        this.strategy = v;
    }

    public boolean validate(String s){
        return strategy.execute(s);
    }
}
