package com.example.frume.service

import android.util.Log
import com.example.frume.model.OrderProductModel
import com.example.frume.repository.OrderProductRepository
import com.example.frume.repository.OrderRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderProductService {
    companion object{
        // 주문상품 추가 메서드
        suspend fun addOrderProduct(orderDocId: String, orderProductModelList: MutableList<OrderProductModel>): Boolean {
            // 주문 결과를 리턴
            return OrderProductRepository.addOrderProduct(orderDocId,orderProductModelList)

        }
    }
}