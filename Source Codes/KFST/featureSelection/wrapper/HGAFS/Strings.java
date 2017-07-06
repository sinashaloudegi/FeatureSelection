package KFST.featureSelection.wrapper.HGAFS;

/**
 * Created by sina on 7/5/2017.
 */
public class Strings {
    double fitness;
    byte[] gene;



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

    public void randomInit(int numFeatures,int numSelecteFeatures) {
        gene=new byte[numFeatures];
        for (int i = 0; i < gene.length; i++) {

        }
    }
}
