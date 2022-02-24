package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM User WHERE id LIKE :id")
    LiveData<User> getUser(int id);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getUsers();

    @Query("SELECT * FROM User WHERE email LIKE :email")
    LiveData<User> getUserByEmail(String email);

    @Query("SELECT * FROM User WHERE email LIKE :email")
    User getUserByEmailSync(String email);

    @Query("UPDATE User SET total_calories = total_calories + :calories WHERE email LIKE :email ")
    void updateCalories(String email, double calories);

    @Query("UPDATE User SET level = :levelX WHERE email LIKE :email ")
    void updateLevel(String email, String levelX);

    @Query("SELECT COUNT(*) FROM User WHERE email LIKE :email")
    int checkIfExists(String email);

    @Query("SELECT COUNT(*) FROM User")
    int getSize();

}
