package com.example.frume.fragment.user_fragment.product_info

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frume.databinding.ItemAddImageBinding

class WriteReviewAdapter(
    private val imageList: MutableList<Uri>,
    private val listener: WriteReviewClickListener
) : RecyclerView.Adapter<ReviewWriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewWriteViewHolder {
        return ReviewWriteViewHolder.from(parent)
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ReviewWriteViewHolder, position: Int) {
        holder.bind(imageList[position], listener)
    }


    fun addImage(newImages: List<Uri>, pos: Int) {
        imageList.addAll(newImages)
        notifyItemRangeInserted(pos, newImages.size)
    }
}

class ReviewWriteViewHolder(
    private val binding: ItemAddImageBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(imgUri: Uri, listener: WriteReviewClickListener) {
        Glide.with(binding.root.context)
            .load(imgUri)
            .into(binding.imageViewItemAddImage)

        binding.tvTest.text = "1"
        binding.imageViewItemAddImage.setOnClickListener {
            listener.onClickReviewImg(adapterPosition)
        }
        binding.imageViewRemoveBtn.setOnClickListener {
            listener.onClickRemoveBtn(adapterPosition)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ReviewWriteViewHolder {
            return ReviewWriteViewHolder(
                ItemAddImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}

