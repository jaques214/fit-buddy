package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User;

import androidx.room.*;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Enumerations.LevelEnum;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Enumerations.StateEnum;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Inf.Inf;

@Entity(foreignKeys = {@ForeignKey(entity = Inf.class, parentColumns = "id", childColumns = "goalId")}, indices = {
        @Index(value = {"goalId"})})
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String password;
    public String email;
    public int total_calories;
    public Integer goalId;
    public String level;
    public String state;
    public String location;

    public User() {
    }

    public User(int id, String name, String email,String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.total_calories = 0;
        this.level = "Beginner";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
