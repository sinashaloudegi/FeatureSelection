package KFST.featureSelection.wrapper.GeneticAlgorithm;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.io.IOException;

/**
 * Created by sina on 6/2/2017.
 */
public class FitnessCalculator {

    String pathTestData;
    String path;
    Instances train;
    Instances test;
    int numFeatures;
    String classifier;


    public FitnessCalculator(String classifier, String path, String pathTestData) throws IOException {
        this.classifier = classifier;
        this.path = path;
        this.pathTestData = pathTestData;
        CSVLoader csvLoader = new CSVLoader();
        File data = new File(path);
        csvLoader.setSource(data);
        this.train = csvLoader.getDataSet();

        System.out.println("train:" + path);
        System.out.println("test:" + pathTestData);
        this.numFeatures = train.numAttributes();
    }

    public Population fitness(Population p) throws Exception {
        int len = 0;
        try {
            len = p.individuals.length;

        } catch (Exception e) {
            len = 0;
        }
        for (int i = 0; i < len; i++) {
            double f = remove(p.individuals[i].toString());
            p.individuals[i].setFitness(f);
        }
        return p;
    }

    private double remove(String s) throws Exception {
        Instances temp = train;
        Remove remove = new Remove();
        remove.setInvertSelection(false);
        remove.setAttributeIndices(s);
        remove.setInputFormat(temp);
        Instances instNew = Filter.useFilter(temp, remove);
        instNew.setClassIndex(instNew.numAttributes() - 1);
        return buildAndEval(instNew);
    }

    private double buildAndEval(Instances train) throws Exception {

        return 0.5;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

}
