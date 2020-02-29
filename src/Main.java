import java.io.File;
import java.util.*;

public class Main {
    private static ArrayList<ArrayList<Double>> matrix_start = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> matrix = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int inputMethod = -1;

        while (inputMethod == -1) {
            showSelectInputMessage();
            try {
                int i = Integer.parseInt(scanner.nextLine());
                if (i == 0 || i == 1) inputMethod = i;
                else throw new Exception("Ошибка ввода!");
            } catch (NoSuchElementException e) {
                System.exit(-1);
            } catch (Exception ignored) {
            }
        }

        System.out.printf("Выбран метод ввода %s\n", (inputMethod == 0 ? "с клавиатуры" : "из файла"));

        if (inputMethod == 1) {
            System.out.println("Файл должен иметь следующий формат:\n1) На первой строке указано число n - размерность матрицы\n" +
                    "2) На следующих n строках находится n+1 чисел - элементы матрицы, где последнее число строки является свободным членом");
            System.out.println("Введите имя файла");
            try {
                File file = new File(scanner.nextLine());
                if (!file.exists()) throw new Exception("Такого файла нет");
                else scanner = new Scanner(file);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }
        if (inputMethod == 0) System.out.println("Введите размерность n");

        int n = -1;
        while (n == -1) {
            try {
                int i = Integer.parseInt(scanner.nextLine());
                if (i < 2 || i > 20) throw new Exception("Выход за диапазон");
                n = i;
            } catch (NoSuchElementException e) {
                System.exit(-1);
            } catch (Exception e) {
                System.out.println("Размерность n должна быть числом в диапазона [2;20]\nВведите размерность n");
            }
        }

        Boolean isRandom = null;
        if (inputMethod == 0) while (isRandom == null) {
            System.out.print("Заполнить матрицу случайными коэфицентами? (Y/N): ");
            try {
                String letter = scanner.nextLine();
                letter = letter.toLowerCase();
                if ("y".equals(letter)) isRandom = true;
                else if ("n".equals(letter)) isRandom = false;
                else throw new Exception("Неправильная буква!");
            } catch (NoSuchElementException ignored) {
                System.exit(-1);
            } catch (Exception e) {
                System.out.println("Y or N");
            }
        }

        if (inputMethod == 1 || !isRandom) {
            if (inputMethod == 0)
                System.out.printf("Введите %d строк по %d чисел, разделенных пробелом, где последнее число в строке - свободный член\n", n, n + 1);
            for (int i = 0; i < n; i++) {
                if (inputMethod == 0) System.out.printf("Строка %d: ", i + 1);
                try {
                    ArrayList<Double> row = new ArrayList<>();
                    String line = scanner.nextLine();
                    String[] numbers = line.split(" ");
                    if (numbers.length != n + 1) throw new Exception("Неверное количество чисел");
                    for (int j = 0; j < n + 1; j++) {
                        double elem = Double.parseDouble(numbers[j].replace(",", "."));
                        row.add(elem);
                    }
                    matrix.add(row);
                } catch (NoSuchElementException ignored) {
                    if (inputMethod == 1) System.out.println("Неверный формат файла");
                    System.exit(-1);
                } catch (Exception e) {
                    if (inputMethod == 1) {
                        System.out.println("Неверный формат файла");
                        System.exit(-1);
                    }
                    i--;
                    System.out.println("Введены неверные данные, повторите ввод строки");
                }
            }
        } else {
            Random random = new Random();
            int rangeMin = -100;
            int rangeMax = 100;
            for (int i = 0; i < n; i++) {
                ArrayList<Double> row = new ArrayList<>();
                for (int j = 0; j < n + 1; j++) row.add(rangeMin + (rangeMax - rangeMin) * random.nextDouble());
                matrix.add(row);
            }
        }
        matrix_start = new ArrayList<>(matrix);
        printMatrix("Введенная матрица: ");
        System.out.printf("Определитель матрицы = %f\n", findDeterminant(matrix));
        gauss();
        printMatrix("Треугольная матрица: ");
        ArrayList<Double> vars = getVars();
        System.out.printf("Решение системы: %s\n", vars);
        System.out.printf("Невязки: %s\n", accuracy(vars));
    }

    private static void selectMainElement(int row) {
        Double max = null;
        for (int i = row; i < matrix.size(); i++) {
            ArrayList<Double> doubles = matrix.get(i);
            if (max == null || Math.abs(doubles.get(row)) > Math.abs(max)) {
                max = doubles.get(row);
                matrix.remove(doubles);
                matrix.add(row, doubles);
            }
        }
    }

    public static double findDeterminant(ArrayList<ArrayList<Double>> matrix) {
        double[][] m = new double[matrix.size()][matrix.size()];
        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Double> doubles = matrix.get(i);
            for (int i1 = 0; i1 < doubles.size() - 1; i1++) {
                Double aDouble = doubles.get(i1);
                m[i][i1] = aDouble;
            }
        }
        return findDeterminant(m, m.length);
    }

    private static double findDeterminant(double[][] a, int n) {
        int i, j;
        double det = 0;
        double[][] matr;
        if (n == 2) det = a[0][0] * a[1][1] - a[0][1] * a[1][0];
        else {
            matr = new double[n - 1][n - 1];
            for (i = 0; i < n; ++i) {
                for (j = 0; j < n - 1; ++j) {
                    if (j < i) matr[j] = a[j];
                    else matr[j] = a[j + 1];
                }
                det += Math.pow(-1, (i + j)) * findDeterminant(matr, n - 1) * a[i][n - 1];
            }
        }
        return det;
    }

    private static ArrayList<Double> accuracy(ArrayList<Double> vars) {
        ArrayList<Double> acc_list = new ArrayList<>();

        for (ArrayList<Double> doubles : matrix_start) {
            double adc = 0.0;
            for (int i = 0; i < doubles.size() - 1; i++) {
                double v = doubles.get(i) * vars.get(i);
                v = (double) Math.round(v * 1000d) / 1000d;
                adc += v;
            }
            acc_list.add(Double.sum(-adc, (double) Math.round(doubles.get(doubles.size() - 1) * 1000d) / 1000d));
        }
        return acc_list;
    }

    public static void gauss() {
        for (int i = 0; i < matrix.size(); i++) {
            selectMainElement(i);
            ArrayList<Double> i_line = matrix.get(i);

            for (int m = i + 1; m < matrix.size(); m++) {
                ArrayList<Double> m_line = matrix.get(m);
                double v = m_line.get(i) / -i_line.get(i);
                for (int j = 0; j < m_line.size(); j++) {
                    m_line.set(j, m_line.get(j) + (i_line.get(j) * v));
                }
            }
        }
    }

    public static ArrayList<Double> getVars() {
        HashMap<Integer, Double> vars = new HashMap<>();
        for (int i = matrix.size() - 1; i >= 0; i--) {
            ArrayList<Double> doubles = matrix.get(i);
            double free_elem = (double) Math.round(doubles.get(doubles.size() - 1) * 1000d) / 1000d;
            double now_var = (double) Math.round(doubles.get(i) * 1000d) / 1000d;
            vars.put(i, (free_elem - arrAdc(vars, doubles)) / now_var);
        }
        return new ArrayList<>(vars.values());
    }

    private static double arrAdc(HashMap<Integer, Double> vars, ArrayList<Double> ks) {
        double adc = 0;
        for (int i = 0; i < ks.size(); i++) {
            double var = 0;
            if (vars.containsKey(i)) var = vars.get(i) * ks.get(i);
            adc += var;
        }
        return adc;
    }

    private static void printMatrix(String message) {
        System.out.println(message);
        for (ArrayList<Double> doubles : matrix) {
            for (int i = 0; i < doubles.size(); i++) {
                Double aDouble = doubles.get(i);
                if (i == matrix.size()) System.out.printf("\t|%10.4f\n", aDouble);
                else System.out.printf("%10.4f", aDouble);
            }
        }
    }

    private static void showSelectInputMessage() {
        System.out.println("Выберите метод ввода данных:");
        System.out.println("0 - с клавиатуры");
        System.out.println("1 - из файла");
    }
}