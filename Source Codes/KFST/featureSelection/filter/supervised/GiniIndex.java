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
import java.util.Arrays;

/**
 * This java class is used to implement the Gini index method.
 *
 * @author Sina Tabakhi
 */
public class GiniIndex implements FilterApproach {

    private double[][] trainSet;
    private int numFeatures;
    private int numClass;
    private int[] selectedFeatureSubset;
    private int numSelectedFeature;
    private double[] giniIndexValues;

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     */
    public GiniIndex(int sizeSelectedFeatureSubset) {
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
        numClass = ob.getNumClass();
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
        numClass = numClasses;
    }

    /**
     * computes the gini value of the data given by start and end indices
     *
     * @param indexStart the start index of the dataset
     * @param indexEnd the end index of the dataset
     * 
     * @return the gini value
     */
    private double computeGini(int indexStart, int indexEnd) {
        double gini = 0;
        int sizeUsedData = indexEnd - indexStart;
        int[] countClassSample = new int[numClass];

        //counts the number of samples in each class
        for (int i = indexStart; i < indexEnd; i++) {
            countClassSample[(int) trainSet[i][numFeatures]]++;
        }

        //computes the gini value of the data given by start and end indeces
        for (int i = 0; i < numClass; i++) {
            if (countClassSample[i] != 0) {
                double prob = countClassSample[i] / (double) sizeUsedData;
                gini += Math.pow(prob, 2);
            }
        }

        return 1 - gini;
    }

    /**
     * starts the feature selection process by Gini index(GI) method
     */
    @Override
    public void evaluateFeatures() {
        double giniSystem = computeGini(0, trainSet.length); // computes the gini value of the system (over all dataset)
        giniIndexValues = new double[numFeatures];
        int[] indecesGI;

        //computes the Gini index values of each feature
        for (int i = 0; i < numFeatures; i++) {
            double giniFeature = 0;
            ArraysFunc.sortArray2D(trainSet, i); // sorts the dataset values corresponding to a given feature(feature i)
            int indexStart = 0;
            double startValue = trainSet[indexStart][i];
            for (int j = 1; j < trainSet.length; j++) {
                if (startValue != trainSet[j][i]) {
                    double prob = (j - indexStart) / (double) trainSet.length;
                    giniFeature += prob * computeGini(indexStart, j);
                    indexStart = j;
                    startValue = trainSet[indexStart][i];
                }
            }
            double prob = (trainSet.length - indexStart) / (double) trainSet.length;
            giniFeature += prob * computeGini(indexStart, trainSet.length);
            giniIndexValues[i] = giniSystem - giniFeature;
        }

        indecesGI = ArraysFunc.sortWithIndex(Arrays.copyOf(giniIndexValues, giniIndexValues.length), true);
//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println(i + ") =  " + giniIndexValues[i]);
//        }

        selectedFeatureSubset = Arrays.copyOfRange(indecesGI, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }

    /**
     * This method return the subset of selected features by Gini index (GI)
     * method
     *
     * @return an array of subset of selected features
     */
    @Override
    public int[] getSelectedFeatureSubset() {
        return selectedFeatureSubset;
    }

    /**
     * This method return the Gini index values of each feature
     *
     * @return an array of Gini index values
     */
    @Override
    public double[] getValues() {
        return giniIndexValues;
    }
}
