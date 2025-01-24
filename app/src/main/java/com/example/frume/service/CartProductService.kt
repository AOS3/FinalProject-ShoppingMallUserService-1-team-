package com.example.frume.service

import com.example.frume.model.CartProductModel
import com.example.frume.model.ProductModel
import com.example.frume.repository.CartProductRepository

class CartProductService {
    companion object{
        // 내장바구니에 상품전체 목록 가져오기 hj
        suspend fun gettingMyCartProductItems(cartDocId: String):MutableList<CartProductModel> {
            val cartProductModelList = mutableListOf<CartProductModel>()
            val result = CartProductRepository.gettingMyCartProductItems(cartDocId)

            result.forEach {
                val cartProductModel = it.toCartProductModel()
                cartProductModelList.add(cartProductModel)
            }

            return cartProductModelList
        }

        // 장바구니에 상품 넣기, 장바구니 서브컬렉션에 넣는다. hj
        suspend fun addMyCartProduct(cartDocId: String, cartProductModel: CartProductModel) {
            CartProductRepository.addMyCartProduct(cartDocId,cartProductModel)

        }

    }
}