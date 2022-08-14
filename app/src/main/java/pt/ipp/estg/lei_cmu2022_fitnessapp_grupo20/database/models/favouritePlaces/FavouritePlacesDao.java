package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.favouritePlaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavouritePlacesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUserPlace(FavouritePlaces favouritePlaces);

    @Query("SELECT * FROM FavouritePlaces WHERE id LIKE :id")
    public LiveData<FavouritePlaces> getFavouritePlace(int id);

    @Query("SELECT * FROM FavouritePlaces")
    public LiveData<List<FavouritePlaces>> getAllFavouritePlaces();

    @Query("SELECT COUNT(*) FROM FavouritePlaces")
    public int getFavoritePlacesSize();

    @Query("SELECT * FROM FavouritePlaces WHERE username LIKE :usernameX")
    public LiveData<List<FavouritePlaces>> getPlacesByUser(String usernameX);
}
