package lsm.string;

import org.junit.Test;

/**
 * Created by za-lishenming on 2017/4/24.
 */
public class StringBuildTest {

    @Test
    public void testAppend() {

        StringBuilder builder = new StringBuilder();

        builder.append("test|").append(true).append(8);

        System.out.println(builder.toString());
    }

    @Test
    public void testSubSequence() {
        StringBuilder builder = new StringBuilder("1234567890abcdefghijklmn");

        System.out.println(builder.subSequence(0, builder.length()));

        System.out.println(builder.toString());
    }
}
