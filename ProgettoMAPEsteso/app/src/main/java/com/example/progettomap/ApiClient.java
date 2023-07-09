package com.example.progettomap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static String BASE_URL = null;

    public static void setBaseUrl(String server, int port)
    {
        BASE_URL = "http://" + server + ":" + port + "/";
    }

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void closeClient()
    {
        retrofit = null;
    }
}