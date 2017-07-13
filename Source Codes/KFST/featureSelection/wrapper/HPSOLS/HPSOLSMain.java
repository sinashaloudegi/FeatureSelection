package KFST.featureSelection.wrapper.HPSOLS;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.WrapperApproach;

/**
 * Created by sina on 7/13/2017.
 */
public class HPSOLSMain implements WrapperApproach {
    public HPSOLSMain(int numSelectedSubset, int numSwarmPopulation, int numIterates) {

    }

    @Override
    public void loadDataSet(DatasetInfo ob) {

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
