package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Inf;

import androidx.room.*;
import androidx.room.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"))
public class Inf {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public double weight;
    public double height;
    public LocalDateTime date;
    public Integer userId;

}
