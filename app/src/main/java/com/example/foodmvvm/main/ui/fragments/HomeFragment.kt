package com.example.foodmvvm.main.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodmvvm.databinding.FragmentHomeBinding
import com.example.foodmvvm.main.adapters.PopularItemsAdapter
import com.example.foodmvvm.main.models.PopularCategoryMeals
import com.example.foodmvvm.main.models.Meal
import com.example.foodmvvm.main.ui.activities.MealActivity
import com.example.foodmvvm.main.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: PopularItemsAdapter
    private val viewModel: HomeViewModel by viewModels()

    companion object{
        const val MEAL_ID = "food.mealId"
        const val MEAL_NAME = "food.mealName"
        const val MEAL_THUMB = "food.mealThumb"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popularItemsAdapter = PopularItemsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //preparing our rv
        preparePopularItemsRV()

        //observing our random meal image
        observeRandomMeal()
        //adding method for clicking on random meal
        onRandomMealClick()

        //observing our popular items list
        observePopularItems()
        //adding method for clicking on popular item
        onPopularItemClick()
    }

    private fun preparePopularItemsRV(){
        binding.rvPopularItems.apply {
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = popularItemsAdapter
        }
    }

    private fun observeRandomMeal(){
        viewModel.randomMealLD.observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(this@HomeFragment)
                    .load(meal.strMealThumb)
                    .into(binding.ivRandomMeal)
            this.randomMeal = meal
        })
    }

    private fun onRandomMealClick(){
        binding.cvRandomMeal.setOnClickListener{
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observePopularItems(){
        viewModel.popularItemsLD.observe(viewLifecycleOwner, Observer { mealsList ->
            popularItemsAdapter.setMeals(mealsList = mealsList as ArrayList<PopularCategoryMeals>)
        })
    }

    private fun onPopularItemClick(){
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }
}