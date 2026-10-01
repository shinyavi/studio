import java.util.*;

public class MoveNegativesTest {
    public static void move(int[] arr) {
        int j = 0;
       for (int i = 0; i < arr.length; i++) {
            if (arr[i] < 0) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                j++;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {5, -3, 8, -1, 4, -2, 7};
        System.out.println("BeforeTest moving negatives:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        
        move(arr);
        Arrays.sort(arr);// Step 1: sort
        System.out.println("\nAfterRt moving negatives:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
