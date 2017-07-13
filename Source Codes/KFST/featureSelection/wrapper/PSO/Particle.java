package KFST.featureSelection.wrapper.PSO;

/**
 * Created by sina on 7/11/2017.
 */
public class Particle {

    double fitness;
    int pBest[];
    int x[];
    double v[];

    PSOFitCalculator psoFitCalculator;

    public Particle(int numFeatures, PSOFitCalculator psoFitCalculator) {
        v = new double[numFeatures];
        x = new int[numFeatures];
        pBest = new int[numFeatures];
        this.psoFitCalculator = psoFitCalculator;
    }


    public void fit() throws Exception {
        String s = toString(x);

        this.fitness = psoFitCalculator.remove(s);


    }

    public void refine() {
        //TODO refine number of ones using random strategy
        int counter = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 1) {
                counter++;
            }

        }
        System.out.println("numOfOnes: " + counter);
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
