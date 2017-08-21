package com.lsm.testJava8.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dish {

    private String name;

    private boolean vegetarian;

    private int calories;

    private Type type;

    public enum Type { MEAT, FISH, OTHER }


}
