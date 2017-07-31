package lsm.concurrent.exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by lishenming on 2017/3/11.
 * 首先生产者Producer、消费中Consumer首先都创建一个缓存列表，通过Exchanger来同步交换数据。
 * 消费中通过调用Exchanger与生产者进行同步来获取数据，而生产者则通过for循环向缓存队列存储数据并使用exchanger对象消费者同步。
 * 到消费者从exchanger哪里得到数据后，他的缓冲列表中有10个数据，而生产者得到的则是一个空的列表。
 * 上面的例子充分展示了消费者-生产者是如何利用Exchanger来完成数据交换的。
 *
 * 在Exchanger中，如果一个线程已经到达了exchanger节点时，对于它的伙伴节点的情况有三种：
 *
 * 1、如果它的伙伴节点在该线程到达之间已经调用了exchanger方法，则它会唤醒它的伙伴然后进行数据交换，得到各自数据返回。
 *
 * 2、如果它的伙伴节点还没有到达交换点，则该线程将会被挂起，等待它的伙伴节点到达被唤醒，完成数据交换。
 *
 * 3、如果当前线程被中断了则抛出异常，或者等待超时了，则抛出超时异常。
 */
public class TestExchanger {
    public static void main(String[] args) {
        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();

        Exchanger<List<String>> exchanger = new Exchanger<>();

        Producer producer = new Producer(buffer1, exchanger);
        Consumer consumer = new Consumer(buffer2, exchanger);

        Thread thread1 = new Thread(producer);
        Thread thread2 = new Thread(consumer);

        thread1.start();
        thread2.start();
    }
}
