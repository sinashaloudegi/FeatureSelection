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
import java.util.Arrays;
import java.util.Random;

/**
 * This java class is used to implement the random subspace method(RSM) method.
 *
 * @author Sina Tabakhi
 */
public class RSM implements FilterApproach {

    private double[][] trainSet;
    private int numFeatures;
    private int numClass;
    private int[] selectedFeatureSubset;
    private int numSelectedFeature;
    private int numIteration;
    private int sizeSubSpace;
    private int thresholdElimination;
    private String nameMultiApproach;
    private int[] featureScore;

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     * @param numIter the number of iteration in the RSM method
     * @param size the size of the subspace
     * @param threshold the number of selected features in each subspace
     * @param nameApproach the name of the multivariate approach used in the RSM
     */
    public RSM(int sizeSelectedFeatureSubset, int numIter, int size, int threshold, String nameApproach) {
        numSelectedFeature = sizeSelectedFeatureSubset;
        selectedFeatureSubset = new int[numSelectedFeature];
        numIteration = numIter;
        sizeSubSpace = size;
        thresholdElimination = threshold;
        nameMultiApproach = nameApproach;
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
     * permutes the index of features
     *
     * @param indexFeat the array of the index of features
     * @param seed determines the index of the seed
     */
    private void permutation(int[] indexFeat, int seed) {
        Random rand = new Random(seed);
        for (int i = 0; i < indexFeat.length; i++) {
            int index1 = rand.nextInt(indexFeat.length);
            int index2 = rand.nextInt(indexFeat.length);

            //swap the two values of the indexFeat array
            int temp = indexFeat[index1];
            indexFeat[index1] = indexFeat[index2];
            indexFeat[index2] = temp;
        }
    }

    /**
     * selects the top feature with size thresholdElimination by given method
     *
     * @param data the new dataset
     * 
     * @return the top feature with size thresholdElimination
     */
    private int[] multivariateApproach(double[][] data) {
        int[] resultSelectedFeature;
        if (nameMultiApproach.equals("Mutual correlation")) {
            MutualCorrelation mc = new MutualCorrelation(thresholdElimination);
            mc.loadDataSet(data, sizeSubSpace, numClass);
            mc.evaluateFeatures();
            resultSelectedFeature = mc.getSelectedFeatureSubset();
        } else {
            resultSelectedFeature = new int[thresholdElimination];
        }
        return resultSelectedFeature;
    }

    /**
     * creates a new dataset based on the given indeces of the features
     *
     * @param index an array of the indeces of features
     * 
     * @return a new dataset
     */
    private double[][] createNewDataset(int[] index) {
        double[][] newData = new double[trainSet.length][sizeSubSpace + 1];

        for (int i = 0; i < trainSet.length; i++) {
            for (int j = 0; j < sizeSubSpace; j++) {
                newData[i][j] = trainSet[i][index[j]];
            }
            newData[i][sizeSubSpace] = trainSet[i][numFeatures];
        }

        return newData;
    }

    /**
     * starts the feature selection process by random subspace method(RSM)
     */
    @Override
    public void evaluateFeatures() {
        featureScore = new int[numFeatures];
        int[] indexFeatures = new int[numFeatures];
        int[] indecesFeatScore;

        //initializes the feature index values
        for (int i = 0; i < indexFeatures.length; i++) {
            indexFeatures[i] = i;
        }

        for (int i = 0; i < numIteration; i++) {
//            System.out.println("\nIteration " + i + ":\n\n");
            permutation(indexFeatures, i);

            int[] featSpace = Arrays.copyOfRange(indexFeatures, 0, sizeSubSpace);
            ArraysFunc.sortArray1D(featSpace, false);

            //creates a new dataset based on featSpace array
            double[][] newDataset = createNewDataset(featSpace);

            //selects the top feature with size thresholdElimination by given method
            int[] featSelected = multivariateApproach(newDataset);

            //updates the score of the feature selected by mutual correlation
            for (int j = 0; j < thresholdElimination; j++) {
                featureScore[featSpace[featSelected[j]]]++;
            }
        }

        indecesFeatScore = ArraysFunc.sortWithIndex(Arrays.copyOf(featureScore, featureScore.length), true);
//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println(i + ") =  " + featureScore[i]);
//        }

        selectedFeatureSubset = Arrays.copyOfRange(indecesFeatScore, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }

    /**
     * This method return the subset of selected features by random subspace
     * method(RSM) method.
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
     * These values does not exist for RSM.
     *
     * @return an array of  weight of features
     */
    @Override
    public double[] getValues() {
        return null;
    }
}
