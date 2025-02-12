package com.example.frume.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.data.MyReviewParent
import com.example.frume.databinding.ItemReviewBinding

class ProductReviewAdapter(
    private val items: MutableList<MyReviewParent>,
    //private val listener: ReviewClickListener
) : RecyclerView.Adapter<ProductReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewViewHolder {
        return ProductReviewViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<MyReviewParent>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged() // 데이터 변경 알림
    }

    fun addItems(newItems: List<MyReviewParent>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    override fun onBindViewHolder(holder: ProductReviewViewHolder, position: Int) {
        // holder.bind(items[position], listener)
        holder.bind(items[position])
    }

}

class ProductReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
    //fun bind(review: TempReview, listener: ReviewClickListener) {
    fun bind(review: MyReviewParent) {
        itemView.setOnClickListener {
            // listener.onClickProductItem(review)
        }
        with(binding) {
//            ratingBarItemReview.rating = review.starPoint.toFloat()
//            imageViewItemReview.setImageResource(review.reviewImg)
//            textViewRecyclerViewReviewTitle.text = review.reviewTitle
//            textViewRecyclerViewReviewText.text = review.reviewBody
//            textViewRecyclerViewReviewDate.text = review.reviewDate.toString()
//            textViewRecyclerViewRemoveReview.visibility = View.GONE
        }
    }

    companion object {
        fun from(parent: ViewGroup): ProductReviewViewHolder {
            return ProductReviewViewHolder(
                ItemReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

}
