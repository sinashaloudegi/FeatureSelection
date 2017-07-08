package KFST.featureSelection.wrapper.HGAFS;


import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by sina on 7/6/2017.
 */
public class FitCalculator {

    String pathTestData;
    String path;
    Instances train;
    Instances test;
    int numFeatures;

    public FitCalculator(String path, String pathTestData) throws IOException {
        this.path = path;
        this.pathTestData = pathTestData;
        CSVLoader csvLoader = new CSVLoader();
        File data = new File(path);
        csvLoader.setSource(data);
        this.train = csvLoader.getDataSet();
        train.setClassIndex(train.numAttributes() - 1);


        if (pathTestData != null) {
            File testData = new File(pathTestData);
            csvLoader.setSource(testData);
            this.test = csvLoader.getDataSet();
            test.setClassIndex(test.numAttributes() - 1);

        }
        this.numFeatures = train.numAttributes();


    }

    public Instances getTrain() {
        return train;
    }

    public Population fit(Population p) throws Exception {
        int len = 0;
        try {
            len = p.strings.length;

        } catch (Exception e) {
            len = 0;
        }
        for (int i = 0; i < len; i++) {
            double f = remove(p.strings[i].toString());
            p.strings[i].setFitness(f);
        }
        return p;
    }

    private double remove(String s) throws Exception {
        Instances tempData = train;
        Remove removeData = new Remove();
        removeData.setInvertSelection(false);
        removeData.setAttributeIndices(s);
        System.out.println("s ;" + s);
        removeData.setInputFormat(tempData);
        Instances instNewData = Filter.useFilter(tempData, removeData);
        if (pathTestData == null) {

            return buildAndEval(instNewData);

        } else {
            Instances tempTest = test;
            Remove removeTest = new Remove();
            removeTest.setInvertSelection(false);
            removeTest.setAttributeIndices(s);
            removeTest.setInputFormat(tempTest);
            Instances instNewTest = Filter.useFilter(tempTest, removeTest);
            return buildAndEval(instNewData, instNewTest);

        }
    }


    private double buildAndEval(Instances train) throws Exception {
        Random rand = new Random(1);
        MultilayerPerceptron nn = new MultilayerPerceptron();
        nn.buildClassifier(train);
        Evaluation eval = new Evaluation(train);
        eval.crossValidateModel(nn, train, 10, rand);
        int sumOfCor = LocalSearchOperation.getSumOfCor();
        return (double) (1 - eval.errorRate()) / sumOfCor;
    }

    private double buildAndEval(Instances train, Instances test) throws Exception {
        MultilayerPerceptron nn = new MultilayerPerceptron();
        nn.buildClassifier(train);
        Evaluation eval = new Evaluation(train);
        eval.evaluateModel(nn, test);
        int sumOfCor = LocalSearchOperation.getSumOfCor();
        return (double) (1 - eval.errorRate()) / sumOfCor;

    }

    public int getNumFeatures() {
        return numFeatures;
    }
}
