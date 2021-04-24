package lsm.algorithm.graph;

import lombok.Data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/7/23 11:38
 **/

public class DAG {

    @Data
    static class Node {
        private int value;
        private List<Integer> next;

        public Node(int value) {
            this.value = value;
            next = new ArrayList<>();
        }

        public void setNext(List<Integer> list) {
            this.next = list;
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Node[] nodes = new Node[9];// 储存节点
        int a[] = new int[9];// 储存入度
        List<Integer> list[] = new ArrayList[10];// 临时空间，为了存储指向的集合
        for (int i = 1; i < 9; i++) {
            nodes[i] = new Node(i);
            list[i] = new ArrayList<Integer>();
        }
        initmap(nodes, list, a);

        // 主要流程
        Queue<Node> queue = new ArrayDeque<>();

        for (int i = 1; i < 9; i++) {
            if (a[i] == 0) {
                queue.add(nodes[i]);
            }

        }
        while (!queue.isEmpty()) {
            Node n1 = queue.poll();

            System.out.print(n1.value + " ");

            List<Integer> next = n1.next;
            for (int i = 0; i < next.size(); i++) {
                a[next.get(i)]--;// 入度减一
                if (a[next.get(i)] == 0)// 如果入度为0
                {
                    queue.add(nodes[next.get(i)]);
                }
            }
        }
    }

    private static void initmap(Node[] nodes, List<Integer>[] list, int[] a) {
        list[1].add(3);
        nodes[1].setNext(list[1]);
        a[3]++;
        list[2].add(4);
        list[2].add(6);
        nodes[2].setNext(list[2]);
        a[4]++;
        a[6]++;
        list[3].add(5);
        nodes[3].setNext(list[3]);
        a[5]++;
        list[4].add(5);
        list[4].add(6);
        nodes[4].setNext(list[4]);
        a[5]++;
        a[6]++;
        list[5].add(7);
        nodes[5].setNext(list[5]);
        a[7]++;
        list[6].add(8);
        nodes[6].setNext(list[6]);
        a[8]++;
        list[7].add(8);
        nodes[7].setNext(list[7]);
        a[8]++;

    }
}
