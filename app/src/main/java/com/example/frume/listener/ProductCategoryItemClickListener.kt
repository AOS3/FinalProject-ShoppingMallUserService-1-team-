package com.example.frume.listener

import com.example.frume.model.ProductModel


interface ProductCategoryItemClickListener {
    fun onClickProductItem(product: ProductModel)
}