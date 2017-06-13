package lsm.interview.dynamicProgramming;

/**
 * Created by lishenming on 2017/3/25.
 */
public class Test1 {
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
    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        System.out.println(calculateSum(0, 0));
        System.out.println(System.currentTimeMillis() - now);
    }

    public static int calculateSum(int i, int j){
        if (i == n - 1){
            return arr[i][j];
        }else{
            int x = calculateSum(i+1, j);
            int y = calculateSum(i+1, j+1);

            return Math.max(x, y) + arr[i][j];
        }

    }
}
