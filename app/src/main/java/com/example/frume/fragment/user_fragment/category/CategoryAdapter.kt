package com.example.frume.fragment.user_fragment.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frume.databinding.ItemProductBinding
import com.example.frume.model.ProductModel
import com.example.frume.util.convertThreeDigitComma

class CategoryAdapter(
    private val listener: ProductCategoryItemClickListener
) : RecyclerView.Adapter<CategoryViewHolder>() {
    val items = mutableListOf<ProductModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }
    fun updateList(newList: List<ProductModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}

class CategoryViewHolder(
    private val binding: ItemProductBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: ProductModel, listener: ProductCategoryItemClickListener) {
        itemView.setOnClickListener {
            listener.onClickProductItem(product)
        }
        with(binding) {
            Glide.with(imageViewItemProductThumbNail.context)
                .load(product.productImages[0])
                .into(imageViewItemProductThumbNail)
            textViewItemProductTitle.text = product.productName
            textViewItemProductDescription.text = product.productDescription
            textViewItemProductPrice.text = product.productPrice.convertThreeDigitComma()
        }
    }

    companion object {
        fun from(parent: ViewGroup): CategoryViewHolder {
            return CategoryViewHolder(
                ItemProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}