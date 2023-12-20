package com.example.hydrosmart.afterlogin.ui.market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hydrosmart.R
import com.example.hydrosmart.data.Market
import com.example.hydrosmart.data.adapter.MarketAdapter
import com.example.hydrosmart.databinding.ActivityMarketBinding

class MarketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMarketBinding
    private val list = ArrayList<Market>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecyclerView()
        binding.fbBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private val marketList: ArrayList<Market>
        get() {
            val title = resources.getString(R.string.title_market)
            val price = resources.getString(R.string.price_market)

            return ArrayList<Market>().apply {
                for (i in 0..15) {
                    add(
                        Market(
                            title,
                            price
                        )
                    )
                }
            }
        }

    private fun showRecyclerView() {
        list.addAll(marketList)
        val marketAdapter = MarketAdapter(list)
        binding.rvMarket.apply {
            layoutManager = GridLayoutManager(this@MarketActivity, 2)
            adapter = marketAdapter
            setHasFixedSize(true)
        }
    }
}