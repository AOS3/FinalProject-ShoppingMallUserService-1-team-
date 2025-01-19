package com.example.frume.fragment.home_fragment.user_home


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.frume.R
import com.example.frume.data.TempBanner
import com.example.frume.databinding.ItemBannerBinding

// 홈 화면 최상단 탭 레이아웃 어댑터

class HomeBannerAdapter(
    private val bannerList: List<TempBanner>
) : RecyclerView.Adapter<BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder.from(parent)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE // Item의 개수를 임의로 늘려준다.

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerList[position % 5])// position에 list.size 을 나눈 나머지 값을 사용한다.
    }
}

class BannerViewHolder(
    private val binding: ItemBannerBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tempBanner: TempBanner) {
        binding.ivItem.setImageResource(tempBanner.bannerResourceId)
    }

    companion object {
        fun from(parent: ViewGroup): BannerViewHolder {
            return BannerViewHolder(
                ItemBannerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}