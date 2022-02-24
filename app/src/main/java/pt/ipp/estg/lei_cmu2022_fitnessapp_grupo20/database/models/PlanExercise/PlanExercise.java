package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.PlanExercise;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Exercise.Exercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Plan.Plan;

@Entity(foreignKeys = {@ForeignKey(entity = Plan.class, parentColumns = "id", childColumns = "planId"),
        @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exerciseId")},
        indices = {@Index(value = {"planId", "exerciseId"})})
public class PlanExercise {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Integer planId;
    public Integer exerciseId;
}
