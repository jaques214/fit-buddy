package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.AccessControl;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.History.History;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;

public class ManualTrackingFragment extends Fragment {

    private boolean start = true;
    private Thread t;
    private ImageView gif;
    private Button buttonStart;
    private Chronometer chronometer;
    private View view;
    private Context _context;
    private BroadcastReceiver br;
    private PendingIntent pi;
    private RemoteViews remoteWidget;
    private ManualTrackingFragment thisFrag;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();
    private SharedViewModel viewModel;
    private User dbUser;

    private double needToAdd = 0;
    private boolean added = false;
    private double elapsedSeconds;
    public static final String EXTRA_MESSAGE = "pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.EXTRA";

    public ManualTrackingFragment() {
        // Required empty public constructor
    }

    public static ManualTrackingFragment newInstance(String param1, String param2) {
        ManualTrackingFragment fragment = new ManualTrackingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!start) {
            if (buttonStart.getText().toString().equals(getString(R.string.button_go_tracking_manual))) {
                ((GifDrawable) gif.getDrawable()).stop();
            }
        }

        if (needToAdd > 0) {
            ((Animatable) gif.getDrawable()).stop();

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
            NotificationManagerCompat nManager = NotificationManagerCompat.from(view.getContext());
            nManager.cancel(101);
            registerDatabase(needToAdd);
            buttonStart.setText(R.string.button_go_tracking_manual);
            needToAdd = 0;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
        if (user == null) {
            updateUI();
        }
    }

    public void updateUI() {
        Intent intent = new Intent(getContext(), AccessControl.class);
        String message = "Logout";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manual_tracking, container, false);

        thisFrag = (ManualTrackingFragment) getActivity().getSupportFragmentManager().findFragmentByTag("mtf");

        viewModel = new ViewModelProvider((MainFragmentsActivity) _context).get(SharedViewModel.class);

        gif = view.findViewById(R.id.stretchGif);

        Glide.with(view).asGif().load(R.raw.stretching)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true))
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(200);
                                } catch (Exception err) {
                                }
                                if (start == true) {
                                    if ((resource).isRunning()) {
                                        (resource).stop();
                                    }
                                    start = false;
                                }
                            }
                        });
                        t.start();
                        return false;
                    }
                }).into(gif);

        setup();


        buttonStart = view.findViewById(R.id.buttonStart);
        chronometer = view.findViewById(R.id.timer);
        chronometer.setFormat("%m:%s");
        chronometer.setBase(SystemClock.elapsedRealtime());


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactWithGif();
            }
        });


        return view;
    }


    public void interactWithGif() {
        Drawable drawable = gif.getDrawable();

        if (((Animatable) drawable).isRunning()) {
            ((Animatable) drawable).stop();

            elapsedSeconds = Math.floor((SystemClock.elapsedRealtime() - chronometer.getBase()) * 0.001);
            Toast.makeText(view.getContext(), "Tempo decorrido: " + elapsedSeconds, Toast.LENGTH_LONG).show();

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
            NotificationManagerCompat nManager = NotificationManagerCompat.from(view.getContext());
            nManager.cancel(101);
            registerDatabase(elapsedSeconds);
            buttonStart.setText(R.string.button_go_tracking_manual);
        } else {
            ((Animatable) drawable).start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            initializeNotification();
            buttonStart.setText(R.string.button_stop_tracking_manual);
        }
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Cronómetro";
            NotificationChannel channel = new NotificationChannel("1", name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }


    public void initializeNotification() {
        remoteWidget = new RemoteViews(view.getContext().getPackageName(), R.layout.custom_chronometer_notification);
        remoteWidget.setChronometer(R.id.noteChronometer, SystemClock.elapsedRealtime(), null, true);
        remoteWidget.setOnClickPendingIntent(R.id.btnStop, pi);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext(), "1")
                .setSmallIcon(R.drawable.ic_chronometer)
                .setContentTitle("Alongamentos")
                .setOngoing(true)
                .setCustomContentView(remoteWidget);


        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(view.getContext());
        notificationManager.notify(101, builder.build());
    }

    public void setup() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                buttonStart.setText(R.string.button_go_tracking_manual);

                elapsedSeconds = Math.floor((SystemClock.elapsedRealtime() - chronometer.getBase()) * 0.001);

                if (!thisFrag.isResumed()) {
                    needToAdd = elapsedSeconds;
                } else {
                    interactWithGif();
                }
                NotificationManagerCompat manager = NotificationManagerCompat.from(view.getContext());
                manager.cancel(101);

            }
        };
        _context.registerReceiver(br, new IntentFilter("com.cmu.stoptimer"));

        pi = PendingIntent.getBroadcast(_context, 0, new Intent("com.cmu.stoptimer"), PendingIntent.FLAG_MUTABLE);
    }

    public void registerDatabase(double elapsedTime) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                dbUser = viewModel.getUserByEmailSync(user.getEmail());
            }
        });

        History history = new History();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (dbUser != null && !added) {
                        history.userId = dbUser.id;

                        history.duration = elapsedTime;
                        history.exercise = "Alongamento";
                        history.repetitions = Double.valueOf(elapsedTime / 3).intValue();
                        history.burnedCalories = elapsedTime * 100;
                        viewModel.addHistory(history);
                        added = true;
                        break;
                    }
                }
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (dbUser != null && added) {
                        viewModel.updateUserCalories(dbUser.email, (elapsedTime * 100));
                        added = false;
                        break;
                    }
                }
            }
        });


    }


}
