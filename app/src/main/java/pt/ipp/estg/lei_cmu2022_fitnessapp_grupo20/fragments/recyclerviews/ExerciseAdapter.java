package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Exercise.Exercise;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> ex;
    private Context context;

    public ExerciseAdapter(List<Exercise> ex) {
        this.ex = ex;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View exerciseView = inflater.inflate(R.layout.item_exercise, parent, false);
        ExerciseViewHolder holder = new ExerciseViewHolder(exerciseView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        Exercise exercise = ex.get(position);
        TextView nameText = holder.nameText;
        TextView categoryText = holder.categoryText;
        ImageView img = holder.imgEx;
        Button train = holder.buttonTrain;


        if (ex.get(position).type) {
            img.setImageResource(R.drawable.jogging);
        } else {
            img.setImageResource(R.drawable.alongar);
        }
        nameText.setText(exercise.name);
        categoryText.setText(exercise.category);

    }

    @Override
    public int getItemCount() {
        if (ex == null) {
            return 0;
        } else {
            return ex.size();
        }
    }

    public void setAdapterList(List<Exercise> exList) {
        this.ex = exList;
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameText;
        public TextView categoryText;
        public Button buttonTrain;
        public ImageView imgEx;
        public TrackingOnClick trackingOnClick;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameExText);
            categoryText = itemView.findViewById(R.id.categoryText);
            buttonTrain = itemView.findViewById(R.id.buttonPractise);
            imgEx = itemView.findViewById(R.id.imageViewEx);

            buttonTrain.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            ((TrackingOnClick) context).onClick(getLayoutPosition());
        }

    }

    public interface TrackingOnClick {
        void onClick(int position);
    }

}
