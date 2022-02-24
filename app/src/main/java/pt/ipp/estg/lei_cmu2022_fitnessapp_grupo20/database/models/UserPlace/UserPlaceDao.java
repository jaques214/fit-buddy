package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.UserPlace;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserPlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUserPlace(UserPlace userPlace);

    @Query("SELECT * FROM UserPlace WHERE userId LIKE :id")
    public LiveData<UserPlace> getUserPlace(int id);

    @Query("SELECT * FROM UserPlace")
    public LiveData<List<UserPlace>> getAllUserPlaces();


}
