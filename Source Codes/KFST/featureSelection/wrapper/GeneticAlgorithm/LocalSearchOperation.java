package KFST.featureSelection.wrapper.GeneticAlgorithm;

import weka.core.Instances;

/**
 * Created by sina on 7/1/2017.
 */
public class LocalSearchOperation {

    Instances data;
    double arrayData[][];
    int[] S;
    int[] D;
    int[][] C;
    double means[];

    public LocalSearchOperation(Instances data) {
        this.data = data;
        convertToArray();
        means = new double[data.numInstances()];
        calcMean();
    }

    private void computeCorrelation() {

    }

    private void calcMean() {
        int sum = 0;
        for (int i = 0; i < data.numInstances(); i++) {
            for (int j = 0; j < data.numAttributes(); j++) {
                sum += arrayData[j][i];
            }
            sum /= data.numInstances();
            means[i] = sum;
        }

    }


    private void convertToArray() {
        for (int i = 0; i < data.numInstances(); i++) {
            for (int j = 0; j < data.numAttributes(); j++) {
                arrayData[i][j] = data.instance(i).value(j);
            }
        }
    }

}
