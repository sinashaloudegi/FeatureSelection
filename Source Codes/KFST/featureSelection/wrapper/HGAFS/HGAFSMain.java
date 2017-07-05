package KFST.featureSelection.wrapper.HGAFS;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.WrapperApproach;

/**
 * Created by sina on 7/5/2017.
 */
public class HGAFSMain implements WrapperApproach {
    @Override
    public void loadDataSet(DatasetInfo ob) {

    }

    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {

    }

    @Override
    public void evaluateFeatures() {

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
