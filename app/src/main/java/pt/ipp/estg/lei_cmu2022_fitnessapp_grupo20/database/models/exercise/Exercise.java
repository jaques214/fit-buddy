package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.exercise;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String category;
    public boolean type;

    public Exercise(int id, String name, String category,boolean type) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.type = type;
    }
}
