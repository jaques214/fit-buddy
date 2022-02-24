package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit;

public class Coor {
    private double lon;
    private double lat;

    public Coor(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
