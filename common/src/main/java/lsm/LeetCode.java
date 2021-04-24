package lsm;

import lombok.Data;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-07-28 19:59
 **/

public class LeetCode {

    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }

        for (int i = 0; i < nums.length; i++) {
            int key = target - nums[i];
            if (map.containsKey(key) && map.get(key) != i) {
                result[0] = i;
                result[1] = map.get(key);
            }
        }

        return result;
    }

    @Data
    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    @Test
    public void testAddTwoNumbers() {
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);

        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(6);

        ListNode l3 = addTwoNumbers(l1, l2);
        System.out.println(l3);
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode l3 = l2;
        // 进位标记
        int flag = 0;
        while (l1 != null || l3 != null) {

            int val = (l3.val + l1.val + flag) % 10;

            flag = (l3.val + l1.val + flag) / 10;
            l3.val = val;

            if (l3.next == null && l1.next != null) {
                l3.next = new ListNode(0);
            }

            if (l1.next == null && l3.next != null) {
                l1.next = new ListNode(0);
            }

            if (l3.next == null && l1.next == null && flag > 0) {
                l3.next = new ListNode(flag);
                break;
            }

            l3 = l3.next;
            l1 = l1.next;
        }

        return l2;
    }

    public int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int res = 0;
        int end = 0, start = 0;
        Set<Character> set = new HashSet<>();
        while (start < n && end < n) {
            if (set.contains(s.charAt(end))) {
                set.remove(s.charAt(start++));
            } else {
                set.add(s.charAt(end++));
                res = Math.max(res, end - start);
            }

        }
        return res;
    }
}
