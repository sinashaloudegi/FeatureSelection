package KFST.featureSelection.wrapper.HGAFS;

import java.util.Random;

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

    public void randomInit(int numFeatures, int numSelectedFeatures) {
        gene = new byte[numFeatures];
        int temp = 0;
        double threshold = numSelectedFeatures / numFeatures;
        while (temp != numSelectedFeatures) {
            temp = 0;
            for (int i = 0; i < gene.length; i++) {
                if (randomGene() < threshold) {
                    gene[i] = 1;
                    temp++;
                } else {
                    gene[i] = 0;
                }
            }
        }
        for (int i = 0; i < gene.length; i++) {
            System.out.print(gene[i] + ",");
        }
    }

    private double randomGene() {
        Random rand = new Random();

        return rand.nextDouble();
    }
}
