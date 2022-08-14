package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.history;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertHistory(History history);

    @Query("SELECT * FROM History WHERE id LIKE :id")
    public LiveData<History> getHistory(int id);

    @Query("SELECT * FROM History")
    public LiveData<List<History>> getAllHistory();

    @Query("SELECT * FROM History WHERE userId LIKE :userID")
    public LiveData<List<History>> getHistoryByUserID(int userID);

    @Query("SELECT * FROM History, User WHERE User.email LIKE :email AND History.userId LIKE User.id")
    public LiveData<List<History>> getHistoryByEmail(String email);
}
