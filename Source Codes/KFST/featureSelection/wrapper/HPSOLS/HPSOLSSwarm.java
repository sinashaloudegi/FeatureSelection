package KFST.featureSelection.wrapper.HPSOLS;

import KFST.featureSelection.wrapper.HGAFS.LocalSearchOperation;

import java.util.Random;

/**
 * Created by sina on 7/11/2017.
 */
public class HPSOLSSwarm {

    HPSOLSParticle[] particles;
    int gb[];
    int numSwarmPopultion;
    int numFeatures;
    HPSOLSFitCalculator hpsolsPsoFitCalculator;
    int numSelectedFeatures;
    LocalSearchOperation localSearchOperation;

    public HPSOLSSwarm(int numFeatures, int numSwarmPopultion, HPSOLSFitCalculator hpsolsFitCalculator, int numSelectedFeatures, LocalSearchOperation localSearchOperation) {
        this.localSearchOperation = localSearchOperation;
        this.numSwarmPopultion = numSwarmPopultion;
        this.numFeatures = numFeatures;
        this.hpsolsPsoFitCalculator = hpsolsFitCalculator;
        this.numSelectedFeatures = numSelectedFeatures;
        particles = new HPSOLSParticle[numSwarmPopultion];
        gb = new int[numFeatures];
    }

    public void initialize() {
        for (int i = 0; i < numSwarmPopultion; i++) {
            particles[i] = new HPSOLSParticle(numFeatures, hpsolsPsoFitCalculator,localSearchOperation);

            for (int j = 0; j < numFeatures; j++) {
                Random rand = new Random();
                double r = rand.nextDouble();

                particles[i].v[j] = r;
                particles[i].x[j] = S(r);

            }

            particles[i].refine(numSelectedFeatures, numFeatures);
            particles[i].pBest = particles[i].x;

        }
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
        for (int i = 0; i < numSwarmPopultion; i++) {
            for (int j = 0; j < numFeatures; j++) {
                Random rnd1 = new Random();
                Random rnd2 = new Random();
                double r1 = rnd1.nextDouble();
                double r2 = rnd2.nextDouble();
                particles[i].v[j] = particles[i].v[j] + r1 * 2 * (particles[i].x[j] - particles[i].pBest[j]) + r2 * 2 * (particles[i].x[j] - gb[j]);
                particles[i].x[j] = S(particles[i].v[j]);

            }
            particles[i].refine(numSelectedFeatures, numFeatures);
            particles[i].fit();
            if (particles[i].fitness > fit(particles[i].pBest)) {
                particles[i].pBest = particles[i].x;
            }
            if (particles[i].fitness > fit(gb)) {
                gb = particles[i].x;
            }

        }

    }

    private double fit(int[] pBest) throws Exception {
        HPSOLSParticle temp = new HPSOLSParticle(numFeatures, hpsolsPsoFitCalculator,localSearchOperation);
        String s = temp.toString(pBest);
        return hpsolsPsoFitCalculator.remove(s);
    }
}
