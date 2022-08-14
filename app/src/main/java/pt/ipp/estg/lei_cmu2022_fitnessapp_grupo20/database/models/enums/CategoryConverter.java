package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.enums;

import androidx.room.TypeConverter;

public class CategoryConverter {

    @TypeConverter
    public static String fromCategoryToString(Category category) {
        if (category == null)
            return null;
        return category.toString();
    }
}
