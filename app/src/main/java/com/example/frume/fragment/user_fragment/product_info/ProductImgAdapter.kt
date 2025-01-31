package com.example.frume.fragment.user_fragment.product_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.frume.databinding.ItemProductInfoImageBinding
import com.example.frume.model.ProductModel

class ProductImgAdapter(
    private val items: MutableList<ProductModel>
) : RecyclerView.Adapter<ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class ImageViewHolder(private val binding: ItemProductInfoImageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(img: ProductModel) {
        itemView.setOnClickListener {

        }
        with(binding) {
            // imageViewItemProductInfoImage.setImageResource(img.productImages[0)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ImageViewHolder {
            return ImageViewHolder(
                ItemProductInfoImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
