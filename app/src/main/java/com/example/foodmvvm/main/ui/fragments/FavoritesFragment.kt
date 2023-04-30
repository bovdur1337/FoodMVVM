package com.example.foodmvvm.main.ui.fragments

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmvvm.databinding.FragmentFavoritesBinding
import com.example.foodmvvm.main.adapters.FavoriteMealsAdapter
import com.example.foodmvvm.main.ui.activities.MainActivity
import com.example.foodmvvm.main.viewmodel.homevm.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {

    private lateinit var favoriteMealsAdapter: FavoriteMealsAdapter
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        favoriteMealsAdapter = FavoriteMealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //preparing our rv
        prepareFavMealsRV()

        //observing our db for fav meals
        observeFavMeals()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currentMeal = favoriteMealsAdapter.differ.currentList[position]
                viewModel.deleteMealFromFavs(currentMeal)

                Snackbar.make(requireView(), "Meal Deleted!", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.addMealToFavs(currentMeal)
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavMeals)
    }

    private fun prepareFavMealsRV(){
        binding.rvFavMeals.apply {
            layoutManager = GridLayoutManager(
                context,
                2,
                GridLayoutManager.VERTICAL,
                false
            )
            adapter = favoriteMealsAdapter
        }

    }

    private fun observeFavMeals(){
        viewModel.favMeals.observe(viewLifecycleOwner, Observer { meals ->
            favoriteMealsAdapter.differ.submitList(meals)
        })
    }
}