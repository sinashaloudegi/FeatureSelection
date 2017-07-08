package KFST.featureSelection.wrapper.HGAFS;

import java.util.ArrayList;

/**
 * Created by sina on 7/5/2017.
 */
public class Population {
    public Strings strings[];
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
        System.out.println("47pOPULATIO");
        for (int i = 0; i < strings.length; i++) {
            strings[i] = new Strings();
            strings[i].randomInit(numFeatures, numSelectedFeatures);

        }
        setStrings(strings);
        System.out.println("54pOPULATIO");

    }

    public Strings best() {
        double max = Double.MIN_VALUE;
        Strings best = null;
        for (int i = 0; i < this.strings.length; i++) {
            if (this.strings[i].fitness > max) {
                max = this.strings[i].fitness;
                best = this.strings[i];
            }

        }
        return best;
    }

    public void replacement(ArrayList<Strings> childList) {
        Strings[] p = this.getStrings();
        int rep = p.length - childList.size();
        for (int i = 0; i < rep; i++) {
            childList.add(p[i]);
        }
        p = childList.toArray(p);

        this.setStrings(p);

    }

    public void sort() {
        System.out.println("sort");

        Strings[] strings = this.getStrings();
        ArrayList<Strings> temp = new ArrayList<Strings>();
        ArrayList<Strings> res = new ArrayList<Strings>();
        for (int i = 0; i < strings.length; i++) {
            temp.add(strings[i]);
        }
        while (!temp.isEmpty()) {
            double max = 0;
            int maxStrings = 0;
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).fitness >= max) {
                    max = temp.get(i).fitness;
                    maxStrings = i;
                }
            }
            res.add(temp.get(maxStrings));
            temp.remove(maxStrings);
        }

        strings = res.toArray(strings);

        this.setStrings(strings);
        System.out.println("sortrdd");

    }
}
