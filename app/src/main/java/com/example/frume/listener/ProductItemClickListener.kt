package com.example.frume.listener

import com.example.frume.data.Product


interface ProductItemClickListener {
    fun onClickProductItem(product: Product)
}