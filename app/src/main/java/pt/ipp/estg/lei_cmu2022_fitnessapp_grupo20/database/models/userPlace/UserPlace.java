package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.userPlace;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.favouritePlaces.FavouritePlaces;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.user.User;

@Entity(foreignKeys = {@ForeignKey(entity = FavouritePlaces.class, parentColumns = "id", childColumns = "placeId"),
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId")},
        indices = {@Index(value = {"placeId", "userId"})})
public class UserPlace {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Integer placeId;
    public Integer userId;

    public UserPlace(Integer placeId, Integer userId) {
        this.placeId = placeId;
        this.userId = userId;
    }
}
