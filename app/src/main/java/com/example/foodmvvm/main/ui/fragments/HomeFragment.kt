package com.example.foodmvvm.main.ui.fragments

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
import com.example.foodmvvm.main.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

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
        observeRandomMeal()
    }

    private fun observeRandomMeal(){
        viewModel.randomMealLD.observe(viewLifecycleOwner, Observer { randomMeal ->
                Glide.with(this@HomeFragment)
                    .load(randomMeal.strMealThumb)
                    .into(binding.ivRandomMeal)
        })
    }
}