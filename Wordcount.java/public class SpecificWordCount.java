
public class SpecificWordCount {
public static void main(String[] args) {
String sentence = "Java is fun and Java is powerful and fun";
String[] targetWords = {"Java", "fun"};
int[] counts = new int[targetWords.length];

sentence = sentence.toLowerCase();
String[] words = sentence.split("\\s+"); //Splits the sentence into words based on whitespace

for (String word : words) {
for (int i = 0; i < targetWords.length; i++) {
if (word.equals(targetWords[i].toLowerCase())) {
counts[i]++;
}
}
}
for (int i = 0; i < targetWords.length; i++) {
System.out.println(targetWords[i] + " appears " + counts[i] + " times.");
}
}
}



