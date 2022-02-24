package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Plan;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPlan(Plan plan);

    @Query("SELECT * FROM `Plan` WHERE id LIKE :id")
    public LiveData<Plan> getPlan(int id);

    @Query("SELECT * FROM `Plan`")
    public LiveData<List<Plan>> getAllPlan();
}
