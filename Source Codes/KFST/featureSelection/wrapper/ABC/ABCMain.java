package KFST.featureSelection.wrapper.ABC;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.WrapperApproach;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sina on 8/7/2017.
 */
public class ABCMain implements WrapperApproach {

    int numSelectedFeatures;
    int numFeatures;
    int numFoodSource;
    int maxLimit;
    int[] selecteFeatureSubset;
    double MR;
    int numIteration;
    String pathData;
    String pathTestData;
    List<FoodSource> foodSources;

    public ABCMain(int numSelectedFeatures, int maxLimit, double MR, int numIteration) {
        this.maxLimit = maxLimit;
        this.MR = MR;
        this.numIteration = numIteration;
        this.numSelectedFeatures = numSelectedFeatures;
    }

    private void init() throws Exception {
        foodSources = new ArrayList<FoodSource>();
        for (int i = 0; i < numFeatures; i++) {
            FoodSource foodSource = new FoodSource(numFeatures);
            foodSource.initialize(i);
            foodSource.calculateFitness();
            foodSources.add(foodSource);

        }
    }

    @Override
    public void loadDataSet(DatasetInfo ob) {
        numFeatures = ob.getNumFeature();
        numFoodSource = numFeatures;
        pathData = ob.getPathData();
        pathTestData = ob.getPathTestSet();
    }

    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {

    }

    @Override
    public void evaluateFeatures() throws Exception {

        init();

        for (int i = 0; i < numFeatures; i++) {
            for (int j = 0; j < numFeatures; j++) {
                System.out.print(foodSources.get(i).x[j] + ",");

            }
            System.out.println("________");
        }
    }

    @Override
    public int[] getSelectedFeatureSubset() {
        return selecteFeatureSubset;
    }

    @Override
    public double[] getValues() {
        return new double[0];
    }
}
