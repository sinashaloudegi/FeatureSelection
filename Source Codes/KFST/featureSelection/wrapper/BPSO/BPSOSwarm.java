package KFST.featureSelection.wrapper.BPSO;

import java.util.Random;

/**
 * Created by sina on 7/30/2017.
 */
public class BPSOSwarm {
    BPSOParticle[] particles;
    double gb[];
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
        gb = new double[numFeatures];
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

    public boolean update() throws Exception {
        boolean gBestImproved=false;
        for (int i = 0; i < numSwarmPopultion; i++) {
            updatePbest(particles[i]);
            if (particles[i].fitness > fit(gb)) {
                gb = particles[i].x;
                gBestImproved=true;
            }
            Random rand = new Random();
            for (int j = 0; j < numFeatures; j++) {
                double r = rand.nextDouble();
                if(r < 0.5){
                    particles[i].x[j]=N((particles[i].pBest[j] + gb[j])/2,Math.abs(particles[i].pBest[j] - gb[j]));
                }else{
                    particles[i].x[j]=particles[i].pBest[j];
                }
            }
        }
 return gBestImproved;
    }

    private double N(double mean, double variance){
        return mean + new Random().nextGaussian() * variance;
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

    public void uniformCombination(int num){
        double Pc= 0.2 / (1 + Math.pow(Math.E,5- num));
        Random rand = new Random();
        for (int i = 0; i < numSwarmPopultion; i++) {
            double r = rand.nextDouble();
            if(Pc>r){
                int k=i;
                while (i!=k){
                    k = (int) (rand.nextDouble()*(numSwarmPopultion-1));
                }
                 int Un = (int) (Pc * numFeatures);
                if(Pc!=1){
                    Un++;
                }

                for (int j=0;j<Un;j++){
                   int l = (int) (rand.nextDouble()*(numFeatures-1));
                    double r2 = rand.nextDouble();
                    particles[i].x[l]= particles[k].pBest[l] + r2 ;
                }
            }
        }
    }
}
