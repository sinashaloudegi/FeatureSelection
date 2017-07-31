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
        for (int i = 0; i < numSwarmPopultion; i++) {
            updatePbest(particles[i]);

        }

    }

    private void updatePbest(BPSOParticle particle) throws Exception {
        if(fit(particle.pBest)< fit(particle.x)){
            for (int j = 0; j < numFeatures; j++) {
                particle.pBest[j] = 0.5 * (particle.x[j] + particle.z[j] );
            }
        }
        if(fit(particle.pBest)== fit(particle.x) & numOfOne(particle.pBest , particle.z)){
            for (int j = 0; j < numFeatures; j++) {
                particle.pBest[j] = 0.5 * (particle.x[j] + particle.z[j] );
            }
        }
    }

    private boolean numOfOne(double[] pBest , int[] z) throws Exception {
        int[] temp=new int[numFeatures];
        Random rand = new Random();
        for (int i=0;i<numFeatures;i++) {
            double r = rand.nextDouble();

            if (pBest[i] >= r) {
                temp[i] = 1;
            } else {
                temp[i] = 0;
            }
        }
        if(numOfOnes(temp) >= numOfOnes(z)){
            return true;
        }else{
            return false;
        }
    }
    private int numOfOnes(int[] z) {

        int counter = 0;
        for (int i = 0; i < z.length; i++) {
            if (z[i] == 1) {
                counter++;
            }

        }
        return counter;
    }

    private double fit(double[] pBest) throws Exception {
        BPSOParticle temp = new BPSOParticle(numFeatures, bpsoFitCalculator);
        int[] z=new int[numFeatures];
        Random rand = new Random();
        for (int i=0;i<numFeatures;i++) {
            double r = rand.nextDouble();

            if (pBest[i] >= r) {
                z[i] = 1;
            } else {
                z[i] = 0;
            }
        }
        String s = temp.toString(z);
        return bpsoFitCalculator.remove(s);
    }
}
