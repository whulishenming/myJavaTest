package lsm.string;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenming.li on 2017/6/20.
 */
public class StringUtilTest {
    @Test
    public void test() {
        List<String> phoneNumList = new ArrayList<>();
        phoneNumList.add("18312341234");
        phoneNumList.add("18312341235");
        String join = StringUtils.join(phoneNumList, ",");
        System.out.println(join);
    }

    @Test
    public void test2() {
        int phoneNumSize = 499;
        List<String> phoneNumList = new ArrayList<>();
        for (int i = 0; i < phoneNumSize; i++) {
            phoneNumList.add(i + "");
        }
        if (phoneNumSize > 500) {
            int count = phoneNumSize / 500 + 1;
            for (int i = 1; i <= count; i++) {
                int start = (i - 1) * 500;
                int end;
                if (i == count) {
                    end = phoneNumSize;
                }else{
                    end = i * 500;
                }
                List<String> strings = phoneNumList.subList(start, end);
                System.out.println(strings.get(0));
            }
        }else{
            System.out.println(phoneNumList.get(0));
        }
    }
}
