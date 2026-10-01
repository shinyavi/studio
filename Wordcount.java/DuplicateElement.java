import java.util.Arrays;
public class DuplicateElement {
public static void main(String[] args) {
int[] arr = {1,2,3,4,2,7,8,8,3};

Arrays.sort(arr); // Step 1: sort

System.out.println("After removing duplicates:");

for (int i = 0; i < arr.length; i++) {
if (i == 0 || arr[i] != arr[i - 1]) {
System.out.print(arr[i]);
}
}
}
}
