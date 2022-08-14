package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.favouritePlaces.FavouritePlaces;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<FavouritePlaces> favoritePlacesList;

    public RecyclerViewAdapter(Context context, List<FavouritePlaces> favoritePlacesList) {
        this.context = context;
        this.favoritePlacesList = favoritePlacesList;
    }

    @Override
    public int getItemCount() {
        return this.favoritePlacesList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.favorite_place_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FavouritePlaces places = favoritePlacesList.get(position);
        TextView location = holder.locationTextView;
        location.setText(places.place);
        RatingBar rating = holder.rating;
        rating.setRating(places.rank);
        TextView username = holder.username;
        username.setText(places.username);
    }

}

 class ViewHolder extends RecyclerView.ViewHolder {
    TextView locationTextView;
    RatingBar rating;
    TextView username;

    public ViewHolder(View itemView) {
        super(itemView);

        locationTextView = itemView.findViewById(R.id.place);
        rating = itemView.findViewById(R.id.ratingBar);
        username = itemView.findViewById(R.id.username);
    }
}
