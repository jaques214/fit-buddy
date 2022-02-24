package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Enumerations.Converters;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Exercise.Exercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Exercise.ExerciseDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.FavouritePlaces.FavouritePlaces;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.FavouritePlaces.FavouritePlacesDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.History.History;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.History.HistoryDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Inf.Inf;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Inf.InfDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Plan.Plan;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Plan.PlanDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.PlanExercise.PlanExercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.PlanExercise.PlanExerciseDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.UserDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.UserPlace.UserPlace;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.UserPlace.UserPlaceDao;

@Database(entities = {Exercise.class, FavouritePlaces.class, History.class, Inf.class,
        Plan.class, PlanExercise.class, User.class, UserPlace.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class DB extends RoomDatabase {
    public abstract UserDao daoUser();

    public abstract PlanExerciseDao daoPlanExercise();

    public abstract UserPlaceDao daoUserPlace();

    public abstract PlanDao daoPlan();

    public abstract InfDao daoInf();

    public abstract HistoryDao daoHistory();

    public abstract FavouritePlacesDao daoFavouritePlaces();

    public abstract ExerciseDao daoExercise();


    private static volatile DB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DB.class, "database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

}
