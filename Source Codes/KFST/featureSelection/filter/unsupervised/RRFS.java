/*
 * Kurdistan Feature Selection Tool (KFST) is an open-source tool, developed
 * completely in Java, for performing feature selection process in different
 * areas of research.
 * For more information about KFST, please visit:
 *     http://kfst.uok.ac.ir/index.html
 *
 * Copyright (C) 2016 KFST development team at University of Kurdistan,
 * Sanandaj, Iran.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package KFST.featureSelection.filter.unsupervised;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.filter.FilterApproach;
import KFST.util.ArraysFunc;
import KFST.util.MathFunc;
import java.util.Arrays;

/**
 * This java class is used to implement the relevance-redundancy feature
 * selection(RRFS) method. Also, it is the unsupervised version of the RRFS.
 *
 * @author Sina Tabakhi
 */
public class RRFS implements FilterApproach {

    private double[][] trainSet;
    private int numFeatures;
    private int[] selectedFeatureSubset;
    private int numSelectedFeature;
    private double maxSimValue; //maximum allowed similarity between two features
    private boolean status; //check the status of size of the selected feature

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     * @param maxSimilarity maximum allowed similarity between two features
     */
    public RRFS(int sizeSelectedFeatureSubset, double maxSimilarity) {
        numSelectedFeature = sizeSelectedFeatureSubset;
        selectedFeatureSubset = new int[numSelectedFeature];
        maxSimValue = maxSimilarity;
        status = true;
    }

    /**
     * loads the dataset
     *
     * @param ob an object of the DatasetInfo class
     */
    @Override
    public void loadDataSet(DatasetInfo ob) {
        trainSet = ob.getTrainSet();
        numFeatures = ob.getNumFeature();
    }

    /**
     * loads the dataset
     *
     * @param data the input dataset values
     * @param numFeat the number of features in the dataset
     * @param numClasses the number of classes in the dataset
     */
    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {
        trainSet = ArraysFunc.copyDoubleArray2D(data);
        numFeatures = numFeat;
    }

    /**
     * computes the mean absolute difference(MAD) values of the features
     *
     * @param indexFeature index of the given feature
     * @param mean the mean value of the given feature (indexFeature)
     * 
     * @return the mean absolute difference
     */
    private double computeMAD(int indexFeature, double mean) {
        double sum = 0;
        for (int i = 0; i < trainSet.length; i++) {
            sum += Math.abs(trainSet[i][indexFeature] - mean);
        }
        return sum;
    }

    /**
     * starts the feature selection process by relevance-redundancy
     * feature selection(RRFS) method
     */
    @Override
    public void evaluateFeatures() {
        double[] MADValues = new double[numFeatures]; // the mean absolute difference (MAD) values
        int[] indexFeatures = new int[numFeatures];
        int prev, next;

        //computes the mean absolute difference(MAD) values
        for (int i = 0; i < numFeatures; i++) {
            MADValues[i] = computeMAD(i, MathFunc.computeMean(trainSet, i));
        }

        //sorts the features by their relevance values(MAD values)
        indexFeatures = ArraysFunc.sortWithIndex(MADValues, true);

        //starts the feature selection process
        selectedFeatureSubset[0] = indexFeatures[0];
        prev = 0;
        next = 1;
        for (int i = 1; i < numFeatures && next < numSelectedFeature; i++) {
            double simValue = Math.abs(MathFunc.computeSimilarity(trainSet, indexFeatures[i], indexFeatures[prev]));
            if (simValue < maxSimValue) {
                selectedFeatureSubset[next] = indexFeatures[i];
                prev = i;
                next++;
            }
        }

//        for (int i = 0; i < next; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }

        if (next < numSelectedFeature) {
            selectedFeatureSubset = Arrays.copyOfRange(selectedFeatureSubset, 0, next);
            status = false;
        }
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
    }

    /**
     * This method return the subset of selected features by
     * relevance-redundancy feature selection(RRFS) method
     *
     * @return an array of subset of selected features
     */
    @Override
    public int[] getSelectedFeatureSubset() {
        return selectedFeatureSubset;
    }

    /**
     * return the weights of features if the method gives weights of features
     * individually and ranks them based on their relevance (i.e., feature
     * weighting methods); otherwise, these values does not exist.
     * <p>
     * These values does not exist for RRFS.
     *
     * @return an array of  weight of features
     */
    @Override
    public double[] getValues() {
        return null;
    }

    /**
     * gets the status of the size of the selected feature(is equal the size of
     * selected features by RRFS method and the number of selected features
     * by user)
     *
     * @return the status of the size of the selected feature
     */
    public boolean isEqual() {
        return status;
    }
}
