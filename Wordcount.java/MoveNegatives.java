import java.util.*;

public class MoveNegatives {
    public static void main(String[] args) {
        int[] arr = {1, -2, 3, -4, -1, 6};

        moveNegatives(arr);

        System.out.println(Arrays.toString(arr));
    }

    public static void moveNegatives(int[] arr) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            if (arr[left] < 0) {
                left++; // already negative, correct position
            } else if (arr[right] >= 0) {
                right--; // already positive, correct side
            } else {
                // swap positive (left) with negative (right)
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;

                left++;
                right--;
            }
        }
    }
}