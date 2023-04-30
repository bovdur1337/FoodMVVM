package com.example.foodmvvm.main.ui.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodmvvm.R
import com.example.foodmvvm.databinding.ActivityMealBinding
import com.example.foodmvvm.main.db.MealDatabase
import com.example.foodmvvm.main.models.Meal
import com.example.foodmvvm.main.ui.fragments.HomeFragment
import com.example.foodmvvm.main.viewmodel.MealViewModel
import com.example.foodmvvm.main.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealYtLink: String
    private var mealToSave: Meal? = null

    private lateinit var binding: ActivityMealBinding
    private lateinit var viewModel: MealViewModel
//    private val viewModel: MealViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        viewModel = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getting our meal info from intent
        getMealInfoFromIntent()
        setMealInfoFromIntent()

        //turn on our progressBar
        loadingCase()

        //call get request for all other details
        getMealDetailsById()
        observeMealDetails()

        //adding method for clicking yt button
        onFabYtClick()
        //adding method for clicking on fav button
        onFabFavClick()
    }

    private fun getMealInfoFromIntent(){
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun setMealInfoFromIntent(){
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.ivMealDetail)
        binding.tbCollapsing.title = mealName
    }

    private fun getMealDetailsById(){
        viewModel.getMealDetails(mealId)
    }

    private fun observeMealDetails(){
        viewModel.mealDetailsLD.observe(this, Observer { meal ->
            onResponseCase()
            mealToSave = meal

            binding.tvCategory.text = meal.strCategory
            binding.tvCountry.text = meal.strArea
            binding.tvCookingInstructions.text = meal.strInstructions

            mealYtLink = meal.strYoutube.toString()
        })
    }

    private fun loadingCase(){
        binding.apply {
            progressbar.visibility = View.VISIBLE
            fabYT.visibility = View.INVISIBLE
            fabAddToFavs.visibility = View.INVISIBLE
            tvCategory.visibility = View.INVISIBLE
            tvCountry.visibility = View.INVISIBLE
            tvHowtoCook.visibility = View.INVISIBLE
            tvCookingInstructions.visibility = View.INVISIBLE
        }
    }

    private fun onResponseCase(){
        binding.apply {
            progressbar.visibility = View.INVISIBLE
            fabYT.visibility = View.VISIBLE
            fabAddToFavs.visibility = View.VISIBLE
            tvCategory.visibility = View.VISIBLE
            tvCountry.visibility = View.VISIBLE
            tvHowtoCook.visibility = View.VISIBLE
            tvCookingInstructions.visibility = View.VISIBLE
        }
    }

    private fun onFabYtClick(){
        binding.fabYT.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYtLink))
            startActivity(intent)
        }
    }

    private fun onFabFavClick(){
        binding.fabAddToFavs.setOnClickListener{
            mealToSave?.let {
                viewModel.addMealToFavs(it)
                Toast.makeText(this, "Successfully added to Favorites!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}