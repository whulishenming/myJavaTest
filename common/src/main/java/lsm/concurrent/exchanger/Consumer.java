package lsm.concurrent.exchanger;

import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by lishenming on 2017/3/11.
 */
public class Consumer implements Runnable{
    private List<String> buffer;

    private final Exchanger<List<String>> exchanger;

    public Consumer(List<String> buffer,Exchanger<List<String>> exchanger){
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        int cycle = 1;
        for(int i = 0 ; i < 10 ; i++){
            System.out.println("Consumer : Cycle :" + cycle);

            //调用exchange()与消费者进行数据交换
            try {
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Consumer size:" + buffer.size());
            for(int j = 0 ; j < 10 ; j++){
                System.out.println("Consumer : " + buffer.get(0));
                buffer.remove(0);
            }
            cycle++ ;
        }
    }
}
