package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit;

public class POI {
    private String type;
    private Properties properties;
    private Geometry geometry;

    public POI() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
