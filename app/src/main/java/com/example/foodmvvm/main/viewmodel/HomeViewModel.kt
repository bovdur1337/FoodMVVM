package com.example.foodmvvm.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodmvvm.main.models.CategoryList
import com.example.foodmvvm.main.models.CategoryMeals
import com.example.foodmvvm.main.retrofit.RetrofitInstance
import com.example.foodmvvm.main.models.Meal
import com.example.foodmvvm.main.models.MealList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(): ViewModel() {

    private var _randomMealLD = MutableLiveData<Meal>()
    val randomMealLD: LiveData<Meal> = _randomMealLD

    private var _popularItemsLD = MutableLiveData<List<CategoryMeals>>()
    val popularItemsLD: LiveData<List<CategoryMeals>> = _popularItemsLD


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
        RetrofitInstance.api.getPopularItems("Beef").enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null){
                    _popularItemsLD.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }
}