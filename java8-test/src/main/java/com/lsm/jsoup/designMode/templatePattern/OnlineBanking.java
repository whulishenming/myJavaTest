package com.lsm.jsoup.designMode.templatePattern;

import com.lsm.jsoup.domain.User;

import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

public abstract class OnlineBanking {

    public void processUser(String name, Consumer<User> makeCustomerHappy){

        System.out.println("get User by name");

        makeCustomerHappy.accept(new User(name, 25, Arrays.asList("paly game"), new Date()));
    }

}