import java.math.BigInteger;
import java.util.*;

public class ShamirSecretSharing {

    // Converts a number from any base to decimal
    public static BigInteger baseToDecimal(String value, int base) {
        return new BigInteger(value, base);
    }

    // Performs Lagrange interpolation to find the constant term (f(0))
    public static BigInteger lagrangeInterpolation(List<long[]> xy, List<BigInteger> yValues, int k) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(xy.get(i)[0]);
            BigInteger yi = yValues.get(i);

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = BigInteger.valueOf(xy.get(j)[0]);
                    numerator = numerator.multiply(xj.negate());
                    denominator = denominator.multiply(xi.subtract(xj));
                }
            }

            result = result.add(yi.multiply(numerator).divide(denominator));
        }

        return result;
    }

    public static BigInteger solveTestCase(Map<String, Object> testCase) {
        @SuppressWarnings("unchecked")
        Map<String, Object> keys = (Map<String, Object>) testCase.get("keys");
        int n = (int) keys.get("n");
        int k = (int) keys.get("k");

        System.out.println("\nSolving test case with n=" + n + ", k=" + k);

        List<long[]> points = new ArrayList<>();
        List<BigInteger> yValues = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            String key = String.valueOf(i);
            if (testCase.containsKey(key)) {
                @SuppressWarnings("unchecked")
                Map<String, String> point = (Map<String, String>) testCase.get(key);

                int base = Integer.parseInt(point.get("base"));
                String value = point.get("value");

                BigInteger y = baseToDecimal(value, base);
                points.add(new long[]{i});
                yValues.add(y);

                System.out.println("Point " + i + ": x=" + i + ", y=" + y + " (decoded from \"" + value + "\" in base " + base + ")");
            }
        }

        return lagrangeInterpolation(points, yValues, k);
    }

    public static void main(String[] args) {
        System.out.println("=== SHAMIR'S SECRET SHARING SOLUTION ===");

        Map<String, Object> testCase1 = new HashMap<>();
        testCase1.put("keys", Map.of("n", 4, "k", 3));
        testCase1.put("1", Map.of("base", "10", "value", "4"));
        testCase1.put("2", Map.of("base", "2", "value", "111"));
        testCase1.put("3", Map.of("base", "10", "value", "12"));
        testCase1.put("6", Map.of("base", "4", "value", "213"));

        Map<String, Object> testCase2 = new HashMap<>();
        testCase2.put("keys", Map.of("n", 10, "k", 7));
        testCase2.put("1", Map.of("base", "6", "value", "13444211440455345511"));
        testCase2.put("2", Map.of("base", "15", "value", "aed7015a346d63"));
        testCase2.put("3", Map.of("base", "15", "value", "6aeeb69631c227c"));
        testCase2.put("4", Map.of("base", "16", "value", "e1b5e05623d881f"));
        testCase2.put("5", Map.of("base", "8", "value", "316034514573652620673"));
        testCase2.put("6", Map.of("base", "3", "value", "2122212201122002221120200210011020220200"));
        testCase2.put("7", Map.of("base", "3", "value", "20120221122211000100210021102001201112121"));
        testCase2.put("8", Map.of("base", "6", "value", "20220554335330240002224253"));
        testCase2.put("9", Map.of("base", "12", "value", "45153788322a1255483"));
        testCase2.put("10", Map.of("base", "7", "value", "1101613130313526312514143"));

        System.out.println("\n--- TEST CASE 1 ---");
        BigInteger secret1 = solveTestCase(testCase1);
        System.out.println("Secret for Test Case 1: " + secret1);

        System.out.println("\n--- TEST CASE 2 ---");
        BigInteger secret2 = solveTestCase(testCase2);
        System.out.println("Secret for Test Case 2: " + secret2);

        System.out.println("\n=== FINAL RESULTS ===");
        System.out.println("Test Case 1 Secret: " + secret1);
        System.out.println("Test Case 2 Secret: " + secret2);
    }
}
