package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.inf;

import androidx.room.*;
import java.time.LocalDateTime;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.user.User;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"))
public class Inf {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public double weight;
    public double height;
    public LocalDateTime date;
    public Integer userId;

}
