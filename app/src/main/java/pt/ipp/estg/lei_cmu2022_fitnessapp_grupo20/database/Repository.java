package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.exercise.Exercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.exercise.ExerciseDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.favouritePlaces.FavouritePlaces;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.favouritePlaces.FavouritePlacesDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.history.History;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.history.HistoryDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.inf.Inf;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.inf.InfDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.plan.Plan;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.plan.PlanDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.planExercise.PlanExercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.planExercise.PlanExerciseDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.user.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.user.UserDao;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.userPlace.UserPlace;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.userPlace.UserPlaceDao;

public class Repository {
    public UserDao daoUser;
    public PlanExerciseDao daoPlanExercise;
    public UserPlaceDao daoUserPlace;
    public PlanDao daoPlan;
    public InfDao daoInf;
    public HistoryDao daoHistory;
    public FavouritePlacesDao daoFavouritePlaces;
    public ExerciseDao daoExercise;


    Repository(Application application) {
        DB db = DB.getDatabase(application);
        daoUser = db.daoUser();
        daoPlanExercise = db.daoPlanExercise();
        daoUserPlace = db.daoUserPlace();
        daoPlan = db.daoPlan();
        daoInf = db.daoInf();
        daoHistory = db.daoHistory();
        daoFavouritePlaces = db.daoFavouritePlaces();
        daoExercise = db.daoExercise();
    }

    public LiveData<List<User>> getUsers() {
        return daoUser.getUsers();
    }

    public LiveData<List<PlanExercise>> getPlanExercises() {
        return daoPlanExercise.getAllPlanExercises();
    }

    public LiveData<List<UserPlace>> getUserPlaces() {
        return daoUserPlace.getAllUserPlaces();
    }

    public LiveData<List<FavouritePlaces>> getPlacesByUser(String username) {
        return daoFavouritePlaces.getPlacesByUser(username);
    }

    public LiveData<UserPlace> getUserPlace(int id) {
        return daoUserPlace.getUserPlace(id);
    }

    public LiveData<List<Plan>> getPlans() {
        return daoPlan.getAllPlan();
    }

    public LiveData<List<Inf>> getInfs() {
        return daoInf.getAllInf();
    }

    public LiveData<List<History>> getHistories() {
        return daoHistory.getAllHistory();
    }

    public LiveData<List<History>> getHistoryByUser(int userID) {
        return daoHistory.getHistoryByUserID(userID);
    }

    public LiveData<List<FavouritePlaces>> getFavouritePlaces() {
        return daoFavouritePlaces.getAllFavouritePlaces();
    }

    public LiveData<FavouritePlaces> getFavouritePlace(int id) {
        return daoFavouritePlaces.getFavouritePlace(id);
    }

    public LiveData<List<Exercise>> getExercises() {
        return daoExercise.getAllExercises();
    }

    public void addUser(User user) {
        daoUser.insertUser(user);
    }

    public void addHistory(History history) {
        daoHistory.insertHistory(history);
    }

    public int getFavouritePlacesSize() {
       return daoFavouritePlaces.getFavoritePlacesSize();
    }

    public void addFavouritePlaces(FavouritePlaces places) {
        daoFavouritePlaces.insertUserPlace(places);
    }

    public void addUserFavouritePlaces(UserPlace user_place) {
        daoUserPlace.insertUserPlace(user_place);
    }

    public void addExercise(Exercise exercise){
        daoExercise.insertExercise(exercise);
    }

    public LiveData<User> getUserByEmail(String email) {
        return daoUser.getUserByEmail(email);
    }

    public User getUserByEmailSync(String email){
        return daoUser.getUserByEmailSync(email);
    }


    public void updateUserCalories(String email, double newCalories) {
        daoUser.updateCalories(email, newCalories);
    }

    public void updateUserLevel(String email,String level) {
        daoUser.updateLevel(email,level);
    }


    public LiveData<List<History>> getHistoryByEmail(String email){
        return daoHistory.getHistoryByEmail(email);
    }

    public int checkIfExists(String email){
       return daoUser.checkIfExists(email);
    }

    public int getSize(){
        return daoUser.getSize();
    }
}
