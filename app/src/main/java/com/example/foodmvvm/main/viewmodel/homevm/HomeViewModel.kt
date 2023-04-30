package com.example.foodmvvm.main.viewmodel.homevm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodmvvm.main.db.MealDatabase
import com.example.foodmvvm.main.models.Category
import com.example.foodmvvm.main.models.CategoryList
import com.example.foodmvvm.main.models.MealsByCategoryList
import com.example.foodmvvm.main.models.MealsByCategory
import com.example.foodmvvm.main.retrofit.RetrofitInstance
import com.example.foodmvvm.main.models.Meal
import com.example.foodmvvm.main.models.MealList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
): ViewModel() {

    private var _randomMealLD = MutableLiveData<Meal>()
    val randomMealLD: LiveData<Meal> = _randomMealLD

    private var _popularItemsLD = MutableLiveData<List<MealsByCategory>>()
    val popularItemsLD: LiveData<List<MealsByCategory>> = _popularItemsLD

    private var _categoriesListLD = MutableLiveData<List<Category>>()
    val categoriesListLD: LiveData<List<Category>> = _categoriesListLD

    private var _favMealsLD = mealDatabase.mealDao().getAllMeals()
    val favMeals: LiveData<List<Meal>> = _favMealsLD

    private var _bsMealLD = MutableLiveData<Meal>()
    val bsMealLD: LiveData<Meal> = _bsMealLD

    init {
        getRandomMeal()
        getPopularItems()
        getCategories()
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
        RetrofitInstance.api.getPopularItems("Beef").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null){
                    _popularItemsLD.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null){
                    _categoriesListLD.value = response.body()!!.categories
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun deleteMealFromFavs(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun addMealToFavs(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun getMealById(id: String){
        RetrofitInstance.api.getMealDetailsById(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal ->
                    _bsMealLD.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }
}