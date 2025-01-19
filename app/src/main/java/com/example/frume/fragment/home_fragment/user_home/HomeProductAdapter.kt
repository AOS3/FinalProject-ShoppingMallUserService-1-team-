package com.example.frume.fragment.home_fragment.user_home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.data.TempProduct
import com.example.frume.databinding.ItemProductBinding

class HomeProductAdapter(
    private val items: MutableList<TempProduct>,
    private val listener: ProductItemClickListener
) : RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    // 갱신
    fun add(updateList: MutableList<TempProduct>) {
        val posStart = items.size
        items.clear()
        items.addAll(updateList)
        notifyItemRangeChanged(posStart,updateList.size-posStart)
    }
}

class ProductViewHolder(
    private val binding: ItemProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: TempProduct, listener: ProductItemClickListener) {
        itemView.setOnClickListener {
            listener.onClickProductItem(product) // 추후 데이터 삽입
        }
        with(binding) {
            imageViewItemProductThumbNail.setImageResource(product.productImgResourceId)
            textViewItemProductTitle.text = product.productName
            textViewItemProductDescription.text = product.productDescription
        }
    }

    companion object {
        fun from(parent: ViewGroup): ProductViewHolder {
            return ProductViewHolder(
                ItemProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
