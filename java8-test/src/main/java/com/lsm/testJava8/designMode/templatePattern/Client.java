package com.lsm.testJava8.designMode.templatePattern;

import com.lsm.testJava8.domain.User;
import org.junit.Test;

public class Client {

    @Test
    public void test() {

        new OnlineBankingLambda().processUser("lsm", (User u) -> System.out.println("Hello " + u.getName()));

    }
}
