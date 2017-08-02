package KFST.featureSelection.wrapper.PSO4_2;

import java.util.Random;

/**
 * Created by sina on 7/11/2017.
 */
public class PSO4_2Swarm {

    PSO4_2Particle[] particles;
    int gb[];
    int numSwarmPopultion;
    int numFeatures;
    PSO4_2FitCalculator pso42FitCalculator;
    int numSelectedFeatures;

    public PSO4_2Swarm(int numFeatures, int numSwarmPopultion, PSO4_2FitCalculator pso42FitCalculator, int numSelectedFeatures) {
        this.numSwarmPopultion = numSwarmPopultion;
        this.numFeatures = numFeatures;
        this.pso42FitCalculator = pso42FitCalculator;
        this.numSelectedFeatures = numSelectedFeatures;
        particles = new PSO4_2Particle[numSwarmPopultion];
        gb = new int[numFeatures];
    }

    public void initialize() {

        for (int i = 0; i < numSwarmPopultion; i++) {
            particles[i] = new PSO4_2Particle(numFeatures, pso42FitCalculator);
            particles[i].x = mixedInit();

            for (int j = 0; j < numFeatures; j++) {
                Random rand = new Random();
                double r = rand.nextDouble();

                particles[i].v[j] = r;

            }

            particles[i].refine(numSelectedFeatures, numFeatures);
            particles[i].pBest = particles[i].x;

        }


    }

    private int[] mixedInit() {
        //TODO mixed Init implemention
        return null;
    }

    private int S(double r) {
        double s = 1 / (1 + Math.pow(Math.E, -r));
        Random rand = new Random();
        double rnd = rand.nextDouble();
        if (rnd < s) {
            return 1;
        } else {
            return 0;
        }

    }

    public void calculateFitness() throws Exception {
        for (int i = 0; i < numSwarmPopultion; i++) {
            particles[i].fit();
        }
    }

    public void update() throws Exception {
        Random rnd1 = new Random();
        Random rnd2 = new Random();
        double r1, r2;

        for (int i = 0; i < numSwarmPopultion; i++) {

            particles[i].refine(numSelectedFeatures, numFeatures);
            particles[i].fit();
            if (particles[i].fitness > fit(particles[i].pBest)) {
                particles[i].pBest = particles[i].x;
            } else if (particles[i].fitness == fit(particles[i].pBest) && numOfOnes(particles[i].x) < numOfOnes(particles[i].pBest)) {
                particles[i].pBest = particles[i].x;

            }
            if (fit(particles[i].pBest) > fit(gb)) {
                gb = particles[i].pBest;
            } else if (fit(particles[i].pBest) == fit(gb) && numOfOnes(particles[i].pBest) < numOfOnes(gb)) {
                gb = particles[i].pBest;

            }
            for (int j = 0; j < numFeatures; j++) {
                r1 = rnd1.nextDouble();
                r2 = rnd2.nextDouble();
                particles[i].v[j] = particles[i].v[j] + r1 * 2 * (particles[i].pBest[j] - particles[i].x[j]) + r2 * 2 * (gb[j] - particles[i].x[j]);
                particles[i].x[j] = S(particles[i].v[j]);

            }

        }

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

    private double fit(int[] pBest) throws Exception {
        PSO4_2Particle temp = new PSO4_2Particle(numFeatures, pso42FitCalculator);
        String s = temp.toString(pBest);
        return pso42FitCalculator.remove(s);
    }
}
