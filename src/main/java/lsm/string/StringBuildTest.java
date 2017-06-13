package lsm.string;

/**
 * Created by za-lishenming on 2017/4/24.
 */
public class StringBuildTest {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();

        builder.append("test|").append(true).append(8);

        System.out.println(builder.toString());
    }
}
