package KFST.featureSelection.wrapper.PSO4_2;

import java.util.Random;

/**
 * Created by sina on 7/11/2017.
 */
public class PSO4_2Particle {

    double fitness;
    int pBest[];
    int x[];
    double v[];

    PSO4_2FitCalculator pso42FitCalculator;

    public PSO4_2Particle(int numFeatures, PSO4_2FitCalculator pso42FitCalculator) {
        v = new double[numFeatures];
        x = new int[numFeatures];
        pBest = new int[numFeatures];
        this.pso42FitCalculator = pso42FitCalculator;
    }


    public void fit() throws Exception {
        String s = toString(x);

        this.fitness = pso42FitCalculator.remove(s);


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
