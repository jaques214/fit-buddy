package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.inf;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InfDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertInf(Inf inf);

    @Query("SELECT * FROM Inf WHERE id LIKE :id")
    public LiveData<Inf> getInf(int id);

    @Query("SELECT * FROM Inf")
    public LiveData<List<Inf>> getAllInf();
}
