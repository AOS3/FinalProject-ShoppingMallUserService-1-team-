package com.example.frume.fragment.user_fragment.product_info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.data.Inquiry
import com.example.frume.databinding.ItemInquiryBinding

class ProductInquiryAdapter(
    private val listener: InquiryClickListener
) : RecyclerView.Adapter<InquiryViewHolder>() {
    private val items = mutableListOf<Inquiry>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiryViewHolder {
        return InquiryViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: InquiryViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun updateList(list: MutableList<Inquiry>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}

class InquiryViewHolder(private val binding: ItemInquiryBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(inquiry: Inquiry, listener: InquiryClickListener) {
        with(binding) {
            if (inquiry.secret) {
                ivLock.visibility = View.VISIBLE
            } else {
                ivLock.visibility = View.GONE
            }
            tvInquiryType.text = inquiry.type
            tvInquiryTitle.text = inquiry.title
            tvInquiryDate.text = inquiry.date
            tvInquiryAnswerState.text = inquiry.answerState
            tvInquiryName.text = inquiry.name
        }
        itemView.setOnClickListener {
            listener.onClickInquiry(inquiry)
        }
    }

    companion object {
        fun from(parent: ViewGroup): InquiryViewHolder {
            return InquiryViewHolder(
                ItemInquiryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}