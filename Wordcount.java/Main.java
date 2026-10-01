import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 2, 4, 1, 5};
        
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();
        
        for (int num : arr) {
            if (!seen.add(num)) {
                duplicates.add(num);
            }
        }
        
        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Duplicates found: " + duplicates);
    }
}
