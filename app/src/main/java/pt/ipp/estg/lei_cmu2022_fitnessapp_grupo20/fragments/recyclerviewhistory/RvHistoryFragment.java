package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviewhistory;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.history.History;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RvHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RvHistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HystoryAdapter adapter;
    private Activity main;
    private List<History> historyX;
    private FirebaseAuth mAuth;
    private SharedViewModel sh;
    private int id;

    public RvHistoryFragment() {

    }


    public static RvHistoryFragment newInstance(String param1, String param2) {
        RvHistoryFragment fragment = new RvHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        main = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_rv_history, container, false);

        sh = new ViewModelProvider(this).get(SharedViewModel.class);


        adapter = new HystoryAdapter(contentView.getContext(),historyX);
        recyclerView = contentView.findViewById(R.id.rvHistory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(contentView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);



        sh.getHistoryByEmail(mAuth.getCurrentUser().getEmail()).observe((MainFragmentsActivity) main, new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                historyX = histories;
                adapter.setAdapterList(historyX);
                adapter.notifyDataSetChanged();
            }
        });


        return contentView;
    }
}