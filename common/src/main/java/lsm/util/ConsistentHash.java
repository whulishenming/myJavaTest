package lsm.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-08-06 09:22
 **/

public class ConsistentHash<T> {

    public interface HashFunc {
        Long hash(String key);
    }

    /**
     * Hash计算对象，用于自定义hash算法
     */
    private HashFunc hashFunc;

    /**
     * 复制的节点个数
     */
    private int numberOfReplicas = 300;

    /**
     * 一致性Hash环
     */
    private final SortedMap<Long, T> circle = new TreeMap<>();

    public ConsistentHash(int numberOfReplicas, Collection<T> nodes) {
        this.numberOfReplicas = numberOfReplicas;
        this.hashFunc = key -> {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");

                md5.reset();
                md5.update(key.getBytes());
                byte[] bKey = md5.digest();

                long res = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16)
                        | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF);
                return res & 0xffffffffL;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return 0L;
        };
        //初始化节点
        for (T node : nodes) {
            add(node);
        }
    }

    public ConsistentHash(HashFunc hashFunc, int numberOfReplicas, Collection<T> nodes) {
        this.numberOfReplicas = numberOfReplicas;
        this.hashFunc = hashFunc;
        //初始化节点
        for (T node : nodes) {
            add(node);
        }
    }

    /**
     * 添加虚拟节点
     * numberOfReplicas为虚拟节点的数量，初始化hash环的时候传入，我们使用300个虚拟节点
     * @param node
     */
    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashFunc.hash(node.toString() + i), node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunc.hash(node.toString() + i));
        }
    }

    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        long hash = hashFunc.hash((String) key);
        if (!circle.containsKey(hash)) {
            //返回此映射的部分视图，其键大于等于 hash
            SortedMap<Long, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }
}
