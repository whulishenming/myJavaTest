package lsm.concurrent.condition;

/**
 * Created by lishenming on 2017/3/2.
 * 消费者
 */
public class Customer {
    private Depot depot;

    public Customer(Depot depot){
        this.depot = depot;
    }

    public void consume(final int value){
        new Thread(){
            public void run(){
                depot.get(value);
            }
        }.start();
    }
}
