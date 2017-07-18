package KFST.featureSelection.wrapper.HPSOLS;

import java.util.Random;

/**
 * Created by sina on 7/11/2017.
 */
public class HPSOLSParticle {

    double fitness;
    int pBest[];
    int x[];
    double v[];

    HPSOLSFitCalculator hpsolsPsoFitCalculator;

    public HPSOLSParticle(int numFeatures, HPSOLSFitCalculator hpsolsPsoFitCalculator) {
        v = new double[numFeatures];
        x = new int[numFeatures];
        pBest = new int[numFeatures];
        this.hpsolsPsoFitCalculator = hpsolsPsoFitCalculator;
    }


    public void fit() throws Exception {
        String s = toString(x);

        this.fitness = hpsolsPsoFitCalculator.remove(s);


    }

    public void refine(int numSelectedFeatures, int numFeatures) {
        int ones = numOfOnes();

        if (ones > numSelectedFeatures) {

            int temp = ones;
            while (temp != numSelectedFeatures) {
                Random rand = new Random();
                int rnd = rand.nextInt(numFeatures);
                if (this.x[rnd] == 1) {
                    this.x[rnd] = 0;
                    temp--;
                }

            }

        } else if (ones < numSelectedFeatures) {
            int temp = numSelectedFeatures;
            while (temp != ones) {
                Random rand = new Random();
                int rnd = rand.nextInt(numFeatures);
                if (this.x[rnd] == 0) {
                    this.x[rnd] = 1;
                    temp--;
                }

            }
        }
    }

    private int numOfOnes() {

        int counter = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 1) {
                counter++;
            }

        }
        return counter;
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
