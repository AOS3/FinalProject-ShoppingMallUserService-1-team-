package com.example.frume.service

import com.example.frume.model.OrderProductModel
import com.example.frume.repository.OrderProductRepository
import com.example.frume.util.OrderSearchPeriod
import com.example.frume.util.OrderState

class OrderProductService {
    companion object{
        // 주문상품 추가 메서드
        suspend fun addOrderProduct(orderDocId: String, orderProductModelList: MutableList<OrderProductModel>): Boolean {
            // 주문 결과를 리턴
            return OrderProductRepository.addOrderProduct(orderDocId,orderProductModelList)
        }

        // 해당 주문에 해당하는 목록들 가져오는 메서드
        suspend fun gettingMyOrderProductItems(ordersDocId : List<String>, orderSearchPeriod: OrderSearchPeriod):MutableList<OrderProductModel> {
            val orderProductModelList = mutableListOf<OrderProductModel>()



            return orderProductModelList
        }

        // 내 주문에서 상품 목록 1개 가져오기 hj
        suspend fun gettingMyOrderProductItem(orderDocId: String, orderProductDocId: String): OrderProductModel {
            val vo = OrderProductRepository.gettingMyOrderProductItem(orderDocId,orderProductDocId)

            return vo.toOrderProductModel()
        }
    }
}