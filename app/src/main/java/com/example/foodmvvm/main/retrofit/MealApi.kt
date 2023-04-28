package com.example.foodmvvm.main.retrofit

import com.example.foodmvvm.main.models.CategoryList
import com.example.foodmvvm.main.models.PopularCategoryList
import com.example.foodmvvm.main.models.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDetailsById(@Query("i") id: String): Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName: String): Call<PopularCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>
}