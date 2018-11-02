package lsm.string;

/**
 * @author lishenming
 * @create 2017-10-24
 **/

import java.util.ArrayList;
import java.util.List;

public class FindPrime {

    public static void main(String[] args) {
        long time = System.nanoTime();

        time = System.nanoTime();
        System.out.println(findPrime2(20171024).size());
        System.out.println(System.nanoTime() - time);
    }

    public static List<Integer> findPrime1(int n) {
        List<Integer> primes = new ArrayList<Integer>();
        primes.add(2);
        for(int i = 3; i <= n; i++) {
            for(int j = 2; j < i; j++) {
                if(i % j == 0)	break;
                if(j == i-1)	primes.add(i);
            }
        }
        return primes;
    }

    public static List<Integer> findPrime2(int n) {
        List<Integer> primes = new ArrayList<Integer>();
        primes.add(2);
        for(int i = 20161000; i <= n; i++) {
            int tmp = (int)Math.sqrt(i) + 1;
            for(int j = 2; j <= tmp; j++) {
                if(i % j == 0)	break;
                if(j == tmp)	primes.add(i);
            }
        }
        return primes;
    }

    public static List<Integer> findPrime3(int n) {
        List<Integer> primes = new ArrayList<Integer>();
        primes.add(2);
        for(int i = 20001000; i <= n; i+=2) {
            for(int j = 0; j < primes.size(); j++) {
                if(i % primes.get(j) == 0)	break;
                if(j == primes.size() - 1) { primes.add(i); break; }
            }
        }
        return primes;
    }

}
