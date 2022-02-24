package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.FavouritePlaces;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavouritePlaces {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String place;
    public int rank;
    public String username;

    public FavouritePlaces(String place, int rank) {
        this.place = place;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
