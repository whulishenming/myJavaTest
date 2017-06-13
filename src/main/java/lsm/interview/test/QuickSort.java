package lsm.interview.test;

/**
 * Created by lishenming on 2017/3/25.
 */
public class QuickSort {

    private static void quickSort(int[] array, int left, int right){
        if(left < right){
            int key = array[left];
            int low = left;
            int high = right;
            while(low < high){
                while(low < high && array[high] > key){
                    high--;
                }
                if(low < high){
                    array[low++] = array[high];
                }
                while(low < high && array[low] < key){
                    low++;
                }
                if(low < high)
                    array[high--] = array[low];
            }
            array[low] = key;
            quickSort(array,left,low-1);
            quickSort(array,low+1,right);
        }
    }

    public static void main(String[] args) {
        int[] array = {72, 6, 57, 88, 60, 42, 25, 73, 85, 56, 60, 45};
        quickSort(array, 0, 11);
        System.out.println(array);
    }
}
