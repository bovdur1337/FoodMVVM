package com.example.foodmvvm.main.retrofit

import com.example.foodmvvm.main.models.MealList
import retrofit2.Call
import retrofit2.http.GET

interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Call<MealList>
}