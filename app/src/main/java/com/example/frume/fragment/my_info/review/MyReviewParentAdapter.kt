package com.example.frume.fragment.my_info.review

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.data.MyReviewParent
import com.example.frume.databinding.ItemReviewBinding

class MyReviewParentAdapter(
    private val userDocId: String,
    private val listener: ReviewClickListener
) : RecyclerView.Adapter<ReviewParentViewHolder>() {
    private val parentList = mutableListOf<MyReviewParent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewParentViewHolder {
        return ReviewParentViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return parentList.size
    }

    override fun onBindViewHolder(holder: ReviewParentViewHolder, position: Int) {
        holder.bind(parentList[position], userDocId, listener)
    }

    fun submitReview(list: List<MyReviewParent>) {
        parentList.clear()
        parentList.addAll(list)
        notifyDataSetChanged()
    }
}

class ReviewParentViewHolder(
    private val binding: ItemReviewBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val nestedAdapter = MyReviewChildAdapter()

    init {
        binding.recyclerViewItemReview.adapter = nestedAdapter
    }

    fun bind(item: MyReviewParent, userDocId: String, listener: ReviewClickListener) {
        binding.myReview = item
        nestedAdapter.add(item.reviewImagesPath)
        binding.listener = listener
        if (userDocId == item.reviewCustomerDocId) {
            binding.textViewRecyclerViewRemoveReview.visibility = View.VISIBLE
        } else {
            binding.textViewRecyclerViewRemoveReview.visibility = View.GONE
        }
    }

    companion object {
        fun from(parent: ViewGroup): ReviewParentViewHolder {
            return ReviewParentViewHolder(
                ItemReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
