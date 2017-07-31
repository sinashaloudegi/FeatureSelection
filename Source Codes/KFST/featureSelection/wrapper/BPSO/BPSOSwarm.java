package KFST.featureSelection.wrapper.BPSO;

import java.util.Random;

/**
 * Created by sina on 7/30/2017.
 */
public class BPSOSwarm {
    BPSOParticle[] particles;
    int gb[];
    int numSwarmPopultion;
    int numFeatures;
    BPSOFitCalculator bpsoFitCalculator;
    int numSelectedFeatures;

    public BPSOSwarm(int numFeatures, int numSwarmPopultion, BPSOFitCalculator bpsoFitCalculator, int numSelectedFeatures) {
        this.numSwarmPopultion = numSwarmPopultion;
        this.numFeatures = numFeatures;
        this.bpsoFitCalculator = bpsoFitCalculator;
        this.numSelectedFeatures = numSelectedFeatures;
        particles = new BPSOParticle[numSwarmPopultion];
        gb = new int[numFeatures];
    }

    public void initialize() {
        for (int i = 0; i < numSwarmPopultion; i++) {
            particles[i] = new BPSOParticle(numFeatures, bpsoFitCalculator);

            for (int j = 0; j < numFeatures; j++) {
                Random rand = new Random();
                double r = rand.nextDouble();
                double r2 = rand.nextDouble();

                particles[i].x[j] = r;
                particles[i].pBest[j] = r;
                if(r >= r2){
                    particles[i].z[j] = 1;
                }else {
                    particles[i].z[j] = 0;
                }

            }
        }

    }

    public void calculateFitness() throws Exception {
        for (int i = 0; i < numSwarmPopultion; i++) {
            particles[i].fit();
        }
    }

    public void update() throws Exception {

    }

    private double fit(int[] pBest) throws Exception {
        double x=0;
       return x;
    }
}
