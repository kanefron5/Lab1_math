import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Scanner;

public class MainTest {

    @Test
    public void testMain1() {

        ArrayList<ArrayList<Double>> a = getListFromString(
                "5\n" +
                        "2 3 0 5 5\n" +
                        "4 -3 -1 1 4\n" +
                        "2 5 1 3 3\n" +
                        "2 7 2 2 2\n" +
                        "5 3 4 2 1"
        );
        Main.gauss(a);
        Assert.assertEquals(Main.findDeterminant(a), 89f, 0.1);
    }

    @Test
    public void testMain2() {

        ArrayList<ArrayList<Double>> a = getListFromString(
                "5\n" +
                        "1 2 3 -2 5\n" +
                        "1 -1 -2 -3 4\n" +
                        "3 2 -1 2 3\n" +
                        "2 -3 2 1 2\n" +
                        "5 4 3 2 1"
        );
        Main.gauss(a);
        Assert.assertEquals(Main.findDeterminant(a), -1956f, 0.1);
    }

    @Test
    public void testMain3() {

        ArrayList<ArrayList<Double>> a = getListFromString(
                "6\n" +
                        "1 0 -4 2 0 1\n" +
                        "0 2 -6 0 2 0\n" +
                        "-1 -3 3 3 0 4\n" +
                        "2 2 4 4 -2 -2\n" +
                        "4 5 1 5 5 1\n" +
                        "6 -6 0 -1 0 6"
        );
        Main.gauss(a);
        Assert.assertEquals(Main.findDeterminant(a), -1940f, 0.1);
    }

    @Test
    public void testMain4() {

        ArrayList<ArrayList<Double>> a = getListFromString(
                "5\n" +
                        "2 3 0 5 5\n" +
                        "4 -3,764 -1 1 4\n" +
                        "2 5 1 3 3\n" +
                        "2 6,856 2 2 2\n" +
                        "5,002 3 4 2 1"
        );
        Main.gauss(a);
        Assert.assertEquals(Main.findDeterminant(a), 95.87837f, 0.1);
    }

    @Test
    public void testMain5() {

        ArrayList<ArrayList<Double>> a = getListFromString(
                "5\n" +
                        "2 3 0,666 5 5,909\n" +
                        "4 -3,764 -1 1 4\n" +
                        "2 5 1 3 3\n" +
                        "2 6,856 2 2.111 2\n" +
                        "5,002 3,707 4 2 1"
        );
        Main.gauss(a);
        Assert.assertEquals(Main.findDeterminant(a), 272.031376f, 0.1);
    }

    @Test
    public void testMain6() {

        ArrayList<ArrayList<Double>> a = getListFromString(
                "5\n" +
                        "6,39 -2,75 11,48 2,00 1\n" +
                        "1,72 -4,8 -5,56 -0,5994 3,2003\n" +
                        "0,48 10 -2,9528 0,8326 4,1562\n" +
                        "5,57 3 9,8 3,5348 3,5842\n" +
                        "1,11 2,28 0,86725 7,8 -0,7744"
        );
        Main.gauss(a);
        Assert.assertEquals(Main.findDeterminant(a), -8361.1276424491f, 0.1);
    }


    private ArrayList<ArrayList<Double>> getListFromString(String s) {
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        Scanner scanner = new Scanner(s);
        int n = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < n; i++) {
            ArrayList<Double> row = new ArrayList<>();
            String line = scanner.nextLine();
            String[] numbers = line.split(" ");
            for (int j = 0; j < n; j++) {
                double elem = Double.parseDouble(numbers[j].replace(",", "."));
                row.add(elem);
            }
            matrix.add(row);

        }
        return matrix;
    }
}