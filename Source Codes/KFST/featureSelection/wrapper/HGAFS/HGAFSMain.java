package KFST.featureSelection.wrapper.HGAFS;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.GeneticAlgorithm.FitnessCalculator;
import KFST.featureSelection.wrapper.WrapperApproach;
import KFST.util.ArraysFunc;
import weka.core.Instances;

import java.util.Random;

/**
 * Created by sina on 7/5/2017.
 */
public class HGAFSMain implements WrapperApproach {


    private double[][] trainSet;
    private int numFeatures;
    private int numClass;
    private int[] selectedFeatureSubset;
    Strings classifier;
    Population p;

    int numSelectedFeatures;
    double pCrossover;
    double pMutation;
    int numPopulation;
    double miu;
    Instances data;
    String pathData;
    String pathTestData;
    FitnessCalculator fitnessCalculator;

    public HGAFSMain(int numSelectedFeatures, int numPopulation, double pCrossover, double pMutation, double miu) throws Exception {
        this.numSelectedFeatures = numSelectedFeatures;
        this.numPopulation = numPopulation;
        this.pCrossover = pCrossover;
        this.pMutation = pMutation;
        this.miu = miu;
    }

    private void init() {
        Population p = new Population(numPopulation, numFeatures, numSelectedFeatures);
        p.initPopulation();

    }

    @Override
    public void loadDataSet(DatasetInfo ob) {
        trainSet = ob.getTrainSet();
        numFeatures = ob.getNumFeature();
        numClass = ob.getNumClass();
        pathData = ob.getPathData();
        pathTestData = ob.getPathTestSet();
    }

    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {
        trainSet = ArraysFunc.copyDoubleArray2D(data);
        numFeatures = numFeat;
        numClass = numClasses;
    }

    @Override
    public void evaluateFeatures() {
        init();
        start();

    }

    public void start() {
        int counter = 20;

        LocalSearchOperation localSearchOperation = new LocalSearchOperation(data);

        while (counter > 0) {

            FitCalculator fitCalculator = new FitCalculator(p);
            p = fitCalculator.fit(p);
            Strings[] parent = p.getStrings();
            for (int i = 0; i < parent.length - 1; i += 2) {
                double rand = Math.random();
                if (rand < pCrossover) {
                    Strings offspring1 = null, offspring2 = null;
                    if (numFeatures < 10) {
                        int x = randomPosition();
                        offspring1 = crossing(parent[i], parent[i + 1], x);
                        offspring2 = crossing(parent[i + 1], parent[i], x);
                    } else if (numFeatures >= 10) {
                        int a = randomPosition();
                        int b = randomPosition();
                        int x = Math.min(a, b);
                        int y = Math.max(a, b);
                        offspring1 = crossing2(parent[i], parent[i + 1], x, y);
                        offspring2 = crossing2(parent[i + 1], parent[i], x, y);
                    }

                    offspring1 = mutation(offspring1);
                    offspring2 = mutation(offspring2);

                    offspring1 = localSearchOperation.lso(offspring1);
                    offspring2 = localSearchOperation.lso(offspring2);
                }
            }
            counter--;

        }
    }

    private Strings mutation(Strings s) {
        byte[] genes = s.getGene();
        Random random = new Random();
        for (int i = 0; i < genes.length; i++) {
            double rnd = random.nextDouble();
            if (rnd <= pMutation) {
                genes[i] = (byte) (1 - genes[i]);
            }
        }
        s.setGene(genes);
        return s;
    }

    private Strings crossing(Strings a, Strings b, int x) {
        Strings result = new Strings();
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

    private Strings crossing2(Strings a, Strings b, int x, int y) {
        Strings result = new Strings();
        byte[] geneA = a.getGene();
        byte[] geneB = b.getGene();
        byte[] geneResult = new byte[geneA.length];
        for (int i = 0; i < geneResult.length; i++) {
            if (i < x | i > y) {
                geneResult[i] = geneA[i];
            } else {
                geneResult[i] = geneB[i];
            }
        }
        result.setGene(geneResult);

        return result;
    }

    private int randomPosition() {
        double rand = Math.random();
        rand *= 100;
        rand %= numFeatures;
        return (int) rand;
    }

    @Override
    public int[] getSelectedFeatureSubset() {
        return selectedFeatureSubset;
    }

    @Override
    public double[] getValues() {
        return null;
    }
}
