package KFST.featureSelection.wrapper.SequentialFeaatureSelection;

import KFST.featureSelection.wrapper.PSO4_2.PSO4_2FitCalculator;

import java.util.Random;

/**
 * Created by sina on 8/5/2017.
 */
public class ForwardFeatureSelection {
    int[] selectedFeatures;
    PSO4_2FitCalculator pso42FitCalculator;
    int numFeatures;

    public ForwardFeatureSelection(PSO4_2FitCalculator pso42FitCalculator, int numFeatures) {
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

    public void selectSubset() throws Exception {
        int counter=0;
        selectedFeatures= new int[numFeatures];
        int[] check=new int[numFeatures];
        for (int i=0;i<check.length;i++){
            check[i]=0;
            selectedFeatures[i]=0;
        }
        Random rand=new Random();
        while (counter!=numFeatures){
            System.out.println("c: "+counter);
          int r=(int) (rand.nextDouble()*(numFeatures));
            System.out.println("r: "+r);
            if(r==numFeatures){
                r=numFeatures-1;
            }
            if(check[r]==0){
                counter++;
                check[r]=1;
                selectedFeatures=selectBetterSubset(selectedFeatures,r);

            }
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
        try {
            selectSubset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedFeatures;
    }


}
