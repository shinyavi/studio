import java.util.HashMap;
import java.util.Map;

public class DuplicateWords {
    public static void main(String[] args) {
        String sentence = "Java is fun and Java is powerful and fun";

        // Convert to lowercase and split words
        String[] words = sentence.toLowerCase().split("\\s+");

        // Store word counts
        Map<String, Integer> map = new HashMap<>();

        // Count occurrences
        for (String word : words) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        // Print duplicate words
        System.out.println("Duplicate words:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + " → " + entry.getValue());
            }
        }
    }
}