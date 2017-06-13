package lsm.interview.test;

/**
 * Created by lishenming on 2017/3/25.
 */
public class TestNo19 {
    public static void main(String[] args) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                pong();
            }
        };
        thread.start();
        System.out.println(thread.getName());
        System.out.println("ping");
    }

    static void pong(){
        System.out.println("pong");
    }
}
