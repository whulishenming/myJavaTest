package lsm.beanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import lsm.domain.User;
import lsm.domain.UserDomain;
import lsm.util.BeanUtils;

/**
 * Created by za-lishenming on 2017/4/26.
 */
@Slf4j
public class BeanUtilsTest {
    public static void main(String[] args) throws IllegalAccessException {
        User user = new User();
        user.setAge(18);
        user.setCreateTime(new Date());
        user.setName("test");
        user.setLikes(Arrays.asList("12", "dsds", "232"));

        log.info("test");

        // 把user的相同属性赋值到domain，不存在的属性则不复制
        UserDomain domain = new UserDomain();
        BeanUtils.copyProperties(domain, user, null);
        System.out.println(domain);
    }
}
