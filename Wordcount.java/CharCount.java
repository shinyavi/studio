import java.util.Map;
import java.util.HashMap;

public class CharCount {
    public static void main(String[] args) {
        String str = "hello";
        Map<Character, Integer> map = new HashMap<>();

        // Loop through each character
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1); // increase count
            } else {
                map.put(c, 1); // first occurrence
            }
        }

        // Print results
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}