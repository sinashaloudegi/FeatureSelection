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
package KFST.util;

/**
 * This java class is used to implement various utility methods for performing
 * basic statistical operation such as the mean and variance.
 * <p>
 * Also, some numeric operation on the matrices are provides such as multiple
 * of two matrices, subtract of two matrices, and transpose of the matrix.
 * <p>
 * The methods in this class is contained brief description of the applications.
 *
 * @author Sina Tabakhi
 */
public final class MathFunc {

    /**
     * returns the base 2 logarithm of a double value
     *
     * @param x a value
     * 
     * @return the based 2 logarithm of x
     */
    public static double log2(double x) {
        if (x == 0) {
            return 0;
        } else {
            return Math.log(x) / Math.log(2);
        }
    }

    /**
     * rounds the argument value to a double value with given number of
     * floating-point
     *
     * @param num a floating-point value
     * @param numFloatPoint the number of floating-point of the num
     * 
     * @return the string value of the argument rounded with given number of
     * floating-point
     */
    public static String roundDouble(double num, int numFloatPoint) {
        String precision = "%." + String.valueOf(numFloatPoint) + "f";
        return String.format(precision, num);
    }

    /**
     * computes the mean value of the data corresponding to a given column
     * (column index)
     *
     * @param data the input data
     * @param index the index of the given column
     * 
     * @return the mean value of the data
     */
    public static double computeMean(double[][] data, int index) {
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i][index];
        }
        return sum / data.length;
    }

    /**
     * calculates the average values of all columns
     *
     * @param array the input array
     * 
     * @return the average values of columns of the input array
     */
    public static double[][] computeAverageArray(double[][] array) {
        double[][] tempAverage = new double[1][array[0].length];
        for (int j = 0; j < tempAverage[0].length; j++) {
            for (int i = 0; i < array.length; i++) {
                tempAverage[0][j] += array[i][j];
            }
            tempAverage[0][j] = Double.parseDouble(MathFunc.roundDouble(tempAverage[0][j] / array.length, 3));
        }
        return tempAverage;
    }

    /**
     * computes the variance value of the data corresponding to a given column
     * (column index)
     *
     * @param data the input data
     * @param mean the mean value of the data corresponding to a given column
     * @param index the index of the given column
     * 
     * @return the variance value of the data
     */
    public static double computeVariance(double[][] data, double mean, int index) {
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += Math.pow(data[i][index] - mean, 2);
        }
        return sum / data.length;
    }

    /**
     * computes the similarity value between two features using cosine
     * similarity
     *
     * @param data the input data
     * @param index1 the index of the first feature
     * @param index2 the index of the second feature
     * 
     * @return the similarity value
     */
    public static double computeSimilarity(double[][] data, int index1, int index2) {
        double sum1 = 0.0;
        double sum2 = 0.0;
        double sum3 = 0.0;

        for (int i = 0; i < data.length; i++) {
            sum1 += (data[i][index1] * data[i][index2]);
            sum2 += (data[i][index1] * data[i][index1]);
            sum3 += (data[i][index2] * data[i][index2]);
        }

        if (sum2 == 0 && sum3 == 0) {
            return 1;
        } else if (sum2 == 0 || sum3 == 0) {
            return 0;
        } else {
            return sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
        }
    }

    /**
     * calculates the error rate values based on the array of accuracies
     *
     * @param accuracy the input array from accuracies
     * 
     * @return the error rate values
     */
    public static double[][] computeErrorRate(double[][] accuracy) {
        double[][] errorArray = new double[accuracy.length][accuracy[0].length];
        for (int i = 0; i < errorArray.length; i++) {
            for (int j = 0; j < errorArray[i].length; j++) {
                errorArray[i][j] = Double.parseDouble(MathFunc.roundDouble(100 - accuracy[i][j], 3));
            }
        }
        return errorArray;
    }

    /**
     * computes the multiple of two matrices
     *
     * @param matrix1 the first input matrix
     * @param matrix2 the second input matrix
     * 
     * @return a matrix based on the result of the multiple of two matrices
     */
    public static double[][] multMatrix(double[][] matrix1, double[][] matrix2) {
        int row1 = matrix1.length;
        int col1 = matrix1[0].length;
        int col2 = matrix2[0].length;
        double[][] multipleMatrix = new double[row1][col2];

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                multipleMatrix[i][j] = 0;
                for (int k = 0; k < col1; k++) {
                    multipleMatrix[i][j] += (matrix1[i][k] * matrix2[k][j]);
                }
            }
        }
        return multipleMatrix;
    }

    /**
     * computes the subtract of two matrices
     *
     * @param matrix1 the first input matrix
     * @param matrix2 the second input matrix
     * 
     * @return a matrix based on the result of the subtract of two matrices
     */
    public static double[][] subMatrix(double[][] matrix1, double[][] matrix2) {
        double[][] subtractMatrix = new double[matrix1.length][matrix1[0].length];

        for (int i = 0; i < subtractMatrix.length; i++) {
            for (int j = 0; j < subtractMatrix[i].length; j++) {
                subtractMatrix[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }

        return subtractMatrix;
    }

    /**
     * computes the transpose of the input matrix
     *
     * @param matrix the input matrix
     * 
     * @return a matrix based on the result of the transpose of the matrix
     */
    public static double[][] transMatrix(double[][] matrix) {
        double transposeMatrix[][] = new double[matrix[0].length][matrix.length];

        for (int i = 0; i < transposeMatrix.length; i++) {
            for (int j = 0; j < transposeMatrix[i].length; j++) {
                transposeMatrix[i][j] = matrix[j][i];
            }
        }

        return transposeMatrix;
    }
}
