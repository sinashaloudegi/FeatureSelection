package KFST.featureSelection.wrapper.ABC;

import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by sina on 8/7/2017.
 */
public class ABCFitCalculator {


    String pathTestData;
    String path;
    Instances train;
    Instances test;
    int numFeatures;


    public ABCFitCalculator(String path, String pathTestData) throws IOException {
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

    public double remove(String s) throws Exception {
        Instances tempData = train;
        Remove removeData = new Remove();
        removeData.setInvertSelection(false);
        removeData.setAttributeIndices(s);
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
        IBk knn = new IBk();
        knn.setKNN(1);
        knn.buildClassifier(train);
        Evaluation eval = new Evaluation(train);
        eval.crossValidateModel(knn, train, 10, rand);
        return (1 - eval.errorRate());
    }

    private double buildAndEval(Instances train, Instances test) throws Exception {
        IBk knn = new IBk();
        knn.setKNN(1);
        knn.buildClassifier(train);
        Evaluation eval = new Evaluation(train);
        eval.evaluateModel(knn, test);
        return (1 - eval.errorRate());

    }

    public int getNumFeatures() {
        return numFeatures;
    }

}
