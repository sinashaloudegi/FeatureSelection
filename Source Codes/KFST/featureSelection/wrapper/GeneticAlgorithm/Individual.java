package KFST.featureSelection.wrapper.GeneticAlgorithm;

/**
 * Created by sina on 6/2/2017.
 */
public class Individual {
    double fitness;
    byte[] gene;


    public Individual() {

    }


    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public byte[] getGene() {
        return gene;
    }

    public void setGene(byte[] gene) {
        this.gene = gene;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < gene.length; i++) {
            if (gene[i] == 0) {
                s += (i + 1) + ",";
            }
        }
        if (s.length() != 0) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }


    public Individual randomIndividual(int numFeatures) {
        byte[] b = randomByte(numFeatures);
        this.setGene(b);

        return this;
    }

    private byte[] randomByte(int numFeatures) {
        byte[] temp = new byte[numFeatures];
        for (int i = 0; i < numFeatures; i++) {
            double rand = Math.random();
            if (rand > 0.5 & i != numFeatures - 1) {
                temp[i] = 1;
            } else {
                temp[i] = 0;
            }
        }
        return temp;
    }


}
