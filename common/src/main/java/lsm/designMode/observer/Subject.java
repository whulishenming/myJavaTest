package lsm.designMode.observer;

/**
 * Created by lishenming on 2017/3/11.
 */
public interface Subject{
    // registerObserver和removeObserver都需要一个观察者作为变量，该观察者是用来注册或被删除的
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    // 当主题状态改变时。这个方法会被调用，以通知所有的观察者
    void notifyObservers();
}
