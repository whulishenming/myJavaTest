package lsm.designMode.singleton;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-07-30 07:45
 **/

public class Singleton3 {
    private static volatile Singleton3 instance = null;

    // 可以有自己的属性
    private String context;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    private Singleton3() {
    }

    private Singleton3(String context) {
        this.context = context;
    }

    public static Singleton3 getInstance(String context){
        if(instance == null){
            synchronized(Singleton3.class){
                if(instance == null){
                    // 非原子这个步骤
                    // 其实在jvm里面的执行分为三步：
                    //  1.在堆内存开辟内存空间。
                    //  2.在堆内存中实例化SingleTon里面的各个参数。
                    //  3.把对象指向堆内存空间。
                    //
                    // 由于jvm存在乱序执行功能，所以可能在2还没执行时就先执行了3，如果此时再被切换到线程B上，由于执行了3，INSTANCE 已经非空了，
                    // 会被直接拿出来用，这样的话，就会出现异常。这个就是著名的DCL失效问题
                    instance = new Singleton3(context);
                }
            }
        }
        return instance;
    }
}
