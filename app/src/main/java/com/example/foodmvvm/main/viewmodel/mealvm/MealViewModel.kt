package com.example.foodmvvm.main.viewmodel.mealvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodmvvm.main.db.MealDatabase
import com.example.foodmvvm.main.models.Meal
import com.example.foodmvvm.main.models.MealList
import com.example.foodmvvm.main.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    private val mealDatabase: MealDatabase
): ViewModel() {

    private var _mealDetailsLD = MutableLiveData<Meal>()
    val mealDetailsLD: LiveData<Meal> = _mealDetailsLD

    fun getMealDetails(id: String){
        RetrofitInstance.api.getMealDetailsById(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    _mealDetailsLD.value = response.body()!!.meals[0]
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }
        })
    }

    fun addMealToFavs(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
}