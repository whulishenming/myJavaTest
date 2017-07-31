package lsm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by lishenming on 2017/3/29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDomain implements Serializable {

    private String name;

    private Integer age;

    private List<String> likes;

    private String test;

    @Override
    public String toString() {
        return "UserDomain{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", likes=" + likes +
                ", test='" + test + '\'' +
                '}';
    }
}
