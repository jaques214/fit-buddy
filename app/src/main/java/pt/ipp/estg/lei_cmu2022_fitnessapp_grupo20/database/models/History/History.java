package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.History;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Exercise.Exercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;

@Entity(foreignKeys = {@ForeignKey(entity = User.class,
        parentColumns = "id", childColumns = "userId", onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = {"userId"})})
public class History {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String exercise;
    public double duration;
    public double burnedCalories;
    public double meters;
    public int steps;
    public Integer userId;
    public int repetitions;
}
