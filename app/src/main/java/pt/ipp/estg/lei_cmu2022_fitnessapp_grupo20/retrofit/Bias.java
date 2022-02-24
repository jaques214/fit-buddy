package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit;

public class Bias {
    private final static int SIZE = 2;
    private double[] proximity;

    public Bias() {
        this.proximity = new double[SIZE];
    }

    public double[] getProximity() {
        return proximity;
    }

    public void setProximity(double[] proximity) {
        this.proximity = proximity;
    }
}
