package KFST.featureSelection.wrapper.GeneticAlgorithm;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.WrapperApproach;
import KFST.util.ArraysFunc;

import java.util.Random;

/**
 * Created by sina on 6/25/2017.
 */
public class GeneticAlgorithmMain implements WrapperApproach {


    private double[][] trainSet;
    private int numFeatures;
    private int numClass;
    private int[] selectedFeatureSubset;
    String classifier;
    Population p;

    int sizeSelectedFeatureSubset;
    int numPopulation;
    int numGeneration;
    double pCrossover;
    double pMutation;
    float r;


    String pathData;
    String pathTestData;
    FitnessCalculator fitnessCalculator;

        public GeneticAlgorithmMain(int sizeSelectedFeatureSubset, int numPopulation, int numGeneration, double pCrossover, double pMutation, String classifier) throws Exception {
            this.sizeSelectedFeatureSubset = sizeSelectedFeatureSubset;
            this.numPopulation = numPopulation;
            this.numGeneration = numGeneration;
            this.pCrossover = pCrossover;
            this.pMutation = pMutation;
            this.classifier = classifier;
            r = (float) 0.5; //a portion of the  population that need to be selected

        }

    public void initialize() throws Exception {
        fitnessCalculator = new FitnessCalculator(classifier, pathData, pathTestData);
        p = new Population(numPopulation, numFeatures);
        p.init(sizeSelectedFeatureSubset);
        fitnessCalculator.fitness(p);

    }

    public void start() throws Exception {
        int n = (int) ((1 - r) * numPopulation);

        for (int i = 0; i < numGeneration; i++) {
            Population ps = new Population(numPopulation, numFeatures);


            ps.setIndividuals(select(n, p));

            //crossover
            ps = crossOver(ps);

            //mutation
            ps = mutation(ps);

            //ps to p
            p = ps;


            //fit p
            p = fitnessCalculator.fitness(p);

        }

        selectedFeatureSubset = result(p.best());


    }

    private int[] result(Individual best) {
        byte b[] = best.getGene();


        String res = "";
        for (int i = 0; i < b.length; i++) {
            if (b[i] == 1) {
                res += (i) + ",";
            }

        }
        res = res.substring(0, res.length() - 1);
        return toIntArray(res);
    }

    private int[] toIntArray(String res) {
        String temp[] = res.split(",");
        int result[] = new int[temp.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.valueOf(temp[i]);
        }
        return result;
    }


    private Population mutation(Population ps) {
        for (int i = 0; i < ps.individuals.length; i++) {
            double rand = Math.random();
            if (rand < pMutation) {
                ps.individuals[i] = mutate(ps.individuals[i]);
            }
        }
        ps.refineNumOfOnes(sizeSelectedFeatureSubset);
        return ps;
    }

    private Individual mutate(Individual individual) {

        int i = randomPosition();
        individual.gene[i] = (byte) (1 - individual.gene[i]);
        return individual;
    }

    private int randomPosition() {
        double rand = Math.random();
        rand *= 100;
        rand %= numFeatures;
        return (int) rand;
    }

    private Population crossOver(Population p) {
        int numCrossOver = 0;
        Individual[] real = p.getIndividuals();
        Individual[] real_child = new Individual[real.length * 2];
        System.arraycopy(real, 0, real_child, 0, real.length);
        for (int i = 0, j = real.length; i < real.length - 1; i += 2) {
            double rand = Math.random();
            if (rand < pCrossover) {
                numCrossOver += 2;
                int x = randomPosition();
                real_child[j] = crossing(real[i], real[i + 1], x);
                real_child[j + 1] = crossing(real[i + 1], real[i], x);
                j += 2;
            }
        }
        Individual[] result = new Individual[real.length + numCrossOver];
        System.arraycopy(real_child, 0, result, 0, result.length);


        Population temp = new Population(numPopulation + numCrossOver, numFeatures);
        temp.setIndividuals(result);

        temp.refineNumOfOnes(sizeSelectedFeatureSubset);
        return temp;
    }

    private Individual crossing(Individual a, Individual b, int x) {
        Individual result = new Individual();
        byte[] geneA = a.getGene();
        byte[] geneB = b.getGene();
        byte[] geneResult = new byte[geneA.length];
        for (int i = 0; i < geneResult.length; i++) {
            if (i < x) {
                geneResult[i] = geneA[i];
            } else {
                geneResult[i] = geneB[i];
            }
        }
        result.setGene(geneResult);

        return result;
    }

    //Select n distinct Individuals from p using
    private Individual[] select(int n, Population p) {
        if (n % 2 != 0) {
            n++;
        }
        Individual[] temp = new Individual[n];
        Individual[] real = p.getIndividuals();
        double fitnessSum = 0;
        for (int i = 0; i < real.length; i++) {
            fitnessSum += real[i].getFitness();
        }

        double[] roulette = new double[real.length];
        for (int i = 0; i < real.length; i++) {
            if (i == 0) {
                roulette[i] = (real[i].getFitness()) / fitnessSum;
            } else {
                roulette[i] = ((real[i].getFitness()) / fitnessSum) + roulette[i - 1];
            }
        }

        int[] arraySelect = new int[n];
        for (int i = 0; i < n; i++) {
            arraySelect[i] = -1;
        }
        int j = 0;
        Random r = new Random();
        double randomValue;
        int selected = 0;
        while (j < n) {
            randomValue = r.nextDouble();
            for (int i = 0; i < roulette.length; i++) {
                if (randomValue < roulette[i]) {
                    selected = i;
                    break;
                }
            }
            Boolean exist = false;
            for (int i = 0; i < j; i++) {
                if (arraySelect[i] == selected) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                arraySelect[j] = selected;
                temp[j] = real[selected];
                j++;
            }

        }

        return temp;
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
        pathData = ob.getPathData();
        pathTestData = ob.getPathTestSet();
    }

    /**
     * loads the dataset
     *
     * @param data       the input dataset values
     * @param numFeat    the number of features in the dataset
     * @param numClasses the number of classes in the dataset
     */
    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {
        trainSet = ArraysFunc.copyDoubleArray2D(data);
        numFeatures = numFeat;
        numClass = numClasses;
    }

    /**
     * starts the feature selection process by Genetic Algorithm
     */
    @Override
    public void evaluateFeatures() {
        try {
            initialize();
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method return the subset of selected features by microarray gene
     * selection based on ant colony optimization (MGSACO) method.
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
     * These values does not exist for MGSACO.
     *
     * @return an array of  weight of features
     */
    @Override
    public double[] getValues() {
        return null;
    }
}
