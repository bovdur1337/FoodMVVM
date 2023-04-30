package com.example.foodmvvm.main.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodmvvm.databinding.ActivityCategoryMealsBinding
import com.example.foodmvvm.main.adapters.CategoryMealsAdapter
import com.example.foodmvvm.main.ui.fragments.HomeFragment
import com.example.foodmvvm.main.viewmodel.categorymealsvm.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    private val viewModel: CategoryMealsViewModel by viewModels()

    private lateinit var categoryName: String
    private lateinit var categoryMealsCount: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!

        //preparing our rv
        prepareMealsRV()
        //getting our meals
        getMealsByCategory()
        //observing mealsLD for changes
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

    private fun getMealsByCategory() {
        viewModel.getMealsByCategory(categoryName)
    }

    private fun observeMealsListLD(){
        viewModel.mealsLD.observe(this, Observer { mealsList ->
            categoryMealsCount = mealsList.size.toString()
            binding.tvCategoryCount.text = "$categoryName: $categoryMealsCount meals found"
            categoryMealsAdapter.setMealsList(mealsList)
        })
    }
}