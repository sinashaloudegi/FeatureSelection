package KFST.featureSelection.wrapper.BPSO;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.WrapperApproach;

import java.io.IOException;
import java.util.Random;

/**
 * Created by sina on 7/30/2017.
 */
public class BPSOMain  implements WrapperApproach {
    BPSOSwarm swarm;
    int numIterates;
    int numFeatures;
    int numSwarmPopulation;
    int numSelectedFeatures;
    String pathData;
    String pathTestData;
    BPSOFitCalculator bpsoFitCalculator;
    int num=0;

    public BPSOMain(int numSelectedFeatures, int numItertion, int numSwarmPopulation) {
        this.numIterates = numItertion;
        this.numSwarmPopulation = numSwarmPopulation;
        this.numSelectedFeatures = numSelectedFeatures;

    }

    private void run() throws Exception {
        for (int i = 0; i < numIterates; i++) {
            swarm.calculateFitness();

            if(swarm.update()){
                num=0;
            }else {
               num++;
            }

            swarm.uniformCombination(num);

        }
    }

    private void init() throws IOException {
        bpsoFitCalculator = new BPSOFitCalculator(pathData, pathTestData);
        swarm = new BPSOSwarm(numFeatures, numSwarmPopulation, bpsoFitCalculator, numSelectedFeatures);
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
        return  toIntArray(swarm.gb);
    }

    private int[] toIntArray(double[] gb) {
        int[] gbint = new int[numFeatures];
        Random rand = new Random();
        for (int i=0;i<numFeatures;i++) {
            double r = rand.nextDouble();

            if (gb[i] >= r) {
                gbint[i] = 1;
            } else {
                gbint[i] = 0;
            }
        }
        int numOfOnes = numOfOnes(gbint);
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
