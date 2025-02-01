package com.example.frume.service

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
        suspend fun gettingMyOrder(customerUserId: String) :MutableList<OrderModel>{
            val orderVoList = OrderRepository.gettingMyOrder(customerUserId)

            val list = mutableListOf<OrderModel>()

            orderVoList.forEach {
                list.add(it.toOrderModel())
            }
            return list
        }
    }
}