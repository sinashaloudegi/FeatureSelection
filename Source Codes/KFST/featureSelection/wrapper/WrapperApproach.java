package KFST.featureSelection.wrapper;

import KFST.dataset.DatasetInfo;

/**
 * Created by sina on 6/26/2017.
 */
public interface WrapperApproach {

    /**
     * loads the dataset
     *
     * @param ob an object of the DatasetInfo class
     */
    public void loadDataSet(DatasetInfo ob);

    /**
     * loads the dataset
     *
     * @param data the input dataset values
     * @param numFeat the number of features in the dataset
     * @param numClasses the number of classes in the dataset
     */
    public void loadDataSet(double[][] data, int numFeat, int numClasses);

    /**
     * starts the feature selection process by a given method
     */
    public void evaluateFeatures();

    /**
     * return the subset of features selected by a given method.
     *
     * @return an array of subset of selected features
     */
    public int[] getSelectedFeatureSubset();

    /**
     * return the weights of features if the method gives weights of features
     * individually and ranks them based on their relevance (i.e., feature
     * weighting methods); otherwise, these values does not exist.
     *
     * @return an array of  weights of features
     */
    public double[] getValues();

}
