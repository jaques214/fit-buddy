package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Exercise;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertExercise(Exercise exercise);

    @Query("SELECT * FROM Exercise WHERE id LIKE :id")
    public LiveData<Exercise> getExercise(int id);

    @Query("SELECT * FROM Exercise")
    public LiveData<List<Exercise>> getAllExercises();
}
