package KFST.featureSelection.wrapper.HGAFS;

/**
 * Created by sina on 7/5/2017.
 */
public class Population {
    private Strings strings[];
    private int p;
    private int f;

    public Population(Strings[] strings, int p, int f) {
        this.strings = strings;
        this.p = p;
        this.f = f;
    }

    public Strings[] getStrings() {
        return strings;
    }

    public void setStrings(Strings[] strings) {
        this.strings = strings;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }
}
