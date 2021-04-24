package lsm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/7/29 09:29
 **/

public class ConcurrentMapBug {
    private static Map<Integer, Integer> concurrentMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Fibonacci result for 20 is" + fibonacci(20));
    }

    static int fibonacci(int i) {
        if (i == 0){
            return i;
        }

        if (i == 1){
            return 1;
        }

        return concurrentMap.computeIfAbsent(i, (key) -> {
            System.out.println("Value is " + key);
            return fibonacci(i - 2) + fibonacci(i - 1);
        });
    }

}
