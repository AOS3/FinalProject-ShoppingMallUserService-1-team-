package com.example.frume.fragment.home_fragment.user_home

import com.example.frume.data.Product
import com.example.frume.model.ProductModel


interface ProductItemClickListener {
    fun onClickProductItem(product: Product)
}