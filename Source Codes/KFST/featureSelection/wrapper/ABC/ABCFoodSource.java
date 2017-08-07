package KFST.featureSelection.wrapper.ABC;

/**
 * Created by sina on 8/7/2017.
 */
public class ABCFoodSource {
    int[] x;
    int limit;
    double fitness;
    int p;
    ABCFitCalculator abcFitCalculator;

    public void calculateFitness() throws Exception {
        String s = toString(x);
        this.fitness = abcFitCalculator.remove(s);
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

    public int getP() {
        return p;
    }

    public void setP(int p) {
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
