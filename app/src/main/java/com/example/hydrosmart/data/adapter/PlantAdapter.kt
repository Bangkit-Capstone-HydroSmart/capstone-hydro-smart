package com.example.hydrosmart.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hydrosmart.databinding.ItemPlantsBinding

class PlantAdapter(
    private val plantList: ArrayList<String>,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<PlantAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlantAdapter.ListViewHolder {
        val binding = ItemPlantsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlantAdapter.ListViewHolder,
        position: Int
    ) {
        holder.bind(plantList[position])
    }

    override fun getItemCount(): Int = plantList.size

    inner class ListViewHolder(
        private var binding: ItemPlantsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: String) {
            binding.itemPlants.text = plant
            itemView.setOnClickListener {
                onClick(plant)
            }
        }
    }
}