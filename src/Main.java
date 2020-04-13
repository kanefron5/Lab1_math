import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Matrix matrix = new Matrix(inputData());
        System.out.println("Введенная матрица: ");
        System.out.println(matrix.toString());
        matrix.gauss();
        try {
            System.out.printf("Определитель матрицы = %f\n", matrix.getDeterminant());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        System.out.println("Треугольная матрица: ");
        System.out.println(matrix.toString());

        System.out.println("Решение системы:");
        ArrayList<Double> vars = matrix.getVars();
        for (int i = 0; i < vars.size(); i++) {
            System.out.printf("X%d = %s\n", i, vars.get(i));
        }

        System.out.println("Невязки:");
        ArrayList<Double> residual = matrix.getResidual();
        for (int i = 0; i < residual.size(); i++) {
            System.out.printf("X%d = %s\n", i, residual.get(i));
        }

    }

    private static ArrayList<ArrayList<Double>> inputData() {
        ArrayList<ArrayList<Double>> arr_matrix = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        Boolean isInputFromFile = null; //false - клава, true - файл

        while (isInputFromFile == null) {
            showSelectInputMessage();
            try {
                int i = Integer.parseInt(scanner.nextLine());
                switch (i) {
                    case 0:
                        isInputFromFile = false;
                        break;
                    case 1:
                        isInputFromFile = true;
                        break;
                    default:
                        throw new Exception("Ошибка ввода!");
                }
            } catch (NoSuchElementException e) {
                System.exit(-1);
            } catch (Exception ignored) {
            }
        }

        System.out.printf("Выбран метод ввода %s\n", (!isInputFromFile ? "с клавиатуры" : "из файла"));

        if (isInputFromFile) {
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
        if (!isInputFromFile) System.out.println("Введите размерность n");

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
        if (!isInputFromFile) while (isRandom == null) {
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

        if (isInputFromFile || !isRandom) {
            if (!isInputFromFile)
                System.out.printf("Введите %d строк по %d чисел, разделенных пробелом, где последнее число в строке - свободный член\n", n, n + 1);
            for (int i = 0; i < n; i++) {
                if (!isInputFromFile) System.out.printf("Строка %d: ", i + 1);
                try {
                    ArrayList<Double> row = new ArrayList<>();
                    String line = scanner.nextLine();
                    String[] numbers = line.split(" ");
                    if (numbers.length != n + 1) throw new Exception("Неверное количество чисел");
                    for (int j = 0; j < n + 1; j++) {
                        double elem = Double.parseDouble(numbers[j].replace(",", "."));
                        row.add(elem);
                    }
                    arr_matrix.add(row);
                } catch (NoSuchElementException ignored) {
                    if (isInputFromFile) System.out.println("Неверный формат файла");
                    System.exit(-1);
                } catch (Exception e) {
                    if (isInputFromFile) {
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
                arr_matrix.add(row);
            }
        }
        return arr_matrix;
    }

    private static void showSelectInputMessage() {
        System.out.println("Выберите метод ввода данных:");
        System.out.println("0 - с клавиатуры");
        System.out.println("1 - из файла");
    }
}