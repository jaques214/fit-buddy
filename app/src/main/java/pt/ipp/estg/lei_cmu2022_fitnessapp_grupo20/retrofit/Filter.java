package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit;

public class Filter {
    private final static int SIZE = 3;
    private double[] circle;

    public Filter() {
        this.circle = new double[SIZE];
    }

    public double[] getCircle() {
        return circle;
    }

    public void setCircle(double[] circle) {
        this.circle = circle;
    }
}
