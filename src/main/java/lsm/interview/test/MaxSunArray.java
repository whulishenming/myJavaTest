package lsm.interview.test;

/**
 * Created by lishenming on 2017/3/25.
 */
public class MaxSunArray {

    public static void main(String[] args) {
        int[] arr = {1, -2, 3, 10, -4, 7, 2, -5};
        System.out.println(getMaxSubArraySum(arr).toString());
    }

    private static Domain getMaxSubArraySum(int[] array){
        int start = 0;
        int end = 0;
        int result = array[0];
        int sum = array[0];

        for (int i = 1; i < array.length; i++) {
            if (sum > 0){
                sum = sum + array[i];
            }else{
                start = i;
                sum = array[i];
            }

            if (sum > result){
                end = i;
                result = sum;
            }
        }

        return new Domain(start, end, sum);
    }

    private static class Domain{

        private int start;

        private int end;

        private int sum;

        public Domain(int start, int end, int sum) {
            this.start = start;
            this.end = end;
            this.sum = sum;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        @Override
        public String toString() {
            return this.getStart() + "--" + this.getEnd() + "--" + this.getSum();
        }
    }


}
