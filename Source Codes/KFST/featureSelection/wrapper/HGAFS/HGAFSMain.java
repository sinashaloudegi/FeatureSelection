package KFST.featureSelection.wrapper.HGAFS;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.GeneticAlgorithm.FitnessCalculator;
import KFST.featureSelection.wrapper.WrapperApproach;
import KFST.util.ArraysFunc;

/**
 * Created by sina on 7/5/2017.
 */
public class HGAFSMain implements WrapperApproach {


    private double[][] trainSet;
    private int numFeatures;
    private int numClass;
    private int[] selectedFeatureSubset;
    Strings classifier;
    Population p;

    int numSelectedFeatures;
    double pCrossover;
    double pMutation;
    int numPopulation;
    double miu;

    String pathData;
    String pathTestData;
    FitnessCalculator fitnessCalculator;

    public HGAFSMain(int numSelectedFeatures, int numPopulation, double pCrossover, double pMutation, double miu) throws Exception {
        this.numSelectedFeatures = numSelectedFeatures;
        this.numPopulation = numPopulation;
        this.pCrossover = pCrossover;
        this.pMutation = pMutation;
        this.miu = miu;
    }

    private void init() {
        Population p = new Population(numPopulation, numFeatures,numSelectedFeatures);
        p.initPopulation();
    }

    @Override
    public void loadDataSet(DatasetInfo ob) {
        trainSet = ob.getTrainSet();
        numFeatures = ob.getNumFeature();
        numClass = ob.getNumClass();
        pathData = ob.getPathData();
        pathTestData = ob.getPathTestSet();
    }

    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {
        trainSet = ArraysFunc.copyDoubleArray2D(data);
        numFeatures = numFeat;
        numClass = numClasses;
    }

    @Override
    public void evaluateFeatures() {

    }

    @Override
    public int[] getSelectedFeatureSubset() {
        return selectedFeatureSubset;
    }

    @Override
    public double[] getValues() {
        return null;
    }
}
