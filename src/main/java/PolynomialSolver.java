import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PolynomialSolver {

    public static void main(String[] args) {
        String filePath = "test_case.json"; // Path to the JSON file

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(filePath));
            JSONObject keys = (JSONObject) jsonObject.get("keys");
            int n = Integer.parseInt(keys.get("n").toString());
            int k = Integer.parseInt(keys.get("k").toString());

            if (n < k) {
                throw new IllegalArgumentException("Insufficient roots to determine the polynomial. n must be >= k.");
            }

            List<double[]> points = new ArrayList<>();

            for (Object key : jsonObject.keySet()) {
                if (!key.equals("keys")) {
                    JSONObject pointData = (JSONObject) jsonObject.get(key);
                    int base = Integer.parseInt(pointData.get("base").toString());
                    String value = pointData.get("value").toString();
                    int x = Integer.parseInt((String) key);
                    double y = convertToDecimal(value, base);
                    points.add(new double[]{x, y});
                }
            }

            double constantTerm = lagrangeInterpolation(points);
            System.out.println("The constant term c is: " + constantTerm);

        } catch (IOException | ParseException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private static double convertToDecimal(String value, int base) {
        return (double) Long.parseLong(value, base);
    }

    private static double lagrangeInterpolation(List<double[]> points) {
        double constantTerm = 0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            double xi = points.get(i)[0];
            double yi = points.get(i)[1];
            double li = 1;

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    double xj = points.get(j)[0];
                    li *= (-xj) / (xi - xj);
                }
            }

            constantTerm += yi * li;
        }

        return constantTerm;
    }
}
