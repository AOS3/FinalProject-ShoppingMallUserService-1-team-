package com.example.frume.service

import android.util.Log
import com.example.frume.model.CartProductModel
import com.example.frume.model.ProductModel
import com.example.frume.repository.CartProductRepository
import com.example.frume.vo.CartProductVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartProductService {
    companion object {
        // 내장바구니에 상품전체 목록 가져오기 hj
        suspend fun gettingMyCartProductItems(cartDocId: String): MutableList<CartProductModel> {
            val cartProductModelList = mutableListOf<CartProductModel>()
            val result = CartProductRepository.gettingMyCartProductItems(cartDocId)

            result.forEach {
                val cartProductModel = it.toCartProductModel()
                cartProductModelList.add(cartProductModel)
            }

            return cartProductModelList
        }

        // 내 장바구니에서 선택된 상품 목록 가져오기
        suspend fun gettingMyCartProductCheckedItems(cartDocId: String): MutableList<CartProductModel> {
            // 리스트를 받아서, 상태값이 true인것만 골라 리턴한다.
           val checkedList = gettingMyCartProductItems(cartDocId).filter { it.cartItemIsCheckState.bool==true }.toMutableList()

            return checkedList
        }

        // 장바구니에 상품 넣기, 장바구니 서브컬렉션에 넣는다. hj
        suspend fun addMyCartProduct(cartDocId: String, cartProductModel: CartProductModel) {
            CartProductRepository.addMyCartProduct(cartDocId, cartProductModel)

        }

        // 내장바구니에 상품 목록 1개 가져오기 hj
        suspend fun gettingMyCartProductItem(
            cartDocId: String,
            cartProductDocId: String
        ): CartProductModel {
            var cartProductModel = CartProductModel()
            val result = CartProductRepository.gettingMyCartProductItem(cartDocId, cartProductDocId)


            cartProductModel = result.toCartProductModel()

            return cartProductModel
        }

        // 내 장바구니 옵션 변경하기
        suspend fun changeCartProductOption(cartDocId: String, cartProductDocId: String, cartProductModel : CartProductModel) {
            // 업데이트
            CartProductRepository.changeCartProductOption(cartDocId, cartProductDocId, cartProductModel.toCartProductVO())
        }

        // 장바구니에서 품목 제거
        suspend fun deleteCartProducts(cartDocId: String, selectedListDocId : MutableList<String>):Boolean{
            Log.d("test100","CartProductService ->deleteCartProducts()")

            val result = CartProductRepository.deleteCartProducts(cartDocId, selectedListDocId)
            return result
        }

    }
}