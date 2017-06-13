package lsm.concurrent.lock;

/**
 * Created by lishenming on 2017/5/4.
 * 1. 模拟java读写锁
 * 2. 不可重入
 * http://ifeve.com/read-write-locks/
 */
public class MyReadWriteLock {
    private int readers = 0;
    private int writers = 0;
    private int writeRequests = 0;

    /**
     * 只要没有线程拥有写锁（writers==0），且没有线程在请求写锁（writeRequests ==0），所有想获得读锁的线程都能成功获取
     */
    public synchronized void lockRead() throws InterruptedException{
        while(writers > 0 || writeRequests > 0){
            wait();
        }
        readers++;
    }

    public synchronized void unlockRead() {
        readers--;
        notifyAll();
    }

    /**
     * 当一个线程想获得写锁的时候，首先会把写锁请求数加1（writeRequests++），然后再去判断是否能够真能获得写锁
     * 当没有线程持有读锁（readers==0 ）,且没有线程持有写锁（writers==0）时就能获得写锁
     * 有多少线程在请求写锁并无关系
     */
    public synchronized void lockWrite() throws InterruptedException{
        writeRequests++;

        while(readers > 0 || writers > 0){
            wait();
        }
        writeRequests--;
        writers++;
    }

    public synchronized void unlockWrite(){
        writers--;
        notifyAll();
    }
}
