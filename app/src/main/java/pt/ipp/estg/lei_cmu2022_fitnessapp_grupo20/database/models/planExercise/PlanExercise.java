package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.planExercise;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.exercise.Exercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.plan.Plan;

@Entity(foreignKeys = {@ForeignKey(entity = Plan.class, parentColumns = "id", childColumns = "planId"),
        @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exerciseId")},
        indices = {@Index(value = {"planId", "exerciseId"})})
public class PlanExercise {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Integer planId;
    public Integer exerciseId;
}
