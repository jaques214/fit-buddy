package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviews;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.exercise.Exercise;


public class RvExerciseFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private SharedViewModel sh;
    private Activity main;
    private List<Exercise> exerciseList;

    public RvExerciseFragment() {
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.main =(MainFragmentsActivity) context;
    }

    public static RvExerciseFragment newInstance(String param1, String param2) {
        RvExerciseFragment fragment = new RvExerciseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.main = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_rv_exercise, container, false);


        sh = new ViewModelProvider(this).get(SharedViewModel.class);
        LiveData<List<Exercise>> temp = sh.getExercises();
        adapter = new ExerciseAdapter(exerciseList);
        recyclerView = contentView.findViewById(R.id.recyclerViewExercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(contentView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        temp.observe((LifecycleOwner) main, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                exerciseList = exercises;
                adapter.setAdapterList(exerciseList);
                adapter.notifyDataSetChanged();
            }
        });

        return contentView;
    }

}