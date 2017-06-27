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
import java.util.Random;

/**
 * This java class is used to implement the incremental relevance–redundancy
 * feature selection based on ant colony optimization, version2 (IRRFSACO_2)
 * method.
 *
 * @author Sina Tabakhi
 */
public class    IRRFSACO_2 implements FilterApproach {

    private double[][] trainSet;
    private int numFeatures;
    private int numClass;
    private int[] selectedFeatureSubset;
    private int numSelectedFeature;
    private double initPheromoneValue;
    private int maxIteration;
    private int numAnts;
    private int numFeatOfAnt;
    private double decayRate;
    private double alpha;
    private double beta;
    private double probChooseEquation;
    private int[][] antSubsetSelected;
    private double[] relevanceFeature;
    private double[] simValues;
    private double[] pheromoneValues;
    private int[] featureCounter;
    private boolean[][] tabuList;
    private int[] currentState;
//    private int seedValue = 0;
    private double errorSimilarity = 0.0001;
    private double errorRelevance = 0.0001;
//    private Random randNumber = new Random(seedValue);

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     * @param initPheromone the initial value of the pheromone
     * @param numIterations the maximum number of iteration
     * @param numAnt the number of ants
     * @param numFeatureOfAnt the number of selected features by each ant in each iteration
     * @param evaporationRate the evaporation rate of the pheromone
     * @param alphaParameter the alpha parameter in the state transition rule
     * @param betaParameter the beta parameter in the state transition rule
     * @param q0_Parameter the q0 parameter in the state transition rule
     */
    public IRRFSACO_2(int sizeSelectedFeatureSubset, double initPheromone, int numIterations, int numAnt, int numFeatureOfAnt, double evaporationRate, double alphaParameter, double betaParameter, double q0_Parameter) {
        numSelectedFeature = sizeSelectedFeatureSubset;
        selectedFeatureSubset = new int[numSelectedFeature];
        initPheromoneValue = initPheromone;
        maxIteration = numIterations;
        numAnts = numAnt;
        numFeatOfAnt = numFeatureOfAnt;
        decayRate = evaporationRate;
        alpha = alphaParameter;
        beta = betaParameter;
        probChooseEquation = q0_Parameter;
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
     * computes the relevance of each feature using the normalized term variance
     * (the relevance values normalized by softmax scaling function)
     */
    private void computeRelevance() {
        double mean = 0;
        double variance = 0;
        double parameterControl;

        //computes the term variance values of the data
        TermVariance tv = new TermVariance(numFeatures);
        tv.loadDataSet(trainSet, numFeatures, numClass);
        tv.evaluateFeatures();
        relevanceFeature = tv.getValues();

//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println("relevance f(" + i + ") = " + relevanceFeature[i]);
//        }

        //normalizes the relevance values by softmax scaling function
        for (int i = 0; i < numFeatures; i++) {
            mean += relevanceFeature[i];
        }
        mean /= numFeatures;

        for (int i = 0; i < numFeatures; i++) {
            variance += Math.pow(relevanceFeature[i] - mean, 2);
        }
        variance = Math.sqrt(variance / (numFeatures - 1));

        if (variance == 0) {
            variance = errorRelevance;
        }

        parameterControl = mean / variance;
        if (parameterControl == 0) {
            parameterControl = errorRelevance;
        }

        for (int i = 0; i < numFeatures; i++) {
            relevanceFeature[i] = (relevanceFeature[i] - mean) / (variance * parameterControl);
            relevanceFeature[i] = 1.0 / (1.0 + Math.pow(Math.E, -1 * relevanceFeature[i]));
        }

//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println("norm relevance f(" + i + ") = " + relevanceFeature[i]);
//        }
    }

    /**
     * places the ants randomly on the graph nodes as their starting nodes
     */
    private void setStartNode() {
        boolean[] checkArray = new boolean[numFeatures];

        for (int i = 0; i < numAnts; i++) {
            //finds starting node randomly
            while (true) {
                int rand = new Random().nextInt(numFeatures);
//                int rand = randNumber.nextInt(numFeatures);
                if (!checkArray[rand]) {
                    currentState[i] = rand;
                    checkArray[rand] = true;
                    break;
                }
            }
            //sets starting node into the tabu list
            tabuList[i][currentState[i]] = true;
            antSubsetSelected[i][0] = currentState[i];
        }
    }

    /**
     * greedy state transition rule
     *
     * @param indexAnt index of the ant
     * @param currentSize the current size of the selected feature
     * 
     * @return the index of the selected feature
     */
    private int greedyRule(int indexAnt, int currentSize) {
        int index = -1;
        double max = -Double.MAX_VALUE;

        for (int j = 0; j < numFeatures; j++) {
            if (!tabuList[indexAnt][j]) {
                double averageSim = 0;
                for (int i = 0; i < currentSize; i++) {
                    int newIndex = findIndex(antSubsetSelected[indexAnt][i], j);
                    averageSim += simValues[newIndex];
                }
                averageSim /= currentSize;
                double result = pheromoneValues[j] * Math.pow(relevanceFeature[j], alpha) / Math.pow(averageSim + errorSimilarity, beta);
                if (result > max) {
                    max = result;
                    index = j;
                }
            }
        }

        return index;
    }

    /**
     * probability state transition rule
     *
     * @param indexAnt index of the ant
     * @param currentSize the current size of the selected feature
     * 
     * @return the index of the selected feature
     */
    private int probRule(int indexAnt, int currentSize) {
        int index = -1;
        double rand = new Random().nextDouble();
//        double rand = randNumber.nextDouble();
        double[] prob = new double[numFeatures];
        double sumOfProb = 0;
        for (int j = 0; j < numFeatures; j++) {
            if (!tabuList[indexAnt][j]) {
                double averageSim = 0;
                for (int i = 0; i < currentSize; i++) {
                    int newIndex = findIndex(antSubsetSelected[indexAnt][i], j);
                    averageSim += simValues[newIndex];
                }
                averageSim /= currentSize;
                prob[j] = pheromoneValues[j] * Math.pow(relevanceFeature[j], alpha) / Math.pow(averageSim + errorSimilarity, beta);
                sumOfProb += prob[j];
            }
        }
        for (int j = 0; j < numFeatures; j++) {
            if (!tabuList[indexAnt][j]) {
                prob[j] /= sumOfProb;
                if (rand <= prob[j]) {
                    index = j;
                    break;
                }
            }
        }

        //if the next node(feature) is not selected by previous process
        if (index == -1) {
            while (true) {
//                int rand1 = randNumber.nextInt(numFeatures);
                int rand1 = new Random().nextInt(numFeatures);
                if (!tabuList[indexAnt][rand1]) {
                    index = rand1;
                    break;
                }
            }
        }

        return index;
    }

    /**
     * chooses the next feature among unvisited features according to the
     * state transition rules
     *
     * @param indexAnt the index of the ant
     * @param currentSize the current size of the selected feature
     * 
     * @return the index of the selected feature
     */
    private int stateTransitionRules(int indexAnt, int currentSize) {
        double q = new Random().nextDouble();
//        double q = randNumber.nextDouble();
        if (q <= probChooseEquation) {
            return greedyRule(indexAnt, currentSize);
        } else {
            return probRule(indexAnt, currentSize);
        }
    }

    /**
     * updates intensity of pheromone values
     */
    private void pheromoneUpdatingRule() {
        double sum = numAnts * (numFeatOfAnt - 1);

        for (int i = 0; i < numFeatures; i++) {
            pheromoneValues[i] = ((1 - decayRate) * pheromoneValues[i]) + (featureCounter[i] / sum);
        }
    }

    /**
     * starts the feature selection process by incremental relevance–redundancy
     * feature selection based on ant colony optimization, version2 (IRRFSACO_2)
     * method
     */
    @Override
    public void evaluateFeatures() {
        antSubsetSelected = new int[numAnts][numFeatOfAnt];
        relevanceFeature = new double[numFeatures];
        simValues = new double[(numFeatures * (numFeatures - 1)) / 2];
        featureCounter = new int[numFeatures];
        tabuList = new boolean[numAnts][numFeatures];
        currentState = new int[numAnts];
        pheromoneValues = new double[numFeatures];
        int[] indecesFeature;
        int counter = 0;

        //computes the relevance values of the features
        computeRelevance();

        //computes the similarity values between pairs of feature
        for (int i = 0; i < numFeatures; i++) {
            for (int j = 0; j < i; j++) {
                simValues[counter++] = Math.abs(MathFunc.computeSimilarity(trainSet, i, j));
            }
        }

        //sets the initial intensity of pheromone
        Arrays.fill(pheromoneValues, initPheromoneValue);

        //starts the feature selection process
        for (int nc = 0; nc < maxIteration; nc++) {
            //System.out.println("          ------ Iteration " + nc + " -----");

            //sets the initial values of feature counter (FC) to zero
            Arrays.fill(featureCounter, 0);

            //sets the initial values of tabu list to false
            for (int i = 0; i < numAnts; i++) {
                Arrays.fill(tabuList[i], false);
            }

            //places the ants randomly on the nodes in the graph
            setStartNode();

            //selects predefined number of features for all ants
            for (int i = 1; i < numFeatOfAnt; i++) {
                for (int k = 0; k < numAnts; k++) {
                    int newFeature = stateTransitionRules(k, i);
                    tabuList[k][newFeature] = true;
                    antSubsetSelected[k][i] = newFeature;
                    featureCounter[newFeature]++;
                    currentState[k] = newFeature;
                }
            }

            //updates intensity of the pheromone values
            pheromoneUpdatingRule();
        }

        indecesFeature = ArraysFunc.sortWithIndex(Arrays.copyOf(pheromoneValues, pheromoneValues.length), true);
//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println(i + ") =  " + pheromoneValues[i]);
//        }

        selectedFeatureSubset = Arrays.copyOfRange(indecesFeature, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }

    /**
     * This method return the subset of selected features by incremental
     * relevance–redundancy feature selection based on ant colony optimization,
     * version2 (IRRFSACO_2) method.
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
     * These values does not exist for IRRFSACO_2.
     *
     * @return an array of  weight of features
     */
    @Override
    public double[] getValues() {
        return null;
    }
}
