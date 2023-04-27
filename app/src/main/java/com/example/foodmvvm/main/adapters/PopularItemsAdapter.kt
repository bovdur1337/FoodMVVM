package com.example.foodmvvm.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodmvvm.databinding.FragmentHomeBinding
import com.example.foodmvvm.databinding.PopularItemBinding
import com.example.foodmvvm.main.models.CategoryMeals

class PopularItemsAdapter(): RecyclerView.Adapter<PopularItemsAdapter.PopularItemViewHolder>() {

    lateinit var onItemClick: ((CategoryMeals) -> Unit)
    private var mealsList = ArrayList<CategoryMeals>()

    class PopularItemViewHolder(var binding: PopularItemBinding): RecyclerView.ViewHolder(binding.root)

    fun setMeals(mealsList: ArrayList<CategoryMeals>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularItemViewHolder {
        return PopularItemViewHolder(PopularItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: PopularItemViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.ivPopularItem)

        holder.itemView.setOnClickListener{ onItemClick.invoke(mealsList[position]) }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}