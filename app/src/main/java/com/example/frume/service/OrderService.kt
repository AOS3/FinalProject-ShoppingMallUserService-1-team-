package com.example.frume.service

import com.example.frume.model.OrderModel
import com.example.frume.repository.OrderRepository
import com.example.frume.vo.OrderVO
import com.google.firebase.firestore.FirebaseFirestore

class OrderService {
    companion object {
        // 주문 추가 메서드
        suspend fun addMyOrder(orderModel: OrderModel) :String{
            // 주문 DocId를 리턴
            val result = OrderRepository.addMyOrder(orderModel.toOrderVO())
            return result
        }
    }
}