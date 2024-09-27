package libs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PolynomialSolver {

    // Method to decode the y-value from a given base
    public static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    // Lagrange interpolation to find the constant term (c)
    public static double lagrangeInterpolation(List<int[]> points) {
        int n = points.size();
        double result = 0;

        for (int i = 0; i < n; ++i) {
            double term = points.get(i)[1]; // y-value
            for (int j = 0; j < n; ++j) {
                if (i != j) {
                    term *= (0.0 - points.get(j)[0]) / (points.get(i)[0] - points.get(j)[0]);
                }
            }
            result += term;
        }

        return result;
    }

    // Simple manual JSON parser for this problem
    public static String getJsonValue(String json, String key) {
        key = "\"" + key + "\"";
        int index = json.indexOf(key);
        if (index == -1)
            return null;
        int start = json.indexOf(":", index) + 1;
        int end = json.indexOf(",", start);
        if (end == -1)
            end = json.indexOf("}", start);
        return json.substring(start, end).replace("\"", "").trim();
    }

    public static void main(String[] args) {
        try {
            // Read the JSON file content as a String
            String jsonContent = new String(Files.readAllBytes(Paths.get("input.json")));

            // Get n and k values
            int n = Integer.parseInt(getJsonValue(jsonContent, "n"));
            int k = Integer.parseInt(getJsonValue(jsonContent, "k"));

            List<int[]> points = new ArrayList<>();

            // Manually extract the points from the JSON content
            for (int i = 1; i <= n; i++) {
                String baseKey = String.valueOf(i);
                String base = getJsonValue(jsonContent, baseKey + "\"base");
                String value = getJsonValue(jsonContent, baseKey + "\"value");

                if (base != null && value != null) {
                    int x = i;
                    BigInteger y = decodeValue(value, Integer.parseInt(base));
                    points.add(new int[] { x, y.intValue() });
                }
            }

            // Apply Lagrange interpolation to find the constant term 'c'
            double c = lagrangeInterpolation(points);

            System.out.println("The constant term c is: " + c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}