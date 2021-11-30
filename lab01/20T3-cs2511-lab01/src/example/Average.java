package example;

public class Average {

        /**
         * Returns the average of an array of numbers
         * @param the array of integer numbers
         * @return the average of the numbers
         */
        public float average(int[] nums) {
            float result = 0;
            int sum = 0;
            // Add your code
            for(int tmp : nums){
                sum += tmp;
            }   
            result=(float)sum / (nums.length);
            return result;
        }

        public static void main(String[] args) {
            // Add your code
            int temp[] = { 1, 2, 3, 4 };
            Average q = new Average();
            Average a = new Average();
            System.out.println(a.getClass());
            float print_put=q.average(temp);
            System.out.println(print_put);
        }
}
