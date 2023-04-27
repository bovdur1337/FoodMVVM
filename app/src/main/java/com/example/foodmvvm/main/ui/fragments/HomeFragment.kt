package com.example.foodmvvm.main.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodmvvm.databinding.FragmentHomeBinding
import com.example.foodmvvm.main.models.Meal
import com.example.foodmvvm.main.ui.activities.MealActivity
import com.example.foodmvvm.main.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var randomMeal: Meal
    private val viewModel: HomeViewModel by viewModels()

    companion object{
        const val MEAL_ID = "food.mealId"
        const val MEAL_NAME = "food.mealName"
        const val MEAL_THUMB = "food.mealThumb"
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

        //observing our random meal image
        observeRandomMeal()

        //adding method for clicking on random meal
        onRandomMealClick()
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
}