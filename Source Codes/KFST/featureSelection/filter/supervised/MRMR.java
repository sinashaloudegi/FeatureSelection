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
package KFST.featureSelection.filter.supervised;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.filter.FilterApproach;
import KFST.util.ArraysFunc;
import KFST.util.MathFunc;

/**
 * This java class is used to implement the minimal redundancy maximal
 * relevance (mRMR) method.
 *
 * @author Sina Tabakhi
 */
public class MRMR implements FilterApproach {

    private double[][] trainSet;
    private int numFeatures;
    private int[] selectedFeatureSubset;
    private int numSelectedFeature;
    private double[][] probFeature;
    private double[][] valuesFeature;
//    private double errorDenominator = 0.0001;

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     */
    public MRMR(int sizeSelectedFeatureSubset) {
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
     * computes the number of different values of each feature
     *
     * @param index the index of the feature
     *
     * @return the number of different values
     */
    private int computeNumValue(int index) {
        int count = 0;

        for (int i = 1; i < trainSet.length; i++) {
            if (trainSet[i][index] != trainSet[i - 1][index]) {
                count++;
            }
        }

        return count + 1;
    }

    /**
     * saves the different values of each feature
     *
     * @param index the index of the feature
     */
    private void computeDifferentValue(int index) {
        int count = 0;

        for (int i = 1; i < trainSet.length; i++) {
            if (trainSet[i][index] != trainSet[i - 1][index]) {
                valuesFeature[index][count++] = trainSet[i - 1][index];
            }
        }
        valuesFeature[index][count] = trainSet[trainSet.length - 1][index];
    }

    /**
     * computes the probabilities values of each feature
     *
     * @param index the index of the feature
     */
    private void computeProbFeat(int index) {
        int count = 0;
        int indexStart = 0;

        for (int i = 1; i < trainSet.length; i++) {
            if (trainSet[i][index] != trainSet[i - 1][index]) {
                probFeature[index][count++] = (i - indexStart) / (double) trainSet.length; // probability of the feature based on its given value
//                if (probFeature[index][count - 1] == 0) {
//                    probFeature[index][count - 1] = errorDenominator;
//                }
                indexStart = i;
            }
        }
        probFeature[index][count] = (trainSet.length - indexStart) / (double) trainSet.length; // probability of the feature based on its given value
//        if (probFeature[index][count] == 0) {
//            probFeature[index][count] = errorDenominator;
//        }
    }

    /**
     * computes the joint probabilities values between two features
     *
     * @param indexFeat2 the index of the second feature
     * @param indexStartData the start index of the dataset
     * @param indexEndData the end index of the dataset
     *
     * @return an array of the joint probabilities values
     */
    private double[] computeJointProb(int indexFeat2, int indexStartData, int indexEndData) {
        double[] jointProbValue = new double[probFeature[indexFeat2].length];
        ArraysFunc.sortArray2D(trainSet, indexFeat2, indexStartData, indexEndData); //sorts the dataset values corresponding to a given feature(feature indexFeat2)
        int indexStart = indexStartData;
        int j = -1;

        for (int i = indexStartData + 1; i < indexEndData; i++) {
            if (trainSet[i][indexFeat2] != trainSet[i - 1][indexFeat2]) {
                for (j = j + 1; j < valuesFeature[indexFeat2].length; j++) {
                    if (valuesFeature[indexFeat2][j] == trainSet[i - 1][indexFeat2]) {
                        jointProbValue[j] = (i - indexStart) / (double) trainSet.length; //probability of the feature based on its given value
                        break;
                    }
                }
                indexStart = i;
            }
        }

        for (j = j + 1; j < valuesFeature[indexFeat2].length; j++) {
            if (valuesFeature[indexFeat2][j] == trainSet[indexEndData - 1][indexFeat2]) {
                jointProbValue[j] = (indexEndData - indexStart) / (double) trainSet.length; //probability of the feature based on its given value
                break;
            }
        }

        return jointProbValue;
    }

    /**
     * computes the mutual information values between two features
     *
     * @param index1 the index of the first feature
     * @param index2 the index of the second feature
     *
     * @return the mutual information value
     */
    private double computeMutualInfo(int index1, int index2) {
        double mutualInfoValue = 0;
        ArraysFunc.sortArray2D(trainSet, index1); //sorts the dataset values corresponding to a given feature(feature index1)
        int indexStart = 0;

        for (int i = 1; i < trainSet.length; i++) {
            if (trainSet[i][index1] != trainSet[i - 1][index1]) {
                double probFeat1 = (i - indexStart) / (double) trainSet.length; //probability of the feature based on its given value
                double[] jointProb = computeJointProb(index2, indexStart, i); //joint probabilitis values between feature index1 and index2

                //update mutual information value of the given feature
                for (int j = 0; j < jointProb.length; j++) {
                    if (jointProb[j] != 0) {
                        double denominatorValue = probFeat1 * probFeature[index2][j];
                        mutualInfoValue += jointProb[j] * MathFunc.log2(jointProb[j] / denominatorValue);
                    }
                }

                indexStart = i;
            }
        }

        double probFeat1 = (trainSet.length - indexStart) / (double) trainSet.length; //probability of the feature based on its given value
        double[] jointProb = computeJointProb(index2, indexStart, trainSet.length); //joint probabilitis values between feature index1 and index2

        //update mutual information value of the given feature
        for (int j = 0; j < jointProb.length; j++) {
            if (jointProb[j] != 0) {
                double denominatorValue = probFeat1 * probFeature[index2][j];
                mutualInfoValue += jointProb[j] * MathFunc.log2(jointProb[j] / denominatorValue);
            }
        }

        return mutualInfoValue;
    }

    /**
     * finds the maximum value in the array and returns its index
     *
     * @param array the input array
     *
     * @return the index of the maximum value in the array
     */
    private int findMaxWithIndex(double[] array) {
        int index = 0;
        double max = array[index];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                index = i;
            }
        }
        return index;
    }

    /**
     * checks that is the current feature (index) available in the subset of
     * selected feature
     *
     * @param index the index of the feature
     * @param currentSize the current size of the selected features subset
     *
     * @return true if the current feature has been selected
     */
    private boolean isSelectedFeature(int index, int currentSize) {
        for (int i = 0; i < currentSize; i++) {
            if (selectedFeatureSubset[i] == index) {
                return true;
            }
        }
        return false;
    }

    /**
     * starts the feature selection process by minimal redundancy
     * maximal relevance (mRMR) method
     */
    @Override
    public void evaluateFeatures() {
        double[] mutualInfoFeatClass = new double[numFeatures]; //mutual information values between features and class
        probFeature = new double[numFeatures + 1][]; //probabilities values of the features (+ class feature)
        valuesFeature = new double[numFeatures + 1][]; //different values of the features (+ class feature)

        //computes the probabilities values of each feature
        for (int i = 0; i <= numFeatures; i++) {
            ArraysFunc.sortArray2D(trainSet, i); //sorts the dataset values corresponding to a given feature(feature i)
            probFeature[i] = new double[computeNumValue(i)]; //computes the number of different values in feature i
            valuesFeature[i] = new double[probFeature[i].length];
            computeDifferentValue(i); //saves the different values of each feature
            computeProbFeat(i); //computes the probabilities values of each feature
        }

        //computes the mutual information values between features and class
        for (int i = 0; i < numFeatures; i++) {
            mutualInfoFeatClass[i] = computeMutualInfo(i, numFeatures);
        }

        //starts the feature selection process
        selectedFeatureSubset[0] = findMaxWithIndex(mutualInfoFeatClass); //finds a feature with the maximum mutual information value
        for (int i = 1; i < numSelectedFeature; i++) {
            double maxValue = -Double.MAX_VALUE;
            int indexMaxValue = -1;

            //finds the relevant feature from the current features set
            for (int j = 0; j < numFeatures; j++) {
                if (!isSelectedFeature(j, i)) {
                    double result = 0;
                    for (int k = 0; k < i; k++) {
                        result += computeMutualInfo(j, selectedFeatureSubset[k]);
                    }
                    result /= i;
                    result = mutualInfoFeatClass[j] - result;
                    if (result > maxValue) {
                        maxValue = result;
                        indexMaxValue = j;
                    }
                }
            }
            selectedFeatureSubset[i] = indexMaxValue;
        }

        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }

    /**
     * This method return the subset of selected features by minimal redundancy
     * maximal relevance (mRMR) method
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
     * These values does not exist for mRMR.
     *
     * @return an array of  weight of features
     */
    @Override
    public double[] getValues() {
        return null;
    }
}
