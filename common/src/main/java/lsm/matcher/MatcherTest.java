package lsm.matcher;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * Java 正则表达式
 */
@Slf4j
public class MatcherTest {

    /**
     * 以 MB_ 开头 _加3个数字结尾
     */
    @Test
    public void testMatcher() {
        String content = "MB_sasa_22";

        String pattern = "^MB_.*_\\d{3}";

        boolean isMatch = Pattern.matches(pattern, content);

        log.info("isMatch:{}", isMatch);
    }

    /**
     * 匹配字母加_
     */
    @Test
    public void testMatcher2() {
        String content = "sa1s_Asasa";

        String pattern = "^[A-Za-z_]+$";

        boolean isMatch = Pattern.matches(pattern, content);

        log.info("isMatch:{}", isMatch);
    }
}
