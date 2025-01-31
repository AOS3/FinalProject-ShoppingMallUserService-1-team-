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
    }
}