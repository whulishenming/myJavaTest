package lsm.designMode.singleton;

/**
 * Created by lishenming on 2017/3/11.
 * 单例模式
 */
public class Singleton2 {

    // 可以有自己的属性
    private String context;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    private Singleton2() {
    }

    /**
     *  instance是在第一次加载SingletonContainer类时被创建的，而SingletonContainer类则在调用getInstance方法的时候才会被加载，因此也实现了惰性加载。
     *  JVM内部的机制能够保证当一个类被加载的时候，这个类的加载过程是线程互斥的
     */
    private static class SingletonContainer{

        private static Singleton2 instance = new Singleton2();
    }

    public static Singleton2 getInstance() {

        return SingletonContainer.instance;
    }
}
