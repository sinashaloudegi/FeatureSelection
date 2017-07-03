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

    int numAttributes;
    public LocalSearchOperation(Instances data) {
        this.data = data;
        numAttributes=data.numAttributes()-1;
        convertToArray();
        means = new double[numAttributes];
        calcMean();

    }

    public void computeCorrelation() {
        computeC();
        Cor = new double[numAttributes][2];
        for (int i = 0; i <numAttributes; i++) {
            double sum = 0;
            for (int j = 0; j < numAttributes; j++) {
                sum += Math.abs(C[i][j]);
            }
            Cor[i][0] = sum / (numAttributes);
            Cor[i][1] = i;
        }


        Arrays.sort(Cor, (double[] a, double[] b) -> Double.compare(a[0], b[0]));
        System.out.println("Sorted");

        D = new int[numAttributes / 2];
        S = new int[numAttributes / 2];
        for (int i = 0; i < numAttributes / 2; i++) {
            D[i] = (int) Cor[i][1];
        }
        int k = 0;
        for (int i =(numAttributes+1) / 2; i < numAttributes; i++) {
            S[k] = (int) (Cor[i][1]);
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
        C = new double[numAttributes][numAttributes];
        for (int i = 0; i <numAttributes; i++) {
            for (int j = 0; j < numAttributes; j++) {
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
        for (int i = 0; i < numAttributes; i++) {
            for (int j = 0; j < data.numInstances(); j++) {
                sum += arrayData[j][i];
            }
            sum /= data.numInstances();
            means[i] = sum;
        }


    }

    private void convertToArray() {
        arrayData = new double[data.numInstances()][numAttributes];
        for (int i = 0; i < data.numInstances(); i++) {
            for (int j = 0; j < numAttributes; j++) {
                arrayData[i][j] = data.instance(i).value(j);
            }
        }
    }

    public int[] getS() {
        return S;
    }

    public int[] getD() {
        return D;
    }
}
