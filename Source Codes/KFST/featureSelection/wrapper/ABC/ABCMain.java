package KFST.featureSelection.wrapper.ABC;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.WrapperApproach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sina on 8/7/2017.
 */
public class ABCMain implements WrapperApproach {

    int numSelectedFeatures;
    int numFeatures;
    int numFoodSource;
    int maxLimit;
    int[] selecteFeatureSubset;
    double MR;
    int numIteration;
    String pathData;
    String pathTestData;
    List<FoodSource> foodSources;
    List<FoodSource> abandoned;
    List<FoodSource> onlookers;
    FoodSource bestFoodSource;
    ABCFitCalculator abcFitCalculator;
    int onlookersPoint;

    public ABCMain(int numSelectedFeatures, int maxLimit, double MR, int numIteration) {
        this.maxLimit = maxLimit;
        this.MR = MR;
        this.numIteration = numIteration;
        this.numSelectedFeatures = numSelectedFeatures;

    }

    private void init() throws Exception {
        foodSources = new ArrayList<FoodSource>();
        abandoned = new ArrayList<FoodSource>();
        onlookers = new ArrayList<FoodSource>();
        for (int i = 0; i < numFeatures; i++) {
            FoodSource foodSource = new FoodSource(numFeatures, abcFitCalculator);
            foodSource.initialize(i);
            foodSource.calculateFitness();
            foodSources.add(foodSource);

        }
    }

    private void employed() throws Exception {

        int listSize = foodSources.size();
        for (int i = 0; i < listSize; i++) {
            FoodSource neighbor = new FoodSource(numFeatures, abcFitCalculator);
            neighbor.initialize(-1);
            for (int j = 0; j < numFeatures; j++) {
                Random r = new Random();
                double rand = r.nextDouble();
                if (rand < MR) {
                    neighbor.x[j] = 1;
                } else {
                    neighbor.x[j] = foodSources.get(i).x[j];
                }
            }
            neighbor.calculateFitness();
            if (neighbor.getFitness() > foodSources.get(i).getFitness()) {
                foodSources.add(neighbor);
            } else {
                foodSources.get(i).limit++;
                if (foodSources.get(i).getLimit() > maxLimit) {
                    abandoned.add(foodSources.get(i));
                    foodSources.remove(i);
                    i--;
                    listSize--;
                }
            }

        }
    }

    private boolean employed(FoodSource foodSource, List<FoodSource> listAdd, List<FoodSource> listAbandoned) throws Exception {
        boolean checkRemove = false;
        FoodSource neighbor = new FoodSource(numFeatures, abcFitCalculator);
        neighbor.initialize(-1);
        for (int j = 0; j < numFeatures; j++) {
            Random r = new Random();
            double rand = r.nextDouble();
            if (rand < MR) {
                neighbor.x[j] = 1;
            } else {
                neighbor.x[j] = foodSource.x[j];
            }
        }
        neighbor.calculateFitness();
        if (neighbor.getFitness() > foodSource.getFitness()) {
            listAdd.add(neighbor);
            onlookersPoint++;
        } else {
            foodSource.limit++;
            if (foodSource.getLimit() > maxLimit) {
                listAbandoned.add(foodSource);
                checkRemove = true;
            }
        }
        return checkRemove;
    }

    private void onlooker() throws Exception {
        double sumFitness = 0;
        onlookersPoint = 0;
        for (int i = 0; i < foodSources.size(); i++) {
            sumFitness += foodSources.get(i).getFitness();
        }
        for (int i = 0; i < foodSources.size(); i++) {
            foodSources.get(i).setP(foodSources.get(i).getFitness() / sumFitness);
        }

        double[] roulette = new double[foodSources.size()];
        for (int i = 0; i < foodSources.size(); i++) {
            if (i == 0) {
                roulette[i] = (foodSources.get(i).getP());
            } else {
                roulette[i] = (foodSources.get(i).getP()) + roulette[i - 1];
            }
        }
        Random r = new Random();
        List<FoodSource> listAbandoned = new ArrayList<FoodSource>();
        for (int i = 0; i < numFeatures; i++) {
            int selected = 0;
            double rand = r.nextDouble();
            for (int j = 0; j < roulette.length; j++) {
                if (rand < roulette[j]) {
                    selected = j;
                    break;
                }
            }
            FoodSource newEmployee = foodSources.get(selected);

            onlookers.add(newEmployee);
            onlookersPoint++;
            if (employed(newEmployee, onlookers, listAbandoned)) {
                onlookersPoint--;
                onlookers.remove(onlookersPoint);

            }

        }
        abandoned.addAll(listAbandoned);
        foodSources.addAll(onlookers);
        onlookers.clear();

    }

    private void calculateBestFoodSource() {
        double maxFit = 0;
        int jMaxFit = 0;
        for (int j = 0; j < foodSources.size(); j++) {
            if (foodSources.get(j).getFitness() > maxFit) {
                maxFit = foodSources.get(j).getFitness();
                jMaxFit = j;
            }
        }
        bestFoodSource = foodSources.get(jMaxFit);
    }

    private void scout() throws Exception {
        int abandonedSize = abandoned.size();
        List<FoodSource> listAbandoned = new ArrayList<FoodSource>();
        List<Integer> listDel = new ArrayList<Integer>();
        for (int i = 0; i < abandonedSize; i++) {
            FoodSource foodSource = new FoodSource(numFeatures, abcFitCalculator);
            foodSource.initializeRandom();
            foodSource.calculateFitness();
            abandoned.set(i, foodSource);
            if (employed(abandoned.get(i), abandoned, listAbandoned)) {
                listDel.add(i);
            }
        }
        for (int i = 0; i < abandoned.size(); i++) {
            boolean checkDel = false;
            for (int j = 0; j < listDel.size(); j++) {
                if (i == listDel.get(j)) {
                    checkDel = true;
                    break;
                }
            }
            if (!checkDel) {
                foodSources.add(abandoned.get(i));
            }
        }
        abandoned.clear();
        abandoned.addAll(listAbandoned);

    }

    @Override
    public void loadDataSet(DatasetInfo ob) {
        numFeatures = ob.getNumFeature();
        numFoodSource = numFeatures;
        pathData = ob.getPathData();
        pathTestData = ob.getPathTestSet();
        try {
            abcFitCalculator = new ABCFitCalculator(pathData, pathTestData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {

    }

    @Override
    public void evaluateFeatures() throws Exception {
        System.out.println("start");

        init();
        System.out.println("init");
        for (int i = 0; i < numIteration; i++) {
            employed();
            System.out.println(i + " : em");
            onlooker();
            System.out.println(i + " : on");
            calculateBestFoodSource();
            System.out.println(i + " : ca");
            scout();
            System.out.println(i + " : sc");

        }
    }


    @Override
    public int[] getSelectedFeatureSubset() {
        selecteFeatureSubset = bestFoodSource.getX();
        return toIntArray(selecteFeatureSubset);
    }

    private int[] toIntArray(int[] gb) {
        int numOfOnes = numOfOnes(gb);
        int temp[] = new int[numOfOnes];
        int counter = 0;
        for (int i = 0; i < gb.length; i++) {
            if (gb[i] == 1) {
                temp[counter] = i;
                counter++;
            }

        }
        return temp;
    }

    private int numOfOnes(int[] gb) {
        int counter = 0;
        for (int i = 0; i < gb.length; i++) {
            if (gb[i] == 1) {
                counter++;
            }

        }
        return counter;
    }

    @Override
    public double[] getValues() {
        return new double[0];
    }
}
