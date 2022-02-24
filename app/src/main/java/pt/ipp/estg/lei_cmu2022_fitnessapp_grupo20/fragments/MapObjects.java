package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments;

import com.here.sdk.core.Color;
import com.here.sdk.core.GeoCircle;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.GeoPolygon;
import com.here.sdk.mapview.MapCamera;
import com.here.sdk.mapview.MapPolygon;
import com.here.sdk.mapview.MapScene;
import com.here.sdk.mapview.MapView;

public class MapObjects {
    private static final double DISTANCE_IN_METERS = 50000;
    private final MapScene mapScene;
    private final MapCamera mapCamera;
    private MapPolygon mapCircle;

    public MapObjects(MapView mapView) {
        mapScene = mapView.getMapScene();
        mapCamera = mapView.getCamera();
    }

    public void showMapCircle(GeoCoordinates geoCoordinates) {
        clearMapCircle();
        // Move map to expected location.
        mapCamera.flyTo(geoCoordinates, DISTANCE_IN_METERS, new MapCamera.FlyToOptions());

        mapCircle = createMapCircle(geoCoordinates);
        mapScene.addMapPolygon(mapCircle);
    }

    private MapPolygon createMapCircle(GeoCoordinates geoCoordinates) {
        float radiusInMeters = 10000;
        GeoCircle geoCircle = new GeoCircle(geoCoordinates, radiusInMeters);

        GeoPolygon geoPolygon = new GeoPolygon(geoCircle);
        Color fillColor = Color.valueOf(0, 0.56f, 0.54f, 0.63f); // RGBA
        MapPolygon mapPolygon = new MapPolygon(geoPolygon, fillColor);

        return mapPolygon;
    }

    private void clearMapCircle() {
       if (mapCircle != null) {
            mapScene.removeMapPolygon(mapCircle);
        }
    }
}
