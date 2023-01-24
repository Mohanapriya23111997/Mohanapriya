package com.example.androidtask

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET()
    suspend fun product(@Url url:String): Response<ResponseBody>
}