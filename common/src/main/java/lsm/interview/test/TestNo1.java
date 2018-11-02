package lsm.interview.test;

/**
 * Created by lishenming on 2017/3/25.
 */
public class TestNo1 {
    public static void main(String[] args) {
        int a = 10 >> 1;
        int b = a++;
        int c = ++a;
        int d = b * a++;

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
    }
}
