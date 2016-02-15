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
 * This java class is used to implement the mutual correlation method.
 *
 * @author Sina Tabakhi
 */
public class MutualCorrelation implements FilterApproach {

    private double[][] trainSet;
    private int numFeatures;
    private int[] selectedFeatureSubset;
    private int numSelectedFeature;

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     */
    public MutualCorrelation(int sizeSelectedFeatureSubset) {
        numSelectedFeature = sizeSelectedFeatureSubset;
        selectedFeatureSubset = new int[numSelectedFeature];
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
     * computes the mutual correlation value between each pairs of features
     *
     * @param index1 the index of the feature 1
     * @param index2 the index of the feature 2
     * @param mean1 the mean value of the data corresponding to the feature 1
     * @param mean2 the mean value of the data corresponding to the feature 2
     * 
     * @return the mutual correlation value
     */
    private double computeMutualCorrelation(int index1, int index2, double mean1, double mean2) {
        double sum1 = 0;
        double sum2 = 0;
        double sum3 = 0;
        for (int i = 0; i < trainSet.length; i++) {
            sum1 += trainSet[i][index1] * trainSet[i][index2];
            sum2 += trainSet[i][index1] * trainSet[i][index1];
            sum3 += trainSet[i][index2] * trainSet[i][index2];
        }
        sum1 -= trainSet.length * mean1 * mean2;
        sum2 -= trainSet.length * mean1 * mean1;
        sum3 -= trainSet.length * mean2 * mean2;

        if (sum2 == 0 && sum3 == 0) {
            return 1;
        } else if (sum2 == 0 || sum3 == 0) {
            return 0;
        } else {
            return sum1 / Math.sqrt(sum2 * sum3);
        }
    }

    /**
     * finds index in new Data Structure(Symmetric Matrix)
     *
     * @param index1 index of the row
     * @param index2 index of the column
     * 
     * @return the index in new Data Structure
     */
    private static int findIndex(int index1, int index2) {
        if (index1 < index2) {
            return ((index2 * (index2 - 1)) / 2) + index1;
        } else {
            return ((index1 * (index1 - 1)) / 2) + index2;
        }
    }

    /**
     * finds the maximum value in the array and returns its index
     *
     * @param array the input array
     * @param len the length of available values in the array
     * 
     * @return the index of the maximum value in the array
     */
    private int findMaxWithIndex(double[] array, int len) {
        int index = 0;
        double max = array[index];
        for (int i = 1; i <= len; i++) {
            if (array[i] > max) {
                max = array[i];
                index = i;
            }
        }
        return index;
    }

    /**
     * swaps the two entities of the input integer array
     *
     * @param array the input array
     * @param firstIndex index of the first entity in the array
     * @param secondIndex index of the second entity in the array
     */
    private void swapValue(int[] array, int firstIndex, int secondIndex) {
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    /**
     * swaps the two entities of the input double array
     *
     * @param array the input array
     * @param firstIndex index of the first entity in the array
     * @param secondIndex index of the second entity in the array
     */
    private void swapValue(double[] array, int firstIndex, int secondIndex) {
        double temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    /**
     * starts the feature selection process by mutual correlation(MC) method
     */
    @Override
    public void evaluateFeatures() {
        double[] mean = new double[numFeatures];
        double[] correlationValues = new double[(numFeatures * (numFeatures - 1)) / 2]; // mutual correlation values
        double[] meanMutCorrelation = new double[numFeatures]; //the mean absolute mutual correlation values
        int[] indexFeatures = new int[numFeatures];
        int counter = 0;

        //initializes the feature index values
        for (int i = 0; i < indexFeatures.length; i++) {
            indexFeatures[i] = i;
        }

        //computes the mean values of the features
        for (int i = 0; i < numFeatures; i++) {
            mean[i] = MathFunc.computeMean(trainSet, i);
        }

        //computes the mutual correlation values between each pairs of features
        for (int i = 0; i < numFeatures; i++) {
            for (int j = 0; j < i; j++) {
                correlationValues[counter++] = computeMutualCorrelation(i, j, mean[i], mean[j]);
            }
        }

        //computes the mean absolute mutual correlation values for the features
        for (int i = 0; i < numFeatures; i++) {
            for (int j = 0; j < numFeatures; j++) {
                if (i != j) {
                    int index = findIndex(i, j);
                    meanMutCorrelation[i] += Math.abs(correlationValues[index]);
                }
            }
            meanMutCorrelation[i] /= (numFeatures - 1);
        }

        //starts the feature elimination process
        for (int i = numFeatures - 1; i >= numSelectedFeature; i--) {
            int maxIndex = findMaxWithIndex(meanMutCorrelation, i);
            swapValue(meanMutCorrelation, maxIndex, i);
            swapValue(indexFeatures, maxIndex, i);
            for (int j = 0; j < i; j++) {
                int index = findIndex(indexFeatures[j], indexFeatures[i]);
                meanMutCorrelation[j] = (i * meanMutCorrelation[j] - Math.abs(correlationValues[index])) / (i - 1);
            }
        }

        selectedFeatureSubset = Arrays.copyOfRange(indexFeatures, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }

    /**
     * This method return the subset of selected features by mutual
     * correlation(MC) method.
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
     * These values does not exist for mutual correlation(MC).
     *
     * @return an array of  weight of features
     */
    @Override
    public double[] getValues() {
        return null;
    }
}
