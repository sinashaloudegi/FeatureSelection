package KFST.featureSelection.wrapper.GeneticAlgorithm;

import weka.core.Instances;

import java.util.Arrays;

/**
 * Created by sina on 7/1/2017.
 */
public class LocalSearchOperation {

    Instances data;
    double arrayData[][];
    int[] S;
    int[] D;
    double[][] C;

    double means[];
    double Cor[][];

    public LocalSearchOperation(Instances data) {
        this.data = data;
        convertToArray();
        means = new double[data.numAttributes() - 1];
        calcMean();

    }

    public void computeCorrelation() {
        computeC();
        Cor = new double[data.numAttributes()][2];
        for (int i = 0; i < data.numAttributes(); i++) {
            double sum = 0;
            for (int j = 0; j < data.numAttributes(); j++) {
                sum += Math.abs(C[i][j]);
            }
            Cor[i][0] = sum / (data.numAttributes() - 1);
            Cor[i][1] = i;
        }


        System.out.println(Arrays.deepToString(Cor));
        Arrays.sort(Cor, (double[] a, double[] b) -> Double.compare(a[0], b[0]));
        System.out.println("Sorted");
        System.out.println(Arrays.deepToString(Cor));

        D = new int[data.numAttributes() / 2];
        S = new int[data.numAttributes() / 2];
        for (int i = 0; i < data.numAttributes() / 2; i++) {
            D[i] = (int) Cor[i][0];
        }
        int k = 0;
        for (int i = data.numAttributes() / 2; i < data.numAttributes(); i++) {
            S[k] = (int) (Cor[i][0]);
            k++;
        }
        System.out.println("s");
        for (int i = 0; i < S.length; i++) {
            System.out.print(S[i] + ",");
        }
        System.out.println();
        System.out.println("d");
        for (int i = 0; i < D.length; i++) {
            System.out.print(D[i] + ",");
        }
    }


    private void computeC() {
        C = new double[data.numAttributes()][data.numAttributes()];
        for (int i = 0; i < data.numAttributes() - 1; i++) {
            for (int j = 0; j < data.numAttributes() - 1; j++) {
                double sum = 0;
                double xi = 0;
                double xj = 0;

                for (int k = 0; k < data.numInstances(); k++) {
                    xi = (arrayData[k][i] - means[i]);
                    xj = (arrayData[k][j] - means[j]);
                    sum += xi * xj;
                }
                xj = 0;
                xi = 0;
                for (int k = 0; k < data.numInstances(); k++) {
                    xi += Math.pow((arrayData[k][i] - means[i]), 2);
                    xj += Math.pow((arrayData[k][j] - means[j]), 2);
                }
                double denominator = Math.sqrt(xi) * Math.sqrt(xj);
                C[i][j] = sum / denominator;

            }

        }

    }


    private void calcMean() {
        double sum = 0;
        for (int i = 0; i < data.numAttributes() - 1; i++) {
            for (int j = 0; j < data.numInstances(); j++) {
                sum += arrayData[j][i];
            }
            sum /= data.numInstances();
            means[i] = sum;
        }


    }


    private void convertToArray() {
        arrayData = new double[data.numInstances()][data.numAttributes()];
        for (int i = 0; i < data.numInstances(); i++) {
            for (int j = 0; j < data.numAttributes(); j++) {
                arrayData[i][j] = data.instance(i).value(j);
            }
        }
    }

}
