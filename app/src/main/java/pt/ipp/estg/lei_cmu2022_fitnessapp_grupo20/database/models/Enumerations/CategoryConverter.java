package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Enumerations;

import androidx.room.TypeConverter;

public class CategoryConverter {

    @TypeConverter
    public static String fromCategoryToString(Category category) {
        if (category == null)
            return null;
        return category.toString();
    }

    /*@TypeConverter
    public static Category fromStringToCategory(String category) {
        if (TextUtils.isEmpty(category))
            return DEFAULT_CATEGORY;
        return YOUR_LOGIC_FOR_CONVERSION;
    } */
}
