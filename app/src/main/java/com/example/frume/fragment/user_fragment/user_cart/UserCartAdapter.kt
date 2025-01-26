package com.example.frume.fragment.user_fragment.user_cart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.data_ye.TempCartProduct
import com.example.frume.databinding.ItemUsercartListBinding
import com.example.frume.util.applyNumberFormat

class UserCartAdapter(
    private val items: MutableList<TempCartProduct>,
    private val listener: CartClickListener
) : RecyclerView.Adapter<CartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun getTotalPrice(): Int {
        var totalPrice = 0
        items.forEach {
            totalPrice += it.productPrice*it.quantity
        }
        Log.d("TEST", totalPrice.toString())

        return totalPrice
    }

    fun deleteSelectedItems(): Boolean {
        val removedIndices = items.mapIndexedNotNull { index, item ->
            if (item.productCheck) index else null
        }

        // 역순으로 삭제 (리스트의 인덱스가 변동되지 않도록)
        removedIndices.asReversed().forEach { index ->
            items.removeAt(index)
            notifyItemRemoved(index)
        }

        return items.isEmpty()
    }

    fun onClickAdd(pos: Int) {
        items[pos].quantity += 1
        items[pos].productCheck
        notifyItemChanged(pos)
    }

    fun onClickMinus(pos: Int) {
        items[pos].quantity -= 1
        notifyItemChanged(pos)
    }

    fun onClickAllCheckBox(checked: Boolean) {
        items.forEach { it.productCheck = checked } // 모든 아이템 체크 상태 변경
        notifyDataSetChanged()
    }

    // sehoon 값을 리턴해서 fragment의 체크박스 상태를 변경
    fun isCheckBox(): Boolean {
       return items.all { it.productCheck }
    }

    fun isCheckBoxAny(): Boolean {
        return items.any { it.productCheck }

    }

}

class CartViewHolder(
    private val binding: ItemUsercartListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cart: TempCartProduct, listener: CartClickListener) {
        with(binding) {
            textViewRecyclerViewProductName.text = cart.productName
            textViewRecyclerViewProductPrice.applyNumberFormat(cart.productPrice * cart.quantity)
            imageViewRecyclerViewImage.setImageResource(cart.imageResId)
            /*editTextProductCount.setText("${cart.quantity}")*/
            checkboxRecyclerViewSelect.isChecked = cart.productCheck

        /*    imageViewRecyclerViewAdd.setOnClickListener {
                listener.onClickAdd(adapterPosition, cart)
            }
            imageViewRecyclerViewRemove.setOnClickListener {
                listener.onClickMinus(adapterPosition, cart)
            }*/
            checkboxRecyclerViewSelect.setOnClickListener {
                listener.onClickItemCheckBox(adapterPosition, cart)
            }
        }
    }


    companion object {
        fun from(parent: ViewGroup): CartViewHolder {
            return CartViewHolder(
                ItemUsercartListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
