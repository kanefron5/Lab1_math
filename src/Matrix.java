import java.util.ArrayList;
import java.util.HashMap;

public class Matrix {

    private ArrayList<ArrayList<Double>> matrix_start;
    private ArrayList<ArrayList<Double>> matrix;

    public Matrix(ArrayList<ArrayList<Double>> matrix) {
        this.matrix = matrix;
        this.matrix_start = new ArrayList<>(matrix);
    }

    public double getDeterminant() throws Exception {
        double res = 1;
        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Double> doubles = matrix.get(i);
            Double aDouble = doubles.get(i);
            if (aDouble.isNaN() || aDouble.isInfinite() || aDouble.equals(0.0)) {
                throw new Exception("Данная матрица не имеет решений, либо имеет бесконечно много решений");
            }
            res *= aDouble;
        }
        return res;
    }

    public ArrayList<Double> getResidual() {
        ArrayList<Double> acc_list = new ArrayList<>();

        for (ArrayList<Double> doubles : matrix_start) {
            double sum = 0.0;
            ArrayList<Double> vars = getVars();
            for (int i = 0; i < doubles.size() - 1; i++)
//                sum += round(doubles.get(i) * vars.get(i));
                sum += doubles.get(i) * vars.get(i);
//            acc_list.add(round(Double.sum(-sum, round(doubles.get(doubles.size() - 1)))));
            acc_list.add(Double.sum(-sum, doubles.get(doubles.size() - 1)));
        }
        return acc_list;
    }

    public ArrayList<Double> getVars() {
        HashMap<Integer, Double> vars = new HashMap<>();
        for (int i = matrix.size() - 1; i >= 0; i--) {
            ArrayList<Double> doubles = matrix.get(i);
            double free_elem = doubles.get(doubles.size() - 1);
            double now_var = doubles.get(i);
            vars.put(i, round((free_elem - getArrSum(vars, doubles)) / now_var));
        }
        return new ArrayList<>(vars.values());
    }


    private double round(double num) {
        double accuracy = 1_0000d;
//        return num;
        return (double) Math.round(num * accuracy) / accuracy;
    }

    private double getArrSum(HashMap<Integer, Double> vars, ArrayList<Double> ks) {
        double sum = 0;
        for (int i = 0; i < ks.size(); i++) {
            double var = 0;
            if (vars.containsKey(i)) var = vars.get(i) * ks.get(i);
            sum += var;
        }
        return sum;
    }

    private void selectMainElement(int row) {
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
 //A[i][j]1 = A[i][j]0 - A[i][1]0 / A[1][1]0 * A[1][j]0
    public void gauss() {
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (ArrayList<Double> doubles : matrix) {
            for (int i = 0; i < doubles.size(); i++) {
                Double aDouble = doubles.get(i);
                if (i == matrix.size()) builder.append(String.format("\t│%10.4f\n", aDouble));
                else builder.append(String.format("%10.4f", aDouble));
            }
        }

        return builder.toString();
    }

}
