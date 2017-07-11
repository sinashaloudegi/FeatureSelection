package KFST.featureSelection.wrapper.PSO;

/**
 * Created by sina on 7/11/2017.
 */
public class PSOMain {

    Swarm swarm;
    int numItertion;
    int numFeatures;
    int epochs;

    public void start() {
        init();
        run();
    }

    private void run() {
        for (int i = 0; i < numItertion; i++) {
            swarm.calculateFitness();
            swarm.update();

        }
    }

    private void init() {
        swarm = new Swarm( numFeatures,epochs);
        swarm.initialaize();
    }

}
