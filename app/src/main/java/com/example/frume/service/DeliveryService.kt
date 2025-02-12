package com.example.frume.service

import android.util.Log
import com.example.frume.model.DeliveryModel
import com.example.frume.repository.DeliveryRepository
import com.example.frume.vo.DeliveryVO
import com.google.firebase.firestore.FirebaseFirestore

class DeliveryService {
    companion object{
        // 배송 추가 메서드
        fun addUserDelivery(deliveryModel: DeliveryModel): String {
         return DeliveryRepository.addUserDelivery(deliveryModel.toDeliverVO())
        }

        // 해당 배송 가져오기
        suspend fun gettingDeliveryByDocId(deliveryDocId : String) : DeliveryModel{
            return DeliveryRepository.gettingDeliveryByDocId(deliveryDocId).toDeliverModel()
        }
    }
}