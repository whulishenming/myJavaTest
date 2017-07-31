package lsm.concurrent.lock;

/**
 * Created by lishenming on 2017/5/4.
 * 1. 简单的lock实现
 * 2. 可重入锁
 */
public class MyLock2 {
    private boolean isLocked = false;
    private Thread  lockedBy = null;
    private int lockedCount = 0;

    public synchronized void lock() throws InterruptedException{
        Thread callingThread = Thread.currentThread();
        while(isLocked && lockedBy != callingThread){
            wait();
        }
        isLocked = true;
        lockedCount++;
        lockedBy = callingThread;
    }

    public synchronized void unlock(){
        if(Thread.currentThread() == this.lockedBy){
            lockedCount--;
            if(lockedCount == 0){
                isLocked = false;
                notify();
            }
        }
    }

}
