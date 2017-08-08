package KFST.featureSelection.wrapper.ABC;

import java.util.Random;

/**
 * Created by sina on 8/7/2017.
 */
public class FoodSource {
    int[] x;
    int limit;
    double fitness;
    double p;
    ABCFitCalculator abcFitCalculator;
    int numFeatures;

    public void calculateFitness() throws Exception {
        String s = toString(x);
        this.fitness = abcFitCalculator.remove(s);
    }

    public FoodSource(int numFeatures, ABCFitCalculator abcFitCalculator) {
        this.numFeatures = numFeatures;
        this.abcFitCalculator = abcFitCalculator;
        limit = 0;
    }

    public void initialize(int i) {
        x = new int[numFeatures];
        for (int j = 0; j < numFeatures; j++) {
            if (i == j) {
                x[j] = 1;
            } else {
                x[j] = 0;
            }
        }
    }

    public void initializeRandom() {
        x = new int[numFeatures];
        Random r = new Random();

        for (int j = 0; j < numFeatures; j++) {
            double rand = r.nextDouble();
            if (rand > 0.5) {
                x[j] = 1;
            } else {
                x[j] = 0;
            }
        }
    }

    public int[] getX() {
        return x;
    }

    public void setX(int[] x) {
        this.x = x;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public String toString(int[] particle) {
        String s = "";
        for (int i = 0; i < particle.length; i++) {
            if (particle[i] == 0) {
                s += (i + 1) + ",";
            }
        }
        if (s.length() != 0) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
