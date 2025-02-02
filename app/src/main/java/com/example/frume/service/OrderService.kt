package com.example.frume.service

import android.util.Log
import com.example.frume.model.OrderModel
import com.example.frume.repository.OrderRepository

class OrderService {
    companion object {
        // 주문 추가 메서드
        suspend fun addMyOrder(orderModel: OrderModel) :String{
            // 주문 DocId를 리턴
            val result = OrderRepository.addMyOrder(orderModel.toOrderVO())
            return result
        }

        // 내 주문 목록 가져오는 메서드
        suspend fun gettingMyOrder(customerUserId: String): MutableList<OrderModel> {
            Log.d("test100", "OrderService->gettingMyOrder() 호출됨")
            Log.d("test100", "customerUserId: $customerUserId")

            // 🔹 OrderRepository에서 해당 사용자의 주문 목록을 가져옴
            val orderVoList = OrderRepository.gettingMyOrder(customerUserId)
            Log.d("test100", "orderVoList 크기: ${orderVoList.size}")

            // 🔹 변환된 주문 목록을 저장할 리스트 생성
            val list = mutableListOf<OrderModel>()

            // 🔹 가져온 주문 데이터를 OrderModel 형식으로 변환하여 리스트에 추가
            orderVoList.forEach {
                val orderModel = it.toOrderModel()
                list.add(orderModel)
                Log.d("test100", "변환된 OrderModel: $orderModel")
            }

            // 🔹 변환된 주문 목록 반환
            Log.d("test100", "최종 반환할 list 크기: ${list.size}")
            return list
        }

        // 해당 주문 가져오기
        suspend fun gettingOrderByOrderDocId(orderDocId: String):OrderModel {
            Log.d("test100", "OrderService->gettingOrderByOrderDocId() 호출됨")

            val orderModel = OrderRepository.gettingOrderByOrderDocId(orderDocId).toOrderModel()
            return orderModel
        }

    }
}