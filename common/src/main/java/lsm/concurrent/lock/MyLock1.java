package lsm.concurrent.lock;

/**
 * Created by lishenming on 2017/5/4.
 * 1. 简单的lock实现
 * 2. 不可重入锁
 */
public class MyLock1 {
    private boolean isLocked = false;
    public synchronized void lock() throws InterruptedException{
        while(isLocked){
            wait();
        }
        isLocked = true;
    }
    public synchronized void unlock(){
        isLocked = false;
        notify();
    }
}
