package com.example.frume.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.databinding.ItemProductInfoImageCarouselBinding

class ProductCarouselAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<CarouselViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(items[position])
    }

}

class CarouselViewHolder(private val binding: ItemProductInfoImageCarouselBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(image: String) {
        binding.image = image
    }

    companion object {
        fun from(parent: ViewGroup): CarouselViewHolder {
            return CarouselViewHolder(
                ItemProductInfoImageCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}

