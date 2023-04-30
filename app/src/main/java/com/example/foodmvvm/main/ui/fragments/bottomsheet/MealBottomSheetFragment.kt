package com.example.foodmvvm.main.ui.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodmvvm.databinding.FragmentMealBottomSheetBinding
import com.example.foodmvvm.main.ui.activities.MainActivity
import com.example.foodmvvm.main.ui.activities.MealActivity
import com.example.foodmvvm.main.ui.fragments.HomeFragment
import com.example.foodmvvm.main.viewmodel.homevm.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private var mealId: String? = null
    private var mealName: String? = null
    private var mealThumb: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //calling a method for get our meal information
        getMealById()

        //creating a method to observe our changes in viewModel
        observeBsMeal()

        //adding a method for clicking on our bottom sheet dialog
        onBsDialogClick()
    }

    private fun getMealById(){
        mealId?.let { viewModel.getMealById(it) }
    }

    private fun observeBsMeal(){
        viewModel.bsMealLD.observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(this)
                .load(meal.strMealThumb)
                .into(binding.ivMeal)
            binding.tvMealName.text = meal.strMeal
            binding.tvMealLocation.text = meal.strArea
            binding.tvMealCategory.text = meal.strCategory

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
         })
    }

    private fun onBsDialogClick(){
        binding.bottomSheet.setOnClickListener{
            if (mealName!=null && mealThumb!=null){
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply{
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, mealName)
                    putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                }
                startActivity(intent)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}