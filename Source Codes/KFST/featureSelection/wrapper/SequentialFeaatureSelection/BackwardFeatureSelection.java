package KFST.featureSelection.wrapper.SequentialFeaatureSelection;

import KFST.featureSelection.wrapper.PSO4_2.PSO4_2FitCalculator;

/**
 * Created by sina on 8/5/2017.
 */
public class BackwardFeatureSelection {
    int[] selectedFeatures;
    PSO4_2FitCalculator pso42FitCalculator;
    int numFeatures;

    public BackwardFeatureSelection(PSO4_2FitCalculator pso42FitCalculator, int numFeatures) {
        this.pso42FitCalculator = pso42FitCalculator;
        this.numFeatures = numFeatures;
    }

    private int[] selectBetterSubset(int[] current, int index) throws Exception {
        int[] newSubset = current;
        newSubset[index] = 1;
        if (fit(current) < fit(newSubset)) {
            return newSubset;
        } else {
            return current;
        }

    }

    public double fit(int[] x) throws Exception {
        String s = toString(x);

        return pso42FitCalculator.remove(s);


    }

    public String toString(int[] particle) {
        String s = "";
        for (int i = 0; i < particle.length; i++) {
            if (particle[i] == 0) {
                s += (i + 1) + ",";
            }
        }
        if (s.length() != 0) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public int[] getSelectedFeatures() {
        return selectedFeatures;
    }
}
