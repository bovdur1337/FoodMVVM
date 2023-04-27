package com.example.foodmvvm.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodmvvm.main.models.PopularCategoryList
import com.example.foodmvvm.main.models.PopularCategoryMeals
import com.example.foodmvvm.main.retrofit.RetrofitInstance
import com.example.foodmvvm.main.models.Meal
import com.example.foodmvvm.main.models.MealList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(): ViewModel() {

    private var _randomMealLD = MutableLiveData<Meal>()
    val randomMealLD: LiveData<Meal> = _randomMealLD

    private var _popularItemsLD = MutableLiveData<List<PopularCategoryMeals>>()
    val popularItemsLD: LiveData<List<PopularCategoryMeals>> = _popularItemsLD

    init {
        getRandomMeal()
        getPopularItems()
    }

    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    _randomMealLD.value = response.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Beef").enqueue(object : Callback<PopularCategoryList>{
            override fun onResponse(call: Call<PopularCategoryList>, response: Response<PopularCategoryList>) {
                if (response.body() != null){
                    _popularItemsLD.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<PopularCategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }
}