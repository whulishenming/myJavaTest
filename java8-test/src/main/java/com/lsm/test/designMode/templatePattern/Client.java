package com.lsm.test.designMode.templatePattern;

import com.lsm.test.domain.User;
import org.junit.Test;

public class Client {

    @Test
    public void test() {

        new OnlineBankingLambda().processUser("lsm", (User u) -> System.out.println("Hello " + u.getName()));

    }
}
