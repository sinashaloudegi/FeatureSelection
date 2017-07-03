package KFST.featureSelection.wrapper.GeneticAlgorithm;

import weka.core.Instances;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by sina on 7/1/2017.
 */
public class LocalSearchOperation {

    Instances data;
    double arrayData[][];
    double[] S;
    double[] D;
    double[][] C;

    double means[];
    double Cor[][];

    public LocalSearchOperation(Instances data) {
        this.data = data;
        convertToArray();
        means = new double[data.numAttributes()-1];
        calcMean();

    }

    public void computeCorrelation() {
        computeC();
        Cor = new double[2][data.numAttributes()];
        for (int i = 0; i < data.numAttributes(); i++) {
            double sum = 0;
            for (int j = 0; j < data.numAttributes(); j++) {
                sum += C[i][j];
            }
            Cor[0][i] = sum / (data.numAttributes() - 1);
            Cor[1][i] = i;
        }
        Arrays.sort(Cor, Comparator.comparingDouble(a -> a[0]));
        for (int i = 0; i < data.numAttributes() / 2; i++) {
            D[i] = Cor[1][i];
        }
        for (int i = data.numAttributes() / 2; i < data.numAttributes(); i++) {
            S[i] = Cor[1][i];
        }
        System.out.println("s");
        for (int i = 0; i < data.numAttributes(); i++) {
            System.out.print(S[i] + ",");
        }
        System.out.println("d");
        for (int i = 0; i < data.numAttributes(); i++) {
            System.out.print(D[i] + ",");
        }
    }


    private void computeC() {
        C = new double[data.numAttributes()][data.numAttributes()];
        for (int i = 0; i < data.numAttributes(); i++) {
            for (int j = 0; j < data.numAttributes(); j++) {
                double sum = 0;
                double xi = 0;
                double xj = 0;

                for (int k = 0; k < data.numInstances(); k++) {
                    xi = (arrayData[i][k] - means[i]);
                    xj = (arrayData[j][k] - means[j]);
                    sum += xi * xj;
                }
                xj = 0;
                xi = 0;
                for (int k = 0; k < data.numInstances(); k++) {
                    xi += Math.pow((arrayData[i][k] - means[i]), 2);
                    xj += Math.pow((arrayData[j][k] - means[j]), 2);
                }
                double denominator = Math.sqrt(xi) * Math.sqrt(xj);
                C[i][j] = sum / denominator;

            }

        }

    }


    private void calcMean() {
        double sum = 0;
        for (int i = 0; i < data.numAttributes()-1; i++) {
            for (int j = 0; j < data.numInstances(); j++) {
                sum += arrayData[j][i];
            }
            sum /= data.numInstances();
            means[i] = sum;
        }

        System.out.println("Means : ");
        for (int i = 0; i < means.length; i++) {
            System.out.print(means[i] + ", ");

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
