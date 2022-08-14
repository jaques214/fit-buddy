package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MapActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit.ImageLoadTask;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit.Weather;

public class WeatherFragment extends Fragment {
    private MapActivity activity;
    private String local;
    private String url;
    private String temperature;
    private String interval;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MapActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            local = getArguments().getString("location");
            url = getArguments().getString("weather_icon");
            temperature = getArguments().getString("temp");
            interval = getArguments().getString("temp_interval");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vf = inflater.inflate(R.layout.fragment_weather, container, false);

        TextView location = vf.findViewById(R.id.location);
        location.setText(local);
        ImageView weather_icon = vf.findViewById(R.id.icon);
        AsyncTask<Void, Void, Bitmap> imageView = new ImageLoadTask(url, weather_icon).execute();
        TextView temp = vf.findViewById(R.id.degrees);
        temp.setText(temperature);
        TextView temp_interval = vf.findViewById(R.id.temp_interval);
        temp_interval.setText(interval);

        return vf;
    }

    public interface FragmentCommunication {
        void updateUI(Weather poi);
    }
}

