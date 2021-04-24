package lsm.algorithm.lru;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/2/5 13:29
 **/

public class LruTest {

    @Test
    public void testV1() {
        //map的最大容量是10
        final int maxSize = 10;
        Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>(0, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size() > maxSize;
            }
        };

        System.out.println(map.size());
        for (int i = 0; i < 10; i ++){
            map.put(i, i);
        }
        System.out.println("当前有10个数据："+map.toString());
        System.out.println(map.get(6));
        System.out.println("访问key为6的数据后："+map.toString());
        map.put(11,11);
        System.out.println("插入第11个数据后："+map.toString());

    }

    @Test
    public void testV2() {
        final int maxSize = 10;
        LruCache<Integer,Integer> lruCache = new LruCache<Integer, Integer>(maxSize){
            @Override
            protected int sizeOf(Integer key, Integer value) {
                return 1;//数据大小为1
            }
        };

        for (int i = 0; i < maxSize; i ++){
            lruCache.put(i, i);
        }
        System.out.println("当前数据："+lruCache.toString());
        System.out.println("key为6的数据"+lruCache.get(6));
        System.out.println("访问key为6的数据后："+lruCache.toString());
        lruCache.put(11,11);
        System.out.println("插入value为11的数据后："+lruCache.toString());
    }
}
