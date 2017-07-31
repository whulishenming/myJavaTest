package lsm.interview.test;

/**
 * Created by lishenming on 2017/3/25.
 */

public class TestNo9
{
    public static void main(String[] args) {
        Child child = new Child();
        child.printAll();
    }
}
class Parent{
    void printMe(){
        System.out.println("parent");
    }
}
class Child extends Parent{
    void printMe(){
        System.out.println("child");
    }
    void printAll(){
        super.printMe();
        this.printMe();
        printMe();
    }
}
