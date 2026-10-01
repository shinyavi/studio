import java.util.LinkedHashMap;
import java.util.Map;

public class FirstNonRepeating {
    public static void main(String[] args) {
        String str = "swiss";

        Map<Character, Integer> map = new LinkedHashMap<>();

        // Step 1: Count frequency of each character
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            map.put(c, map.getOrDefault(c, 0) + 1); // Loops through each character in the string.
            // map.getOrDefault(c, 0) returns the current count (or 0 if not present).
        }

        // Step 2: Find the first character with count 1
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                System.out.println("First non-repeating character: " + entry.getKey());
                return;
            }
        }

        System.out.println("No non-repeating character found.");
    }
}