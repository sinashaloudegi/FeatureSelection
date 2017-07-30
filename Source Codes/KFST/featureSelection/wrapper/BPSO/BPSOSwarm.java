package KFST.featureSelection.wrapper.BPSO;

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

    }

    public void calculateFitness() throws Exception {
        for (int i = 0; i < numSwarmPopultion; i++) {
            //
        }
    }

    public void update() throws Exception {

    }

    private double fit(int[] pBest) throws Exception {
        double x=0;
       return x;
    }
}
