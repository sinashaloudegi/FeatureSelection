package KFST.featureSelection.wrapper.HGAFS;

import java.util.ArrayList;

/**
 * Created by sina on 7/5/2017.
 */
public class Population {
    private Strings strings[];
    private int numPopulation;
    private int numFeatures;
    private int numSelectedFeatures;

    public Population(int numPopulation, int numFeatures, int numSelectedFeatures) {
        this.numPopulation = numPopulation;
        this.numFeatures = numFeatures;
        this.numSelectedFeatures = numSelectedFeatures;

    }

    public Strings[] getStrings() {
        return strings;
    }

    public void setStrings(Strings[] strings) {
        this.strings = strings;
    }

    public int getNumPopulation() {
        return numPopulation;
    }

    public void setNumPopulation(int numPopulation) {
        this.numPopulation = numPopulation;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

    public void setNumFeatures(int numFeatures) {
        this.numFeatures = numFeatures;
    }

    public void initPopulation() {
        Strings[] strings = new Strings[numFeatures];
        for (int i = 0; i < strings.length; i++) {
            strings[i].randomInit(numFeatures,numSelectedFeatures);

        }
    }

    public void replacement(ArrayList<Strings> childList) {
        
    }

    public void sort() {
        Strings[] p=this.getStrings();
        ArrayList<Strings> temp=  new ArrayList<Strings>();
        ArrayList<Strings> res=  new ArrayList<Strings>();
        for (int i=0;i<p.length;i++){
            temp.add(p[i]);
        }
        while (!temp.isEmpty()){
            double max=0;
            int maxStrings=0;
            for (int i=0;i<temp.size();i++){
                if(temp.get(i).fitness>max){
                    max=temp.get(i).fitness;
                    maxStrings=i;
                }
            }
            res.add(temp.get(maxStrings));
            temp.remove(maxStrings);
        }

        p=res.toArray(p);

    }
}
