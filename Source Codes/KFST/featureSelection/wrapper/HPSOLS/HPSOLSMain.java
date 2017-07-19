package KFST.featureSelection.wrapper.HPSOLS;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.HGAFS.LocalSearchOperation;
import KFST.featureSelection.wrapper.WrapperApproach;
import weka.core.Instances;

import java.io.IOException;

/**
 * Created by sina on 7/13/2017.
 */
public class HPSOLSMain implements WrapperApproach {

    HPSOLSSwarm hpsolsswarm;
    int numIterates;
    int numFeatures;
    int numSwarmPopulation;
    int numSelectedFeatures;
    String pathData;
    String pathTestData;
    HPSOLSFitCalculator hpsolsFitCalculator;
    LocalSearchOperation localSearchOperation;
    Instances data;

    public HPSOLSMain() {

    }

    public HPSOLSMain(int numSelectedFeatures, int numItertion, int numSwarmPopulation) {
        this.numIterates = numItertion;
        this.numSwarmPopulation = numSwarmPopulation;
        this.numSelectedFeatures = numSelectedFeatures;
    }

    private void run() throws Exception {

        for (int i = 0; i < numIterates; i++) {
            hpsolsswarm.calculateFitness();
            hpsolsswarm.update();

        }
    }

    private void init() throws IOException {
        hpsolsFitCalculator = new HPSOLSFitCalculator(pathData, pathTestData);
        data = hpsolsFitCalculator.getTrain();
        localSearchOperation = new LocalSearchOperation(data, 0.6, numSelectedFeatures);
        localSearchOperation.computeCorrelation();
        hpsolsswarm = new HPSOLSSwarm(numFeatures, numSwarmPopulation, hpsolsFitCalculator, numSelectedFeatures, localSearchOperation);
        hpsolsswarm.initialize();

    }

    @Override
    public void loadDataSet(DatasetInfo ob) {
        numFeatures = ob.getNumFeature();
        pathData = ob.getPathData();
        pathTestData = ob.getPathTestSet();
    }

    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {

    }

    @Override
    public void evaluateFeatures() throws Exception {
        init();
        run();
    }

    @Override
    public int[] getSelectedFeatureSubset() {
        return toIntArray(hpsolsswarm.gb);
    }

    public int[] toIntArray(int[] gb) {
        int numOfOnes = numOfOnes(gb);
        int temp[] = new int[numOfOnes];
        int counter = 0;
        for (int i = 0; i < gb.length; i++) {
            if (gb[i] == 1) {
                temp[counter] = i;
                counter++;
            }

        }
        return temp;
    }

    private int numOfOnes(int[] gb) {
        int counter = 0;
        for (int i = 0; i < gb.length; i++) {
            if (gb[i] == 1) {
                counter++;
            }

        }
        return counter;
    }

    @Override
    public double[] getValues() {
        return new double[0];
    }
}
