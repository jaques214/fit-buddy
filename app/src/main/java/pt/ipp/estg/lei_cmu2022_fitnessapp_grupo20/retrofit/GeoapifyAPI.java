package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoapifyAPI {
    @GET("places")
    Call<ObjectWrapper> getGymsByRadius(@Query("categories") String categories,
                                        @Query(value = "filter", encoded = true) String filter,
                                        @Query(value = "bias", encoded = true) String bias,
                                        @Query("limit") int limit,
                                        @Query("apiKey") String apiKey);
    @GET("weather")
    Call<Weather> getCurrentWeather(@Query("lat") double lat,
                                    @Query("lon") double lon,
                                    @Query("units") String units,
                                    @Query("appid") String appid);
}
