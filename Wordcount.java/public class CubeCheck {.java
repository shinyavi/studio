public class CubeCheck {
    public static void main(String[] args) {
        int[] numbers = {2, 3, 8, 10, 27, 5};

        boolean found = false;

        for (int num : numbers) {
            int cubeRoot = (int) Math.round(Math.cbrt(num)); // find cube root
            if (cubeRoot * cubeRoot * cubeRoot == num) {     // check if it's a perfect cube
                System.out.println(num + " is a perfect cube.");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No perfect cube found in the array.");
        }
    }
}