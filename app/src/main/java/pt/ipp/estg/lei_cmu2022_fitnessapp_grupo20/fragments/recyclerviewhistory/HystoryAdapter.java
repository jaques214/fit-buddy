package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviewhistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.history.History;

public class HystoryAdapter extends RecyclerView.Adapter<HystoryAdapter.HistoryViewHolder>{
    private Context context;
    private List<History> historyExercises;

    public HystoryAdapter(Context context, List<History> historyExercises) {
        this.context = context;
        this.historyExercises = historyExercises;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View historyView = inflater.inflate(R.layout.item_history, parent, false);
        HistoryViewHolder holder = new HistoryViewHolder(historyView);

        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        History exercise = historyExercises.get(position);

        TextView nameText = holder.nameText;
        TextView timeText = holder.durationText;
        TextView caloriesText = holder.caloriesText;
        ImageView img = holder.img;

        if(exercise.exercise.equals("Corrida")){
            img.setImageResource(R.drawable.jogging);
            nameText.setText(exercise.exercise);
            String steps = "Steps : " + exercise.steps + " - Meters : "+exercise.meters;
            String calories = "Calories : " + exercise.burnedCalories;
            timeText.setText(steps);
            caloriesText.setText(calories);
        }else{
            img.setImageResource(R.drawable.alongar);
            nameText.setText(exercise.exercise);
            String time = "Time : " + exercise.duration;
            String calories = "Calories : " + exercise.burnedCalories;
            timeText.setText(time);
            caloriesText.setText(calories);
        }


    }

    @Override
    public int getItemCount() {
        if (historyExercises == null) {
            return 0;
        } else {
            return historyExercises.size();
        }
    }

    public void setAdapterList(List<History> exList) {
        this.historyExercises = exList;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameText;
        public TextView durationText;
        public TextView caloriesText;
        public ImageView img;


        public HistoryViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameExText);
            durationText = itemView.findViewById(R.id.timeText);
            caloriesText = itemView.findViewById(R.id.caloriesText);
            img = itemView.findViewById(R.id.imageViewH);
        }

        @Override
        public void onClick(View v) {
        }

    }
}
