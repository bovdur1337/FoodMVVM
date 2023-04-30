package com.example.foodmvvm.main.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.foodmvvm.R
import com.example.foodmvvm.main.db.MealDatabase
import com.example.foodmvvm.main.viewmodel.homevm.HomeViewModel
import com.example.foodmvvm.main.viewmodel.homevm.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProvideFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelProvideFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNav)
        val navController = Navigation.findNavController(this, R.id.hostFragment)

        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}