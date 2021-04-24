package com.lsm.test;

import com.lsm.test.domain.OptionDomain;
import com.lsm.test.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
public class OptionalTest {

    private OptionDomain domain1 = null;

    private OptionDomain domain2 = new OptionDomain();

    private OptionDomain domain3 = new OptionDomain(new User("testName", 25, Arrays.asList("pingpong"), new Date()), "string", 111L);

    @Test
    public void test() {

        Optional<OptionDomain> optionDomain = Optional.ofNullable(domain1);
        // 存在即返回, 无则提供默认值
        OptionDomain newDomain = optionDomain.orElse(domain3);
        // 存在即返回, 无则由函数来产生
        OptionDomain newDomain2 = optionDomain.orElseGet(this::getOptionDomainFromDataBase);
        // 存在才对它做点什么
        optionDomain.ifPresent(System.out::println);
        // 使用filter 剔除特定的值
        optionDomain.filter((domain) -> "string".equals(domain.getStringDomain()))
                .ifPresent((x) -> System.out.println("ok"));
        // map
        Integer size = optionDomain
                .map(OptionDomain::getUser)
                .map(User::getLikes)
                .map(List::size)
                .orElse(0);
        log.info("optionDomain:{}", newDomain.getStringDomain());
        log.info("optionDomain:{}", newDomain2.getStringDomain());
        log.info("size:{}", size);
    }


    private OptionDomain getOptionDomainFromDataBase() {
        return new OptionDomain(new User("testName2", 26, Arrays.asList("pingpong2"), new Date()), "string2", 1112L);
    }
}
