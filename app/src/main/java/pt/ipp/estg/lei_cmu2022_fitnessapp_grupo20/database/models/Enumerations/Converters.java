package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Enumerations;

import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Converters {

    @TypeConverter
    public long dateToTimestamp(LocalDateTime date) {
        return Timestamp.valueOf(date.toString()).getTime();
    }

    @TypeConverter
    public LocalDateTime fromTimestamp(Long value) {
        if (value != null) {
            return LocalDateTime.parse(new Timestamp(value).toString());
        }
        return null;
    }
}
