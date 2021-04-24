package lsm;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-07-25 23:04
 **/

public class ChildDemo extends ParentDemo {

    public static void main(String[] args) {
        ChildDemo demo = new ChildDemo();

        demo.test1();
        ChildDemo.test2();
        demo.test3();

        System.out.println("-------");


    }
}
