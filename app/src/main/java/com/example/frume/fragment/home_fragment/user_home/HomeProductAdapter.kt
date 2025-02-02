package com.example.frume.fragment.home_fragment.user_home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frume.data.Product
import com.example.frume.databinding.ItemProductBinding
import com.example.frume.util.convertThreeDigitComma

class HomeProductAdapter(
    private val items: MutableList<Product>,
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

    // 갱신 add(updateList: MutableList<TempProduct>)
    fun add(updateList: MutableList<Product>) {
        items.clear() // 기존 데이터를 초기화
        items.addAll(updateList) // 새로운 데이터를 추가
        notifyDataSetChanged() // 전체 데이터 변경 알림
    }


}

class ProductViewHolder(
    private val binding: ItemProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product, listener: ProductItemClickListener) {
        itemView.setOnClickListener {
            listener.onClickProductItem(product) // 추후 데이터 삽입
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
