import java.util.*;

public class RemoveDuplicates {
public static void main(String[] args) {
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 2, 4, 3, 5));


ArrayList<Integer> uniqueList =
new ArrayList<>(new LinkedHashSet<>(list));

System.out.println(uniqueList);
}
}