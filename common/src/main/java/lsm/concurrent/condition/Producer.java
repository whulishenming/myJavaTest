package lsm.concurrent.condition;

/**
 * Created by lishenming on 2017/3/2.
 * 生产者
 */
public class Producer {
    private Depot depot;

    public Producer(Depot depot){
        this.depot = depot;
    }

    public void produce(final int value){
        new Thread(() -> depot.put(value)).start();
    }
}
