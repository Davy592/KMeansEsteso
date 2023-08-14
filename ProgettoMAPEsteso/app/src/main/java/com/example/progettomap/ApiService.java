package com.example.progettomap;

import android.os.Parcelable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("connectionInfo")
    Call<List<String>> sendInfoToServer(@Body List<String> list);
    @POST("newClusterSet")
    Call<List<String>> requestNewClusterSet(@Body List<String> numCluster);

    @POST("fileInfo")
    Call<List<String>> sendFileToServer(@Body List<String> list);

    @POST("fileNames")
    Call<List<String>> requestFilesName();
}