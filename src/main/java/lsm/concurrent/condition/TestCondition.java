package lsm.concurrent.condition;

/**
 * Created by lishenming on 2017/3/2.
 */
public class TestCondition {
    public static void main(String[] args) {
        Depot depot = new Depot();

        Producer producer = new Producer(depot);
        Customer customer = new Customer(depot);

        producer.produce(10);
        customer.consume(5);
        producer.produce(15);
        customer.consume(10);
        customer.consume(15);
        producer.produce(10);
    }
}
