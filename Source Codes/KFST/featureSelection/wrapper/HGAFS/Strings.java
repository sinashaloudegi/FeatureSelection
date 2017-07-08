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
        System.out.println("num" + numSelectedFeatures + " numfeatures " + numFeatures);
        double threshold = (double) numSelectedFeatures / numFeatures;
        while (temp != numSelectedFeatures) {
            System.out.println("temp=" + temp + " num " + numSelectedFeatures);
            temp = 0;
            for (int i = 0; i < gene.length; i++) {
                System.out.println(randomGene() + " <= => " + threshold);
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
    public int[] getOnes() {
        int[] temp = new int[numOfOnes()];
        int j = 0;
        for (int i = 0; i < gene.length; i++) {
            if (gene[i] == 1) {
                temp[j] = i;
                j++;
            }

        }
        return temp;
    }

    private int numOfOnes() {
        int counter = 0;
        for (int i = 0; i < gene.length; i++) {
            if (gene[i] == 1) {
                counter++;
            }
        }
        return counter;

    }

    private double randomGene() {
        Random rand = new Random();

        return rand.nextDouble();
    }
}
