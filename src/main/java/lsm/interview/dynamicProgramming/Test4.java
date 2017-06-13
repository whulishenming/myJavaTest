package lsm.interview.dynamicProgramming;

/**
 * Created by lishenming on 2017/3/25.
 */
public class Test4 {
    private static int[][] arr = {
            {7},
            {3, 8},
            {8, 1, 0},
            {2, 7, 4, 4},
            {4, 5, 2, 6, 5},
            {6, 7, 5, 3, 9, 10},
            {8, 6, 7, 9, 11, 15, 14},
            {8, 6, 7, 9, 11, 15, 14, 15},
            {8, 6, 7, 9, 11, 15, 14, 15, 11},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15, 22},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15, 22, 15},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15, 22, 15, 16},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15, 22, 15, 16, 18},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15, 22, 15, 16, 18, 9},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15, 22, 15, 16, 18, 9, 16},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15, 22, 15, 16, 18, 9, 16, 21},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15, 22, 15, 16, 18, 9, 16, 26, 16},
            {8, 6, 7, 9, 11, 15, 14, 15, 11, 15, 22, 15, 16, 18, 9, 16, 26, 16, 26}
    };
    private static final int n = arr.length;
    private static int[] maxSum = arr[n-1];
    public static void main(String[] args) {
        long now = System.currentTimeMillis();

        for (int i = n - 2; i >= 0; i--){
            for (int j = 0; j <= i; j++) {
                maxSum[j] = Math.max(maxSum[j], maxSum[j+1]) + arr[i][j];
            }
        }
        System.out.println(maxSum[0]);
        System.out.println(System.currentTimeMillis() - now);
    }


}
