package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitGeoapifyInstance {

    private static Retrofit getRetrofit(String base_url) {
        return new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static GeoapifyAPI getApi(String base_url) {
        return getRetrofit(base_url).create(GeoapifyAPI.class);
    }
}
