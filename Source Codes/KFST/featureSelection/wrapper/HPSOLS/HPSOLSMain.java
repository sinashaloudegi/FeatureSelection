package KFST.featureSelection.wrapper.HPSOLS;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.PSO.Swarm;
import KFST.featureSelection.wrapper.WrapperApproach;

/**
 * Created by sina on 7/13/2017.
 */
public class HPSOLSMain implements WrapperApproach {
    Swarm swarm;
    int numIterates;
    int numFeatures;
    int numSwarmPopulation;
    int numSelectedFeatures;
    String pathData;
    String pathTestData;

    public HPSOLSMain(int numSelectedFeatures, int numSwarmPopulation, int numIterates) {
        this.numIterates = numIterates;
        this.numSwarmPopulation = numSwarmPopulation;
        this.numSelectedFeatures = numSelectedFeatures;
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

    }

    @Override
    public int[] getSelectedFeatureSubset() {
        return new int[0];
    }

    @Override
    public double[] getValues() {
        return new double[0];
    }
}
