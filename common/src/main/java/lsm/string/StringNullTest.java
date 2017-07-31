package lsm.string;

/**
 * Created by za-lishenming on 2017/5/9.
 */
public class StringNullTest {
    public static void main(String[] args) {
        Integer i = null;
        System.out.println(i.toString()); // 空指针异常
        System.out.println(i + "");
        System.out.println(String.valueOf(i));
    }
}
