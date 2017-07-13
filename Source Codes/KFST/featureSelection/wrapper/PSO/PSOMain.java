package KFST.featureSelection.wrapper.PSO;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.WrapperApproach;

import java.io.IOException;

/**
 * Created by sina on 7/11/2017.
 */
public class PSOMain implements WrapperApproach {

    Swarm swarm;
    int numIterates;
    int numFeatures;
    int numSwarmPopulation;
    int numSelectedFeatures;
    String pathData;
    String pathTestData;
    PSOFitCalculator psoFitCalculator;

    public PSOMain(int numSelectedFeatures, int numItertion, int numSwarmPopulation ) {
        this.numIterates = numItertion;
        this.numSwarmPopulation = numSwarmPopulation;
        this.numSelectedFeatures=numSelectedFeatures;
    }

    private void run() throws Exception {
        for (int i = 0; i < numIterates; i++) {
            swarm.calculateFitness();
            swarm.update();

        }
    }

    private void init() throws IOException {
        psoFitCalculator = new PSOFitCalculator(pathData, pathTestData);
        swarm = new Swarm(numFeatures, numSwarmPopulation, psoFitCalculator,numSelectedFeatures);
        swarm.initialize();
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
        return toIntArray(swarm.gb);
    }

    private int[] toIntArray(int[] gb) {
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
