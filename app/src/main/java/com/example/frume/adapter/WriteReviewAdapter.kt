package com.example.frume.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frume.R
import com.example.frume.data.Review
import com.example.frume.databinding.ItemAddImageBinding
import com.example.frume.listener.WriteReviewClickListener

class WriteReviewAdapter(
    private val listener: WriteReviewClickListener
) : RecyclerView.Adapter<ReviewWriteViewHolder>() {
    private val imageList = mutableListOf<Uri>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewWriteViewHolder {
        return ReviewWriteViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ReviewWriteViewHolder, position: Int) {
        holder.bind(imageList[position], listener)
    }

    fun removeImage(pos: Int) {
        imageList.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun changeImage(newImage: Uri, pos: Int) {
        imageList[pos] = newImage
        notifyDataSetChanged()
    }

    fun getImage(): MutableList<Review> {
        val stringList = imageList.map { uri ->
            Review(
                reviewImage = uri
            )
        }
        return stringList.toMutableList()
    }

    fun addImage(newImages: List<Uri>, pos: Int) {
        Log.d("addImage", newImages.toString())
        if (pos in imageList.indices) {
            // 기존 이미지를 교체
            newImages.forEachIndexed { index, uri ->
                if (pos + index < imageList.size) {
                    imageList[pos + index] = uri
                    notifyItemChanged(pos + index)
                } else {
                    imageList.add(uri)
                    notifyItemInserted(imageList.size - 1)
                }
            }
        } else {
            // 새 이미지 추가
            val startPos = imageList.size
            imageList.addAll(newImages)
            notifyItemRangeInserted(startPos, newImages.size)
        }
    }
}

class ReviewWriteViewHolder(
    private val binding: ItemAddImageBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(imgUri: Uri, listener: WriteReviewClickListener) {
        Log.d("bind", imgUri.toString())
        if (imgUri.toString().isNotEmpty()) {
            Glide.with(binding.root.context)
                .load(imgUri)
                .into(binding.imageViewItemAddImage)
        } else {
            // 기본 이미지 설정 (필요 시 리소스 제공)
            binding.imageViewItemAddImage.setImageResource(R.drawable.img_fruit)
        }

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

