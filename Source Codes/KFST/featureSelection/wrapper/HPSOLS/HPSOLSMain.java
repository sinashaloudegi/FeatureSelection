package KFST.featureSelection.wrapper.HPSOLS;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.HGAFS.LocalSearchOperation;
import KFST.featureSelection.wrapper.WrapperApproach;
import weka.core.Instances;

import java.io.IOException;
import java.util.Random;

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

    private int subsetSizeDeterminatuionScheme() {
        double epsilon = 0.5; // 0.15 - 0.7
        int sy = (int) ((numFeatures) * epsilon);
        System.out.println("sy : " + sy);
        double[] Lk = new double[sy - 2];
        for (int i = 0; i < Lk.length; i++) {
            Lk[i] = calculateLk(i);
        }
        return randomSelection(Lk);
    }

    private int randomSelection(double[] lk) {
        for (int i = 0; i < lk.length; i++) {
            System.out.println("LK");
            System.out.print(lk[i]+",");
        }
        Random random = new Random();
        double u = random.nextDouble();
        int k = 0;
        int sum = 0;
        for (int i = 3; i < lk.length; i++) {
            sum += lk[i];
            if (u <= sum) {
                k = i;
                break;
            }
        }
        return k;
    }

    private double calculateLk(int k) {
        double nominator = (numFeatures - k);
        double denominator = calcDenominator(nominator);
        System.out.println("deNominiator : " + nominator / denominator);

        return nominator / denominator;
    }

    private double calcDenominator(double len) {
        int sum = 0;
        for (int i = 1; i <= len; i++) {
            sum += (numFeatures - i);
        }
        return sum;
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
        localSearchOperation = new LocalSearchOperation(data, 0.66, numSelectedFeatures);
        localSearchOperation.computeCorrelation();
     //   this.numSelectedFeatures = subsetSizeDeterminatuionScheme();
        System.out.println("NUmber of features selected " + numSelectedFeatures);
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
