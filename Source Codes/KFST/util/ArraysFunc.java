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

import java.util.Arrays;

/**
 * This java class is used to implement various utility methods for manipulating
 * arrays and matrices. The methods in this class is contained brief description
 * of the applications.
 *
 * @author Sina Tabakhi
 */
public final class ArraysFunc {

    /**
     * creates a copy of the two dimensional array
     *
     * @param initArray initial array
     *
     * @return a copy array of the input array
     */
    public static double[][] copyDoubleArray2D(double[][] initArray) {
        double[][] copyArray = new double[initArray.length][initArray[0].length];
        for (int i = 0; i < initArray.length; i++) {
            copyArray[i] = Arrays.copyOf(initArray[i], initArray[i].length);
        }
        return copyArray;
    }

    /**
     * sorts the two dimensional array by using the index of column as ascending
     * order
     *
     * @param array the input array
     * @param col the index of the column
     */
    public static void sortArray2D(double[][] array, int col) {
        int i, j;

        for (i = 1; i < array.length; i++) {
            double[] next = Arrays.copyOf(array[i], array[i].length);
            for (j = i - 1; j >= 0 && (next[col] < array[j][col]); j--) {
                array[j + 1] = Arrays.copyOf(array[j], array[j].length);
            }
            array[j + 1] = Arrays.copyOf(next, next.length);
        }
    }

    /**
     * sorts the two dimensional array by using the index of column as ascending
     * order
     *
     * @param array the input array
     * @param col the index of the column
     * @param indexStart the start index of the data
     * @param indexEnd the end index of the data
     */
    public static void sortArray2D(double[][] array, int col, int indexStart, int indexEnd) {
        int i, j;

        for (i = indexStart + 1; i < indexEnd; i++) {
            double[] next = Arrays.copyOf(array[i], array[i].length);
            for (j = i - 1; j >= indexStart && (next[col] < array[j][col]); j--) {
                array[j + 1] = Arrays.copyOf(array[j], array[j].length);
            }
            array[j + 1] = Arrays.copyOf(next, next.length);
        }
    }

    /**
     * sorts the one dimensional array (double values) as descending or
     * ascending order
     *
     * @param array the input array
     * @param descending indicates the type of sorting
     */
    public static void sortArray1D(double[] array, boolean descending) {
        Arrays.sort(array);
        if (descending) {
            double[] tempArray = Arrays.copyOfRange(array, 0, array.length);
            for (int i = 0, j = array.length - 1; i < array.length; i++, j--) {
                array[i] = tempArray[j];
            }
        }
    }

    /**
     * sorts the one dimensional array (integer values) as descending or
     * ascending order
     *
     * @param array the input array
     * @param descending indicates the type of sorting
     */
    public static void sortArray1D(int[] array, boolean descending) {
        Arrays.sort(array);
        if (descending) {
            int[] tempArray = Arrays.copyOfRange(array, 0, array.length);
            for (int i = 0, j = array.length - 1; i < array.length; i++, j--) {
                array[i] = tempArray[j];
            }
        }
    }

    /**
     * sorts the one dimensional array (double values) by values and returns a
     * list of indeces
     *
     * @param array the input array
     * @param descending indicates the type of sorting
     *
     * @return the sorted array
     */
    public static int[] sortWithIndex(double[] array, boolean descending) {
        int[] index = new int[array.length];
        int i, j;
        for (i = 0; i < index.length; i++) {
            index[i] = i;
        }

        if (descending) {
            for (i = 1; i < array.length; i++) {
                double next = array[i];
                int nextIndex = index[i];
                for (j = i - 1; j >= 0 && (next > array[j]); j--) {
                    array[j + 1] = array[j];
                    index[j + 1] = index[j];
                }
                array[j + 1] = next;
                index[j + 1] = nextIndex;
            }
        } else {
            for (i = 1; i < array.length; i++) {
                double next = array[i];
                int nextIndex = index[i];
                for (j = i - 1; j >= 0 && (next < array[j]); j--) {
                    array[j + 1] = array[j];
                    index[j + 1] = index[j];
                }
                array[j + 1] = next;
                index[j + 1] = nextIndex;
            }
        }
        return index;
    }

    /**
     * sorts the one dimensional array (integer values) by values and returns a
     * list of indeces
     *
     * @param array the input array
     * @param descending indicates the type of sorting
     *
     * @return the sorted array
     */
    public static int[] sortWithIndex(int[] array, boolean descending) {
        int[] index = new int[array.length];
        int i, j;
        for (i = 0; i < index.length; i++) {
            index[i] = i;
        }

        if (descending) {
            for (i = 1; i < array.length; i++) {
                int next = array[i];
                int nextIndex = index[i];
                for (j = i - 1; j >= 0 && (next > array[j]); j--) {
                    array[j + 1] = array[j];
                    index[j + 1] = index[j];
                }
                array[j + 1] = next;
                index[j + 1] = nextIndex;
            }
        } else {
            for (i = 1; i < array.length; i++) {
                int next = array[i];
                int nextIndex = index[i];
                for (j = i - 1; j >= 0 && (next < array[j]); j--) {
                    array[j + 1] = array[j];
                    index[j + 1] = index[j];
                }
                array[j + 1] = next;
                index[j + 1] = nextIndex;
            }
        }
        return index;
    }

    /**
     * converts the string input to double values
     * 
     * @param array the string input array
     * 
     * @return the double array obtained from input array
     */
    public static double[][] convertStringToDouble(String[][] array) {
        double[][] results = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                results[i][j] = Double.parseDouble(array[i][j]);
            }
        }
        return results;
    }
}
