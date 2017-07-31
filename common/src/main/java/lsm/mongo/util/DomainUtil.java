package lsm.mongo.util;

import lsm.domain.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lishenming on 2017/3/31.
 */
public class DomainUtil {

    public static User getUser(){
        User user = new User();
        user.setAge(25);
        user.setName("lishenming");
        user.setCreateTime(new Date());
        List<String> likes = new ArrayList<>();
        likes.add("pingpong");
        likes.add("eating");
        user.setLikes(likes);

        return user;
    }
}
