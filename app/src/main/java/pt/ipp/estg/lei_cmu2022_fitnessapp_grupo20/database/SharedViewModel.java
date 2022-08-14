package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.exercise.Exercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.favouritePlaces.FavouritePlaces;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.history.History;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.inf.Inf;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.plan.Plan;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.planExercise.PlanExercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.user.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.userPlace.UserPlace;

public class SharedViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<User>> users;
    private LiveData<List<PlanExercise>> planExercises;
    private LiveData<List<UserPlace>> userPlaces;
    private LiveData<List<Plan>> plans;
    private LiveData<List<Inf>> infs;
    private LiveData<List<History>> histories;
    private LiveData<List<FavouritePlaces>> favouritePlaces;
    private LiveData<FavouritePlaces> favouritePlace;
    private LiveData<List<Exercise>> exercises;
    private LiveData<User> user;
    private int id;

    public SharedViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        users = repository.getUsers();
        planExercises = repository.getPlanExercises();
        userPlaces = repository.getUserPlaces();
        plans = repository.getPlans();
        infs = repository.getInfs();
        histories = repository.getHistories();
        favouritePlaces = repository.getFavouritePlaces();
        favouritePlace = repository.getFavouritePlace(id);
        exercises = repository.getExercises();
    }

    public Repository getRepository() {
        return repository;
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public LiveData<List<PlanExercise>> getPlanExercises() {
        return planExercises;
    }

    public LiveData<List<UserPlace>> getUserPlaces() {
        return userPlaces;
    }

    public LiveData<List<FavouritePlaces>> getPlacesByUser(String username) {
        return repository.getPlacesByUser(username);
    }

    public LiveData<List<Plan>> getPlans() {
        return plans;
    }

    public LiveData<List<Inf>> getInfs() {
        return infs;
    }

    public LiveData<List<History>> getHistories() {
        return histories;
    }

    public LiveData<List<History>> getHistoryByUser(int userID){
        return repository.getHistoryByUser(userID);
    }

    public LiveData<List<FavouritePlaces>> getFavouritePlaces() {
        return favouritePlaces;
    }

    public LiveData<FavouritePlaces> getFavouritePlace(int id) {
        return favouritePlace;
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }

    public LiveData<User> getUserByEmail(String email){
        return repository.getUserByEmail(email);
    }

    public void addUser(User user){
        repository.addUser(user);
    }

    public void addExercise(Exercise exercise){
        repository.addExercise(exercise);
    }

    public void addHistory(History history){
        repository.addHistory(history);
    }

    public void updateUserCalories(String email, double new_calories){
        repository.updateUserCalories(email, new_calories);
    }

    public void updateUserLevel(String email,String level) {
        repository.updateUserLevel(email,level);
    }

    public LiveData<List<History>> getHistoryByEmail(String email){
        return repository.getHistoryByEmail(email);
    }

    public int checkIfExists(String email){
        return repository.checkIfExists(email);
    }

    public int getSize(){
        return repository.getSize();
    }

    public User getUserByEmailSync(String email){
        return repository.getUserByEmailSync(email);
    }

}
