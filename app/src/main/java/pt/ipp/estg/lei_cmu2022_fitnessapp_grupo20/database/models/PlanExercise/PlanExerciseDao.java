package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.PlanExercise;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface PlanExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPlanExercise(PlanExercise planExercise);

    @Query("SELECT * FROM PlanExercise WHERE id LIKE :id")
    public LiveData<PlanExercise> getPlanExercise(int id);

    @Query("SELECT * FROM PlanExercise")
    public LiveData<List<PlanExercise>> getAllPlanExercises();
}
