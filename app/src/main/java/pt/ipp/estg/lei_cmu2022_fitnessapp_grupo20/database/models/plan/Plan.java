package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.plan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Plan{
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String category;
    public boolean type;
}
