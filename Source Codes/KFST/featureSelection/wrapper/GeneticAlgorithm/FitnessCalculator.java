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

        if (pathTestData != null) {
            File test = new File(pathTestData);
            csvLoader.setSource(test);
            this.test
                    = csvLoader.getDataSet();
        }
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
        Instances tempData = train;
        Remove removeData = new Remove();
        removeData.setInvertSelection(false);
        removeData.setAttributeIndices(s);
        removeData.setInputFormat(tempData);
        Instances instNewData = Filter.useFilter(tempData, removeData);
        instNewData.setClassIndex(instNewData.numAttributes() - 1);

        if (pathTestData == null) {
            return buildAndEval(instNewData);

        } else {
            Instances tempTest = test;
            Remove removeTest = new Remove();
            removeTest.setInvertSelection(false);
            removeTest.setAttributeIndices(s);
            removeTest.setInputFormat(tempTest);
            Instances instNewTest = Filter.useFilter(tempTest, removeTest);
            instNewTest.setClassIndex(instNewTest.numAttributes() - 1);
            return buildAndEval(instNewData, instNewTest);

        }
    }

    private double buildAndEval(Instances train) throws Exception {

        return 0.5;
    }

    private double buildAndEval(Instances train, Instances test) throws Exception {

        return 0.5;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

}
