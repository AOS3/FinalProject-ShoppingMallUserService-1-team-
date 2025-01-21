package com.example.frume.fragment.user_fragment.product_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.data.TempReview
import com.example.frume.databinding.ItemReviewBinding

class ProductReviewAdapter(
    private val items: MutableList<TempReview>,
    private val listener: ReviewClickListener
) : RecyclerView.Adapter<ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<TempReview>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged() // 데이터 변경 알림
    }

    fun addItems(newItems: List<TempReview>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

}

class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(review: TempReview, listener: ReviewClickListener) {
        itemView.setOnClickListener {
            listener.onClickProductItem(review)
        }
        with(binding) {
            ratingBarItemReview.rating = review.starPoint.toFloat()
            imageViewRecyclerViewReview.setImageResource(review.reviewImg)
            textViewRecyclerViewReviewTitle.text = review.reviewTitle
            textViewRecyclerViewReviewText.text = review.reviewBody
            textViewRecyclerViewReviewDate.text = review.reviewDate.toString()
        }
    }

    companion object {
        fun from(parent: ViewGroup): ReviewViewHolder {
            return ReviewViewHolder(
                ItemReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

}
