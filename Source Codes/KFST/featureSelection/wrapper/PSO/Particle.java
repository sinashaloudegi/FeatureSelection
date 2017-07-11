package KFST.featureSelection.wrapper.PSO;

/**
 * Created by sina on 7/11/2017.
 */
public class Particle {

    double fitness;
    int pBest[];
    int x[];
    double v[];


    public void fit() {
        this.fitness = 0.2;
    }
}
