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
package KFST.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 * This java class is used to create the ARFF file format of the Weka software.
 *
 * @author Sina Tabakhi
 */
public class WekaFileFormat {

    /**
     * This method converts CSV file to ARFF file for the Weka Software
     *
     * @param nameCSV the path of the CSV file
     * @param nameARFF the path for the created ARFF file
     * @param pathProject the path of the project
     * @param sizeFeatureSubset the number of selected features
     * @param ob the object of the DatasetInfo
     */
    public void convertCSVtoARFF(String nameCSV, String nameARFF, String pathProject, int sizeFeatureSubset, DatasetInfo ob) {
        int selectLine = sizeFeatureSubset + 3;
        FileWriter fw = null;
        File tempFile = new File(pathProject + "tempFile.arff");

        String createLabelString = "@attribute " + ob.getNameFeatures()[ob.getNumFeature()] + " {";
        for (int i = 0; i < ob.getNumClass() - 1; i++) {
            createLabelString += ob.getClassLabel()[i] + ",";
        }
        createLabelString += ob.getClassLabel()[ob.getNumClass() - 1] + "}";
        try {
            //load CSV File
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(nameCSV));
            Instances data = loader.getDataSet();
            //load ARFF File
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(tempFile);
            saver.writeBatch();
            //Refine Header ARFF File
            BufferedReader br = new BufferedReader(new FileReader(tempFile));
            fw = new FileWriter(nameARFF, false);
            PrintWriter pw = new PrintWriter(fw);
            while (selectLine-- > 1) {
                pw.println(br.readLine());
            }
            pw.println(createLabelString);
            br.readLine();
            while (br.ready()) {
                pw.println(br.readLine());
            }
            br.close();
            fw.close();
            tempFile.delete();
        } catch (IOException ex) {
            Logger.getLogger(DatasetInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
