package KFST.featureSelection.wrapper.BPSO;

/**
 * Created by sina on 7/30/2017.
 */
public class BPSOParticle {
    double fitness;
    double pBest[];
    int z[];
    double x[];
    BPSOFitCalculator bpsoFitCalculator;

    public BPSOParticle(int numFeatures, BPSOFitCalculator bpsoFitCalculator) {
        x = new double[numFeatures];
        z = new int[numFeatures];
        pBest = new double[numFeatures];
        this.bpsoFitCalculator = bpsoFitCalculator;
    }

    public void fit() throws Exception {
        String s = toString(z);

        this.fitness = bpsoFitCalculator.remove(s);


    }

    private int numOfOnes() {

        int counter = 0;
        for (int i = 0; i < z.length; i++) {
            if (z[i] == 1) {
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
