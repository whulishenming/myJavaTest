package lsm.designMode.singleton;

/**
 * Created by lishenming on 2017/3/25.
 */
public class Client {

    public static void main(String[] args) {
        Singleton1 singleton1 = Singleton1.getInstance();
        Singleton1 singleton2 = Singleton1.getInstance();;
        System.out.println(singleton1 == singleton2);
        Singleton2 singleton21 = Singleton2.getInstance();
        Singleton2 singleton22 = Singleton2.getInstance();
        System.out.println(singleton21 == singleton22);
    }
}
