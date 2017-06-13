package lsm.designMode.singleton;

/**
 * Created by lishenming on 2017/3/11.
 * 单例模式
 */
public class Singleton1 {
    /**
     * 第一次加载类的时候会连带着创建Singleton实例。这段代码保证了线程安全
     * 缺点是：如果这个Singleton实例的创建非常消耗系统资源，而应用始终都没有使用Singleton实例，那么创建Singleton消耗的系统资源就被白白浪费了
     */
    private static Singleton1 uniqueInstance = new Singleton1();

    private Singleton1() {
    }

    public static Singleton1 getInstance() {
        // 已经有实例了，直接使用它
        return uniqueInstance;
    }
}
