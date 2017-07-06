package KFST.featureSelection.wrapper.HGAFS;


import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;

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

    public Population fit(Population p) {
        return p;
    }
}
