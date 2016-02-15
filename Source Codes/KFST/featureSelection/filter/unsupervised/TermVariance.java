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
 * This java class is used to implement the term variance method.
 *
 * @author Sina Tabakhi
 */
public class TermVariance implements FilterApproach {

    private double[][] trainSet;
    private int numFeatures;
    private int[] selectedFeatureSubset;
    private int numSelectedFeature;
    private double[] termVarianceValues;

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     */
    public TermVariance(int sizeSelectedFeatureSubset) {
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
     * starts the feature selection process by term variance(TV) method
     */
    @Override
    public void evaluateFeatures() {
        double[] meanValues = new double[numFeatures];
        termVarianceValues = new double[numFeatures];
        int[] indecesTV;

        //computes the mean values of each feature
        for (int i = 0; i < numFeatures; i++) {
            meanValues[i] = MathFunc.computeMean(trainSet, i);
        }

        //computes the variance values of each feature
        for (int i = 0; i < numFeatures; i++) {
            termVarianceValues[i] = MathFunc.computeVariance(trainSet, meanValues[i], i);
        }

        indecesTV = ArraysFunc.sortWithIndex(Arrays.copyOf(termVarianceValues, termVarianceValues.length), true);
//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println(i + ") =  " + termVarianceValues[i]);
//        }

        selectedFeatureSubset = Arrays.copyOfRange(indecesTV, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }

    /**
     * This method return the subset of selected features by term variance(TV)
     * method.
     *
     * @return an array of subset of selected features
     */
    @Override
    public int[] getSelectedFeatureSubset() {
        return selectedFeatureSubset;
    }

    /**
     * This method return the term variance values of each feature
     *
     * @return an array of term variance values
     */
    @Override
    public double[] getValues() {
        return termVarianceValues;
    }
}
