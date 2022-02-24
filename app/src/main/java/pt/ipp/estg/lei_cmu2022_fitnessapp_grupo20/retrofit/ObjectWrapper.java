package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit;

import com.google.gson.annotations.SerializedName;

public class ObjectWrapper {
    private String type;
    private POI[] features;

    public ObjectWrapper(String type, POI[] features) {
        this.type = type;
        this.features = features;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public POI[] getFeatures() {
        return features;
    }

    public void setFeatures(POI[] features) {
        this.features = features;
    }
}
