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
package KFST.featureSelection.filter;

import KFST.dataset.DatasetInfo;

/**
 * The interface for implementation of the filter-based feature selection
 * methods.
 *
 * @author Sina Tabakhi
 */
public interface FilterApproach {

    /**
     * loads the dataset
     *
     * @param ob an object of the DatasetInfo class
     */
    public void loadDataSet(DatasetInfo ob);

    /**
     * loads the dataset
     *
     * @param data the input dataset values
     * @param numFeat the number of features in the dataset
     * @param numClasses the number of classes in the dataset
     */
    public void loadDataSet(double[][] data, int numFeat, int numClasses);

    /**
     * starts the feature selection process by a given method
     */
    public void evaluateFeatures();

    /**
     * return the subset of features selected by a given method.
     *
     * @return an array of subset of selected features
     */
    public int[] getSelectedFeatureSubset();

    /**
     * return the weights of features if the method gives weights of features
     * individually and ranks them based on their relevance (i.e., feature
     * weighting methods); otherwise, these values does not exist.
     * 
     * @return an array of  weights of features
     */
    public double[] getValues();
}
