package com.example.frume.service

import android.util.Log
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
        suspend fun gettingMyOrderProductItems(ordersDocIdList : List<String>, orderSearchPeriod: OrderSearchPeriod):MutableList<OrderProductModel> {
            Log.d("test100","OrderProductService->gettingMyOrderProductItems()")
            val orderProductVoList = OrderProductRepository.gettingMyOrderProductItems(ordersDocIdList,orderSearchPeriod)
            // map 리턴 타입 -> List
            val orderProductModelList = orderProductVoList.map { it.toOrderProductModel() }
            Log.d("test100","orderProductModelList : ${orderProductModelList}")

            return orderProductModelList as MutableList<OrderProductModel>
        }

        // 내 주문에서 상품 목록 1개 가져오기 hj
        suspend fun gettingMyOrderProductItem(orderDocId: String, orderProductDocId: String): OrderProductModel {
            val vo = OrderProductRepository.gettingMyOrderProductItem(orderDocId,orderProductDocId)

            return vo.toOrderProductModel()
        }
    }
}