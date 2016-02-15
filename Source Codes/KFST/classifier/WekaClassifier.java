/*
 * Kurdistan Feature Selection Tool (KFST) is an open-source tool, developed
 * completely in Java, for performing feature selection process in different
 * areas of research.
 * For more information about KFST, please visit:
 *     http://kfst.uok.ac.ir/index.html
 *
 * Copyright (C) 2016 KFST development team at University of Kurdistan,
 * Sanandaj, Iran.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package KFST.classifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 * This java class is used to apply the classifiers for computing the
 * performance of the feature selection methods. The classifiers have been
 * implemented as the Weka software.
 *
 * @author Sina Tabakhi
 */
public class WekaClassifier {

    /**
     * This method builds and evaluates the support vector machine(SVM)
     * classifier. The SMO are used as the SVM classifier implemented in the
     * Weka software.
     *
     * @param pathTrainData the path of the train set
     * @param pathTestData the path of the test set
     * @param svmKernel the kernel to use
     * 
     * @return the classification accuracy
     */
    public static double SVM(String pathTrainData, String pathTestData, String svmKernel) {
        double resultValue = 0;
        try {
            BufferedReader readerTrain = new BufferedReader(new FileReader(pathTrainData));
            Instances dataTrain = new Instances(readerTrain);
            readerTrain.close();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

            BufferedReader readerTest = new BufferedReader(new FileReader(pathTestData));
            Instances dataTest = new Instances(readerTest);
            readerTest.close();
            dataTest.setClassIndex(dataTest.numAttributes() - 1);
            SMO svm = new SMO();
            if (svmKernel.equals("Polynomial kernel")) {
                svm.setKernel(weka.classifiers.functions.supportVector.PolyKernel.class.newInstance());
            } else if (svmKernel.equals("RBF kernel")) {
                svm.setKernel(weka.classifiers.functions.supportVector.RBFKernel.class.newInstance());
            } else {
                svm.setKernel(weka.classifiers.functions.supportVector.Puk.class.newInstance());
            }
            svm.buildClassifier(dataTrain);
            Evaluation eval = new Evaluation(dataTest);
            eval.evaluateModel(svm, dataTest);
            resultValue = 100 - (eval.errorRate() * 100);
        } catch (Exception ex) {
            Logger.getLogger(WekaClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultValue;
    }

    /**
     * This method builds and evaluates the naiveBayes(NB) classifier.
     * The naiveBayes are used as the NB classifier implemented in the Weka
     * software.
     *
     * @param pathTrainData the path of the train set
     * @param pathTestData the path of the test set
     * 
     * @return the classification accuracy
     */
    public static double naiveBayes(String pathTrainData, String pathTestData) {
        double resultValue = 0;
        try {
            BufferedReader readerTrain = new BufferedReader(new FileReader(pathTrainData));
            Instances dataTrain = new Instances(readerTrain);
            readerTrain.close();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

            BufferedReader readerTest = new BufferedReader(new FileReader(pathTestData));
            Instances dataTest = new Instances(readerTest);
            readerTest.close();
            dataTest.setClassIndex(dataTest.numAttributes() - 1);

            NaiveBayes nb = new NaiveBayes();
            nb.buildClassifier(dataTrain);
            Evaluation eval = new Evaluation(dataTest);
            eval.evaluateModel(nb, dataTest);
            resultValue = 100 - (eval.errorRate() * 100);
        } catch (Exception ex) {
            Logger.getLogger(WekaClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultValue;
    }

    /**
     * This method builds and evaluates the decision tree(DT) classifier.
     * The j48 are used as the DT classifier implemented in the Weka software.
     *
     * @param pathTrainData the path of the train set
     * @param pathTestData the path of the test set
     * @param confidenceValue The confidence factor used for pruning
     * @param minNumSampleInLeaf The minimum number of instances per leaf
     * 
     * @return the classification accuracy
     */
    public static double dTree(String pathTrainData, String pathTestData, double confidenceValue, int minNumSampleInLeaf) {
        double resultValue = 0;
        try {
            BufferedReader readerTrain = new BufferedReader(new FileReader(pathTrainData));
            Instances dataTrain = new Instances(readerTrain);
            readerTrain.close();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

            BufferedReader readerTest = new BufferedReader(new FileReader(pathTestData));
            Instances dataTest = new Instances(readerTest);
            readerTest.close();
            dataTest.setClassIndex(dataTest.numAttributes() - 1);

            J48 decisionTree = new J48();
            decisionTree.setConfidenceFactor((float) confidenceValue);
            decisionTree.setMinNumObj(minNumSampleInLeaf);
            decisionTree.buildClassifier(dataTrain);
            Evaluation eval = new Evaluation(dataTest);
            eval.evaluateModel(decisionTree, dataTest);
            resultValue = 100 - (eval.errorRate() * 100);
        } catch (Exception ex) {
            Logger.getLogger(WekaClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultValue;
    }
}
