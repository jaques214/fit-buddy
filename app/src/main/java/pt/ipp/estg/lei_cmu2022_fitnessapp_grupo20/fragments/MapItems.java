package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.here.sdk.core.Anchor2D;
import com.here.sdk.core.Color;
import com.here.sdk.core.GeoBox;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.GeoOrientationUpdate;
import com.here.sdk.core.GeoPolyline;
import com.here.sdk.core.Location;
import com.here.sdk.core.Point2D;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.gestures.TapListener;
import com.here.sdk.mapview.LocationIndicator;
import com.here.sdk.mapview.MapCamera;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapPolyline;
import com.here.sdk.mapview.MapView;
import com.here.sdk.mapview.MapViewBase;
import com.here.sdk.mapview.PickMapItemsResult;
import com.here.sdk.routing.CalculateRouteCallback;
import com.here.sdk.routing.CarOptions;
import com.here.sdk.routing.Route;
import com.here.sdk.routing.RoutingEngine;
import com.here.sdk.routing.RoutingError;
import com.here.sdk.routing.Waypoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MapActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.FavouritePlaces.FavouritePlaces;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads.DbAddFavouritePlacesThread;

public class MapItems {
    private SharedViewModel sh;
    private final Context context;
    private final MapView mapView;
    private MapActivity mapActivity;
    private final List<MapMarker> mapMarkerList = new ArrayList<>();
    private final List<MapPolyline> mapPolylines = new ArrayList<>();
    private final RoutingEngine routingEngine;
    private GeoCoordinates startGeoCoordinates;
    private Resources resources;
    private final List<LocationIndicator> locationIndicatorList = new ArrayList<>();

    public MapItems(GeoCoordinates location, Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
        this.mapActivity = (MapActivity) context;
        this.startGeoCoordinates = location;
        this.resources = mapActivity.getResources();
        MapCamera camera = mapView.getCamera();
        double distanceInMeters = 50000;
        camera.lookAt(startGeoCoordinates, distanceInMeters);

        try {
            routingEngine = new RoutingEngine();
        } catch (InstantiationErrorException e) {
            throw new RuntimeException("Initialization of RoutingEngine failed: " + e.error.name());
        }
        // Setting a tap handler to pick markers from map.
        setTapGestureHandler();
    }

    public void addPOIMapMarker(GeoCoordinates geoCoordinates) {
        MapImage mapImage = MapImageFactory.fromResource(context.getResources(), R.drawable.poi);

        // The bottom, middle position should point to the location.
        // By default, the anchor point is set to 0.5, 0.5.
        Anchor2D anchor2D = new Anchor2D(0.5F, 1);
        MapMarker mapMarker = new MapMarker(geoCoordinates, mapImage, anchor2D);

        mapView.getMapScene().addMapMarker(mapMarker);
        mapMarkerList.add(mapMarker);
    }

    private void addLocationIndicator(GeoCoordinates geoCoordinates,
                                      LocationIndicator.IndicatorStyle indicatorStyle) {
        LocationIndicator locationIndicator = new LocationIndicator();
        locationIndicator.setLocationIndicatorStyle(indicatorStyle);

        // A LocationIndicator is intended to mark the user's current location,
        // including a bearing direction.
        Location location = new Location.Builder()
                .setCoordinates(geoCoordinates)
                .setTimestamp(new Date())
                .build();

        locationIndicator.updateLocation(location);

        // A LocationIndicator listens to the lifecycle of the map view,
        // therefore, for example, it will get destroyed when the map view gets destroyed.
        mapView.addLifecycleListener(locationIndicator);
        locationIndicatorList.add(locationIndicator);
    }

    public void addLocationMapMarker(GeoCoordinates geoCoordinates) {
        // Centered on location.
        addLocationIndicator(geoCoordinates, LocationIndicator.IndicatorStyle.PEDESTRIAN);
    }

    public void addCircleMapMarker(GeoCoordinates geoCoordinates) {
        MapImage mapImage = MapImageFactory.fromResource(context.getResources(), R.drawable.circle);
        MapMarker mapMarker = new MapMarker(geoCoordinates, mapImage);

        mapView.getMapScene().addMapMarker(mapMarker);
        mapMarkerList.add(mapMarker);
    }

    private String formatTime(long sec) {
        int hours = (int) (sec / 3600);
        int minutes = (int) ((sec % 3600) / 60);

        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    private String formatLength(int meters) {
        int kilometers = meters / 1000;
        int remainingMeters = meters % 1000;

        return String.format(Locale.getDefault(), "%02d.%02d km", kilometers, remainingMeters);
    }

    public void addRoute(GeoCoordinates start, GeoCoordinates end) {
        clearMap();

        Waypoint startWaypoint = new Waypoint(start);
        Waypoint destinationWaypoint = new Waypoint(end);

        List<Waypoint> waypoints =
                new ArrayList<>(Arrays.asList(startWaypoint, destinationWaypoint));

        routingEngine.calculateRoute(
                waypoints,
                new CarOptions(),
                new CalculateRouteCallback() {
                    @Override
                    public void onRouteCalculated(@Nullable RoutingError routingError, @Nullable List<Route> routes) {
                        if (routingError == null) {
                            Route route = routes.get(0);
                            showRouteDetails(route);
                            showRouteOnMap(route, start, end);
                            GeoBox routeGeoBox = route.getBoundingBox();
                            // Set null values to keep the default map orientation.
                            mapView.getCamera().lookAt(routeGeoBox, new GeoOrientationUpdate(null, null));
                        } else {
                            Toast.makeText(mapActivity, "Error while calculating a route", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showRouteOnMap(Route route, GeoCoordinates start, GeoCoordinates end) {
        // Show route as polyline.
        GeoPolyline routeGeoPolyline;
        try {
            routeGeoPolyline = new GeoPolyline(route.getPolyline());
        } catch (InstantiationErrorException e) {
            // It should never happen that a route polyline contains less than two vertices.
            return;
        }

        float widthInPixels = 20;
        MapPolyline routeMapPolyline = new MapPolyline(routeGeoPolyline,
                widthInPixels,
                Color.valueOf(0, 0.56f, 0.54f, 0.63f)); // RGBA

        mapView.getMapScene().addMapPolyline(routeMapPolyline);
        mapPolylines.add(routeMapPolyline);

        // Draw a circle to indicate starting point and destination.
        addCircleMapMarker(start);
        addCircleMapMarker(end);
    }

    public void clearMap() {
        mapView.getMapScene().removeMapMarkers(mapMarkerList);
        mapMarkerList.clear();
        clearRoute();
        double distanceInMeters = 50000;
        mapView.getCamera().lookAt(startGeoCoordinates, distanceInMeters);
    }

    private void clearRoute() {
        for (MapPolyline mapPolyline : mapPolylines) {
            mapView.getMapScene().removeMapPolyline(mapPolyline);
        }
        mapPolylines.clear();
    }

    private void setTapGestureHandler() {
        mapView.getGestures().setTapListener(new TapListener() {
            @Override
            public void onTap(@NonNull Point2D touchPoint) {
                pickMapMarker(touchPoint);
            }
        });
    }

    private void pickMapMarker(final Point2D touchPoint) {
        float radiusInPixel = 2;
        mapView.pickMapItems(touchPoint, radiusInPixel, new MapViewBase.PickMapItemsCallback() {
            @Override
            public void onPickMapItems(@Nullable PickMapItemsResult pickMapItemsResult) {
                if (pickMapItemsResult == null) {
                    // An error occurred while performing the pick operation.
                    return;
                }

                List<MapMarker> mapMarkerList = pickMapItemsResult.getMarkers();
                int listSize = mapMarkerList.size();
                if (listSize == 0) {
                    return;
                }
                MapMarker topmostMapMarker = mapMarkerList.get(0);

                Geocoder geocoder = new Geocoder(context);
                List<Address> location = null;
                try {
                    location = geocoder.getFromLocation(topmostMapMarker.getCoordinates().latitude,
                            topmostMapMarker.getCoordinates().longitude, 1);
                    String message = location.get(0).getAddressLine(0);
                    showDialog(message);
                } catch (IOException e) {
                    Log.e("Erro", e.getMessage());
                }
            }
        });
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = resources.getString(R.string.notification_name);
            NotificationChannel channel = new NotificationChannel("2", name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void showRouteDetails(Route route) {
        long estimatedTravelTimeInSeconds = route.getDurationInSeconds();
        int lengthInMeters = route.getLengthInMeters();

        String routeDetails =
                resources.getString(R.string.travel_time) + " " + formatTime(estimatedTravelTimeInSeconds)
                        + ", " + resources.getString(R.string.distance) + " " + formatLength(lengthInMeters);

        routeDialog(resources.getString(R.string.route_title), routeDetails);
    }

    private void routeDialog(String title, String message) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = mapActivity.getLayoutInflater();
        builder.setMessage(message);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.drawable.ic_baseline_favorite_24)
                .setContentTitle(resources.getString(R.string.notification_title))
                .setContentText(resources.getString(R.string.notification_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        createNotificationChannel();


        builder.setNeutralButton(resources.getString(R.string.follow_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                sh = new ViewModelProvider(mapActivity).get(SharedViewModel.class);
                LiveData<User> temp = sh.getUserByEmail(mAuth.getCurrentUser().getEmail());

                temp.observe(mapActivity, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);;
                        FavouritePlaces place = new FavouritePlaces(message, (int)ratingBar.getRating());
                        DbAddFavouritePlacesThread favouritePlacesThread = new DbAddFavouritePlacesThread(mapActivity, place, user);
                        favouritePlacesThread.start();
                    }
                });
                notificationManager.notify(1,notification.build());
            }
        }
        );
        builder.create();

        builder.show();

    }


}
