package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.history.History;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.user.User;

public class AutomaticTrackingFragment extends Fragment {
    private Context _context;
    private View view;
    private SensorManager sensorManager;
    private double magnitudePrev = 0;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();
    private SharedViewModel viewModel;
    private Sensor stepSensor;
    private TextView textStep;
    private Button button;

    private boolean added = false;
    private User dbUser;
    private boolean running = false;
    private MutableLiveData<Integer> steps;


    public AutomaticTrackingFragment() {
    }


    public static AutomaticTrackingFragment newInstance(String param1, String param2) {
        AutomaticTrackingFragment fragment = new AutomaticTrackingFragment();

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_automatic_tracking, container, false);

        viewModel = new ViewModelProvider((MainFragmentsActivity) _context).get(SharedViewModel.class);

        steps = new MutableLiveData<>();
        steps.setValue(0);

        textStep = view.findViewById(R.id.stepText);
        button = view.findViewById(R.id.buttonStartStep);


        sensorManager = (SensorManager) _context.getSystemService(_context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x_acceleration = event.values[0];
                float y_acceleration = event.values[1];
                float z_acceleration = event.values[2];

                double magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);

                double delta = magnitude - magnitudePrev;
                magnitudePrev = magnitude;

                Log.d("Steps", steps.getValue().toString());
                if (delta > 6) {
                    steps.setValue(steps.getValue() + 1);
                }

                textStep.setText(steps.getValue() + " Steps");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };


        button.setOnClickListener(v -> {
            if (running) {
                button.setText("Start jogging");
                registerDatabase(steps.getValue());
                sensorManager.unregisterListener(stepDetector);
                NotificationManagerCompat manager = NotificationManagerCompat.from(_context);
                running = false;
                manager.cancel(102);
                steps.setValue(0);
                textStep.setText(steps.getValue() + " Steps");
            } else {
                sensorManager.registerListener(stepDetector, stepSensor, SensorManager.SENSOR_DELAY_UI);
                button.setText("Stop");
                running = true;
            }
        });
        return view;
    }



    public void registerDatabase(double steps) {

        AsyncTask.execute(() -> dbUser = viewModel.getUserByEmailSync(user.getEmail()));

        History history = new History();

        AsyncTask.execute(() -> {
            while (true) {
                if (dbUser != null && !added) {
                    history.userId = dbUser.id;

                    history.steps = Double.valueOf(steps).intValue();
                    history.exercise = "Corrida";
                    history.burnedCalories = steps * 150;
                    history.meters = steps * 0.762;
                    viewModel.addHistory(history);
                    added = true;
                    break;
                }
            }
        });

        AsyncTask.execute(() -> {
            while (true) {
                if (dbUser != null && added) {
                    viewModel.updateUserCalories(dbUser.email, (steps * 150));
                    added = false;
                    break;
                }
            }
        });


    }


    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Cronómetro";
            NotificationChannel channel = new NotificationChannel("3", name, NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

}