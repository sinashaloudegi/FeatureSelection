package KFST.featureSelection.wrapper.SequentialFeaatureSelection;

import KFST.featureSelection.wrapper.PSO4_2.PSO4_2FitCalculator;

/**
 * Created by sina on 8/5/2017.
 */
public class BackwardFeatureSelection {
    int[] selectedFeatures;
    PSO4_2FitCalculator pso42FitCalculator;
    public BackwardFeatureSelection(PSO4_2FitCalculator pso42FitCalculator) {
        this.pso42FitCalculator=pso42FitCalculator;
    }

    public int[] getSelectedFeatures() {
        return selectedFeatures;
    }
}
