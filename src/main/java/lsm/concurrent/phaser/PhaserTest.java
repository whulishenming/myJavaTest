package lsm.concurrent.phaser;

import org.junit.Test;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Created by za-lishenming on 2017/5/14.
 * 1. arriveAndDeregister() 任务完成，取消自己的注册
 * 2. arriveAndAwaitAdvance() 自己完成等待其他参与者完成，进入阻塞，直到Phaser成功进入下个阶段
 * 3. arrive() 某个参与者完成任务后调用
 * 4. awaitAdvance(int phase) 该方法等待某一阶段执行完毕。如果当前阶段不等于指定的阶段或者该Phaser已经被终止，则立即返回。
 *                            该阶段数一般由arrive()方法或者arriveAndDeregister()方法返回。返回下一阶段的序号，或者返回参数指定的值（如果该参数为负数），或者直接返回当前阶段序号（如果当前Phaser已经被终止）
 */
public class PhaserTest {

    @Test
    public void testPhaser1(){
        Phaser phaser = new Phaser(5);
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "执行任务完成，等待其他任务执行......");
                //等待其他任务执行完成
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() + "继续执行任务...");
            }).start();
        }
    }

    @Test
    public void testPhaser2(){
        Phaser phaser = new Phaser(1); //相当于CountDownLatch(1)
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                phaser.awaitAdvance(phaser.getPhase());        //countDownLatch.await()
                System.out.println(Thread.currentThread().getName() + "执行任务...");
            }).start();
        }
        try {
            //等待3秒
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        phaser.arrive();        //countDownLatch.countDown()
    }


}
