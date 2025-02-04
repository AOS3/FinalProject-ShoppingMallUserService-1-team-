package com.example.frume.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.databinding.ItemAddImageBinding

class MyReviewChildAdapter(
) : RecyclerView.Adapter<ReviewChildViewHolder>() {
    private val childList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewChildViewHolder {
        return ReviewChildViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return childList.size
    }

    override fun onBindViewHolder(holder: ReviewChildViewHolder, position: Int) {
        holder.bind(childList[position])
    }

    fun add(imageList: List<String>) {
        childList.clear()
        childList.addAll(imageList)
        notifyDataSetChanged()
    }
}

class ReviewChildViewHolder(
    private val binding: ItemAddImageBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: String) {
        binding.image = item
        binding.imageViewRemoveBtn.visibility = View.GONE
    }

    companion object {
        fun from(parent: ViewGroup): ReviewChildViewHolder {
            return ReviewChildViewHolder(
                ItemAddImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}