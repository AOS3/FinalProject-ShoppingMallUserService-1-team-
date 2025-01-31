package com.example.frume.fragment.home_fragment.user_home
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.databinding.ItemProductBinding
import com.example.frume.model.ProductModel

class HomeProductAdapter(
    private val items: MutableList<ProductModel>,
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
    fun add(updateList: MutableList<ProductModel>) {

        Log.d("test111", "!! updateList size: ${updateList.size}") // 업데이트 리스트 크기 확인
        Log.d("test111", "!! updateList contents: $updateList") // 업데이트 리스트 내용 확인

        items.clear() // 기존 데이터를 초기화
        items.addAll(updateList) // 새로운 데이터를 추가
        notifyDataSetChanged() // 전체 데이터 변경 알림

        // 디버깅 로그
        Log.d("test111", "Updated items: $items")
    }


}

class ProductViewHolder(
    private val binding: ItemProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: ProductModel, listener: ProductItemClickListener) {
        itemView.setOnClickListener {
            listener.onClickProductItem(product) // 추후 데이터 삽입
        }
        with(binding) {
            // imageViewItemProductThumbNail.setImageResource(product)
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
