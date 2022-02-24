package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit;

public class Geometry {
    private String point;
    private double[] coordinates;

    public Geometry(String point) {
        this.point = point;
        this.coordinates = new double[2];
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
