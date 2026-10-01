public class SpecificWordCount {
    public static void main(String[] args) {
        String sentence = "Java is fun and Java is powerful and fun";
        String[] targetWords = {"Java", "fun"};  // words to count
        int[] counts = new int[targetWords.length];

        // Convert sentence to lowercase to make it case-insensitive
        sentence = sentence.toLowerCase();

        // Split the sentence into words
        String[] words = sentence.split(" ");

        // Loop through words in the sentence
        for (String word : words) {
            for (int i = 0; i < targetWords.length; i++) {
                if (word.equals(targetWords[i].toLowerCase())) {
                    counts[i]++;
                }
            }
        }

        // Print the result
        for (int i = 0; i < targetWords.length; i++) {
            System.out.println(targetWords[i] + " appears " + counts[i] + " times.");
        }
    }
}