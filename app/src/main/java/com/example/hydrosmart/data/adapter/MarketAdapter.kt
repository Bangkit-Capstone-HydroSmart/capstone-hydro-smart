package com.example.hydrosmart.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hydrosmart.data.Market
import com.example.hydrosmart.databinding.ItemMarketBinding

class MarketAdapter(
    private val listMarket: ArrayList<Market>
): RecyclerView.Adapter<MarketAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MarketAdapter.ListViewHolder {
        val binding = ItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarketAdapter.ListViewHolder, position: Int) {
        holder.bind(listMarket[position])
    }

    override fun getItemCount(): Int = listMarket.size

    inner class ListViewHolder(
        private var binding: ItemMarketBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(market: Market) {
            binding.apply {
                tvTitle.text = market.title
                tvPrice.text = market.price
            }
        }
    }
}