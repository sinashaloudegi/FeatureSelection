package KFST.featureSelection.wrapper.SequentialFeaatureSelection;

import KFST.featureSelection.wrapper.PSO4_2.PSO4_2FitCalculator;

/**
 * Created by sina on 8/5/2017.
 */
public class ForwardFeatureSelection {
    int[] selectedFeatures;
    PSO4_2FitCalculator pso42FitCalculator;

    public ForwardFeatureSelection(PSO4_2FitCalculator pso42FitCalculator) {
        this.pso42FitCalculator = pso42FitCalculator;
    }


    public int[] getSelectedFeatures() {
        return selectedFeatures;
    }
}
