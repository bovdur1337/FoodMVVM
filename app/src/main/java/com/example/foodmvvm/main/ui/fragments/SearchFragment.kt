package com.example.foodmvvm.main.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodmvvm.databinding.FragmentSearchBinding
import com.example.foodmvvm.main.adapters.MealsAdapter
import com.example.foodmvvm.main.ui.activities.MainActivity
import com.example.foodmvvm.main.viewmodel.homevm.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRVAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //preparing our rv
        prepareMealsRV()

        //adding method for clicking on search button
        onSearchClick()

        //observing our livedata with searched meals
        observeSearchedMealsLD()

        var searchJob: Job? = null
        binding.etSearch.addTextChangedListener{ searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeals(searchQuery.toString())
            }
        }
    }

    private fun prepareMealsRV(){
        searchRVAdapter = MealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(
                context,
                2,
                GridLayoutManager.VERTICAL,
                false
            )
            adapter = searchRVAdapter
        }
    }

    private fun onSearchClick(){
        binding.ivSearch.setOnClickListener{
            val searchQuery = binding.etSearch.text.toString()
            if (searchQuery.isNotEmpty()){
                viewModel.searchMeals(searchQuery)
            }
        }
    }

    private fun observeSearchedMealsLD(){
        viewModel.searchedMealsLD.observe(viewLifecycleOwner, Observer { searchedMealsList ->
            searchRVAdapter.differ.submitList(searchedMealsList)

        })
    }
}