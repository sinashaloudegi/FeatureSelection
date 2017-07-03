package KFST.featureSelection.wrapper.GeneticAlgorithm;

import weka.core.Instances;

import java.util.Random;

/**
 * Created by sina on 6/2/2017.
 */
public class Population {

    Individual individuals[];
    int numPopulation;
    private int numFeatures;
    Instances data;
    int[] S;
    int[] D;
    String type;

    public Population(int numPopulation, int numFeatures, int[] S, int[] D, String type) {

        this.numPopulation = numPopulation;
        individuals = new Individual[numPopulation];
        this.numFeatures = numFeatures;
        this.type = type;
        if (type.equals("HGAFS")) {
            this.S = S;
            this.D = D;
        }


    }


    public Individual[] getIndividuals() {
        return individuals;
    }

    public void setIndividuals(Individual[] individuals) {
        this.individuals = individuals;
    }

    public void init(int numSelectedFeature) {

        for (int i = 0; i < numPopulation; i++) {
            individuals[i] = new Individual();

            Individual insInstance = new Individual();
            this.individuals[i] = insInstance.randomIndividual(numFeatures);

        }

        refineNumOfOnes(numSelectedFeature);


    }

    public void refineNumOfOnes(int numSelectedFeature) {

        for (int i = 0; i < individuals.length; i++) {
            int ones = numOfOnes(this.individuals[i].getGene());
            if (ones != numSelectedFeature) {
                if (type.equals("HGAFS")) {
                    refineHGAFS(i, numSelectedFeature, ones);

                } else {
                    refine(i, numSelectedFeature, ones);

                }

            }
        }
    }


    private void refine(int i, int numSelectedFeatures, int ones) {

        if (ones > numSelectedFeatures) {


            int temp = ones;
            while (temp != numSelectedFeatures) {
                Random rand = new Random();
                int rnd = rand.nextInt(numFeatures);
                if (this.individuals[i].gene[rnd] == 1) {
                    this.individuals[i].gene[rnd] = 0;
                    temp--;
                }

            }

        } else if (ones < numSelectedFeatures) {
            int temp = numSelectedFeatures;
            while (temp != ones) {
                Random rand = new Random();
                int rnd = rand.nextInt(numFeatures);
                if (this.individuals[i].gene[rnd] == 0) {
                    this.individuals[i].gene[rnd] = 1;
                    temp--;
                }

            }
        }


    }

    private void refineHGAFS(int i, int numSelectedFeatures, int ones) {
//TODO HGAFS for refine
        int j = 0, k = 0;
        if (ones > numSelectedFeatures & j < S.length) {


            int temp = ones;

            while (temp != numSelectedFeatures & j < S.length) {

                if (this.individuals[i].gene[j] == 1) {
                    this.individuals[i].gene[j] = 0;
                    temp--;

                }
                j++;

            }

        } else if (ones < numSelectedFeatures & k < D.length) {
            int temp = numSelectedFeatures;
            while (temp != ones & k < D.length) {

                if (this.individuals[i].gene[k] == 0) {
                    this.individuals[i].gene[k] = 1;
                    temp--;
                }
                k++;

            }
        }

        if (ones > numSelectedFeatures & j >= S.length) {


            int temp = ones;
            int m = 0;
            while (temp != numSelectedFeatures) {

                if (this.individuals[i].gene[m] == 1) {
                    this.individuals[i].gene[m] = 0;
                    temp--;

                }
                m++;

            }

        } else if (ones < numSelectedFeatures & k >= D.length) {
            int temp = numSelectedFeatures;
            int n = 0;
            while (temp != ones) {

                if (this.individuals[i].gene[n] == 0) {
                    this.individuals[i].gene[n] = 1;
                    temp--;
                }
                n++;

            }
        }
    }


    private int numOfOnes(byte[] gene) {
        int counter = 0;
        for (int i = 0; i < gene.length; i++) {
            if (gene[i] == 1) {
                counter++;
            }
        }
        return counter;

    }

    public int getNumPopulation() {
        return numPopulation;
    }

    public void setNumPopulation(int numPopulation) {
        this.numPopulation = numPopulation;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

    public void setNumFeatures(int numFeatures) {
        this.numFeatures = numFeatures;
    }

    public Individual best() {
        double max = Double.MIN_VALUE;
        Individual best = null;
        for (int i = 0; i < this.individuals.length; i++) {
            if (this.individuals[i].fitness > max) {
                max = this.individuals[i].fitness;
                best = this.individuals[i];
            }

        }
        return best;
    }
}