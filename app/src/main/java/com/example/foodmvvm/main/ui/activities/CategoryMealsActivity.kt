package com.example.foodmvvm.main.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodmvvm.R
import com.example.foodmvvm.databinding.ActivityCategoryMealsBinding
import com.example.foodmvvm.databinding.ActivityMealBinding
import com.example.foodmvvm.main.adapters.CategoryMealsAdapter
import com.example.foodmvvm.main.ui.fragments.HomeFragment
import com.example.foodmvvm.main.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsAdapter: CategoryMealsAdapter
    val viewModel: CategoryMealsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareMealsRV()
        viewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        observeMealsListLD()
    }

    private fun prepareMealsRV(){
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMealsByCategory.apply {
            layoutManager = GridLayoutManager(
                context,
                2,
                GridLayoutManager.VERTICAL,
                false
            )

            adapter = categoryMealsAdapter
        }
    }

    private fun observeMealsListLD(){
        viewModel.mealsLD.observe(this, Observer { mealsList ->
            categoryMealsAdapter.setMealsList(mealsList)
        })
    }
}