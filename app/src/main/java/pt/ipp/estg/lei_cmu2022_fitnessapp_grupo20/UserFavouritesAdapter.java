package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.FavouritePlaces.FavouritePlaces;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.UserPlace.UserPlace;

public class UserFavouritesAdapter extends RecyclerView.Adapter<UserPlaceViewHolder> {
    private Context context;
    private List<FavouritePlaces> favoritePlacesList;

    public UserFavouritesAdapter(MainFragmentsActivity context, List<FavouritePlaces> favoritePlacesList) {
        this.context = context;
        this.favoritePlacesList = favoritePlacesList;
    }

    @Override
    public int getItemCount() {
        return this.favoritePlacesList.size();
    }

    @Override
    public UserPlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.favorite_user_place_item, parent, false);
        return new UserPlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserPlaceViewHolder holder, int position) {
        FavouritePlaces places = favoritePlacesList.get(position);
        TextView location = holder.locationTextView;
        location.setText(places.place);
        RatingBar rating = holder.rating;
        rating.setRating(places.rank);
    }

}

class UserPlaceViewHolder extends RecyclerView.ViewHolder {
    TextView locationTextView;
    RatingBar rating;

   public UserPlaceViewHolder(View itemView) {
       super(itemView);
       locationTextView = itemView.findViewById(R.id.place);
       rating = itemView.findViewById(R.id.ratingBar);
   }
}
