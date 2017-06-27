package KFST.featureSelection.wrapper.GeneticAlgorithm;

import KFST.classifier.WekaClassifier;
import KFST.dataset.DatasetInfo;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sina on 6/2/2017.
 */
public class FitnessCalculator {

    String path;
    Instances train;
    int numFeatures;
    String classifier;


    public FitnessCalculator(String classifier,String path) throws IOException {
        this.classifier = classifier;
        this.path=path;
        CSVLoader csvLoader=new CSVLoader();
        File f=new File(path);
        csvLoader.setSource(f);
        this.train = csvLoader.getDataSet();
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

    /*    double result;
        if (classifier.equals("Support Vector Machine (SVM)")) {
            result = WekaClassifier.SVM(path, pathTestData, "");
        } else if (classifier.equals("Naive Bayes (NB)")) {
            result = WekaClassifier.naiveBayes(path, pathTestData);

        } else if (classifier.equals("Decision Tree (C4.5)")) {
            result = WekaClassifier.dTree(path, pathTestData, 0, 0);

        }*/

        return 0.5;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

}
