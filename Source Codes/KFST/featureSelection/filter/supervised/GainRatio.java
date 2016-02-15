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
import java.util.Arrays;

/**
 * This java class is used to implement the gain ratio method.
 *
 * @author Sina Tabakhi
 */
public class GainRatio implements FilterApproach {

    private double[][] trainSet;
    private int numFeatures;
    private int numClass;
    private int[] selectedFeatureSubset;
    private int numSelectedFeature;
    private double[] gainRatioValues;
    private double errorDenominator = 0.0001;

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     */
    public GainRatio(int sizeSelectedFeatureSubset) {
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
     * computes the split information values of the data for all features
     *
     * @return an array of the split information values
     */
    private double[] splitInformation() {
        double[] splitInformationValues = new double[numFeatures];

        for (int i = 0; i < numFeatures; i++) {
            ArraysFunc.sortArray2D(trainSet, i); // sorts the dataset values corresponding to a given feature(feature i)
            int indexStart = 0;
            double startValue = trainSet[indexStart][i];
            for (int j = 1; j < trainSet.length; j++) {
                if (startValue != trainSet[j][i]) {
                    double prob = (j - indexStart) / (double) trainSet.length;
                    splitInformationValues[i] -= prob * MathFunc.log2(prob);
                    indexStart = j;
                    startValue = trainSet[indexStart][i];
                }
            }
            double prob = (trainSet.length - indexStart) / (double) trainSet.length;
            splitInformationValues[i] -= prob * MathFunc.log2(prob);
            if (splitInformationValues[i] == 0) {
                splitInformationValues[i] = errorDenominator;
            }
        }

        return splitInformationValues;
    }

    /**
     * starts the feature selection process by gain ratio(GR) method
     */
    @Override
    public void evaluateFeatures() {
        double[] infoGainValues;
        double[] splitInfoValues;
        gainRatioValues = new double[numFeatures];
        int[] indecesGR;

        //computes the information gain values of the data
        InformationGain infoGain = new InformationGain(numFeatures);
        infoGain.loadDataSet(trainSet, numFeatures, numClass);
        infoGain.evaluateFeatures();
        infoGainValues = infoGain.getValues();

        //computes the split information values of the data
        splitInfoValues = splitInformation();

        //computes the gain ratio values
        for (int i = 0; i < numFeatures; i++) {
            gainRatioValues[i] = infoGainValues[i] / splitInfoValues[i];
//            System.out.println(i + ")= " + infoGainValues[i] + " , " + splitInfoValues[i] + " , " + gainRatioValues[i]);
        }

        indecesGR = ArraysFunc.sortWithIndex(Arrays.copyOf(gainRatioValues, gainRatioValues.length), true);
        selectedFeatureSubset = Arrays.copyOfRange(indecesGR, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }

    /**
     * This method return the subset of selected features by gain ratio(GR)
     * method
     *
     * @return an array of subset of selected features
     */
    @Override
    public int[] getSelectedFeatureSubset() {
        return selectedFeatureSubset;
    }

    /**
     * This method return the gain ratio values of each feature
     * 
     * @return an array of gain ratio values
     */
    @Override
    public double[] getValues() {
        return gainRatioValues;
    }
}
