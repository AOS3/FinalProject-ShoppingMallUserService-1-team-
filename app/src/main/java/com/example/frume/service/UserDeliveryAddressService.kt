package com.example.frume.service

import android.util.Log
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.model.UserModel
import com.example.frume.repository.UserDeliveryAddressRepository
import com.example.frume.repository.UserRepository

class UserDeliveryAddressService {
    companion object {
        // 기본 배송지 가져오기 hj
        suspend fun gettingDefaultDeliveryAddress(customerUserDocId: String): DeliveryAddressModel {
            val deliverAddressVO =
                UserDeliveryAddressRepository.gettingDefaultDeliveryAddress(customerUserDocId)
            val result = deliverAddressVO.toDeliverAddressModel()
            return result
        }

        // 선택된 배송지 가져오기 hj
        suspend fun gettingSelectedDeliveryAddress(deliveryAddressDocId: String): DeliveryAddressModel {
            val deliverAddressVO =
                UserDeliveryAddressRepository.gettingSelectedDeliveryAddress(deliveryAddressDocId)
            val result = deliverAddressVO.toDeliverAddressModel()
            return result
        }

        // 배송지 목록 가져오기 hj
        suspend fun gettingDeliveryAddressList(customerUserDocId: String): MutableList<DeliveryAddressModel> {
            val result = mutableListOf<DeliveryAddressModel>()
            val deliverAddressVoList =
                UserDeliveryAddressRepository.gettingDeliveryAddressList(customerUserDocId)
            deliverAddressVoList.forEach {
                result.add(it.toDeliverAddressModel())
            }
            return result
        }

        // 배송지 목록에 추가하기 hj
        fun addDeliveryAddress(customerUserDocId: String, deliveryAddressModel: DeliveryAddressModel) : String {
            Log.d("test100","UserDeliveryAddressService -> addDeliveryAddress()")
            Log.d("test100","UserDeliveryAddressService -> deliveryAddressModel:${deliveryAddressModel}")

            // 데이터를 VO에 담아준다.
            val addDeliveryAddressVO = deliveryAddressModel.toDeliverAddressVO()
            // 저장하는 메서드를 호출한다.
            // DB에 문서를 추가하면서 문서 ID를 리턴받는다 fragment에서 추가할때 문서 아이디 사용할 곳이 있기 때문에 리턴받음 hj
            return UserDeliveryAddressRepository.addDeliveryAddress(customerUserDocId,addDeliveryAddressVO)
        }

        // 기본 배송지를 false로 변경하기
        suspend fun changeDefaultStateToFalse(customerUserDocId: String, newDocId : String) {
            UserDeliveryAddressRepository.changeDefaultStateToFalse(customerUserDocId, newDocId)
        }

        // 현재 배송지를 기본배송지로 변경하기
        suspend fun changeDefaultStateToTrue(customerUserDocId: String,newDocId : String) {
            UserDeliveryAddressRepository.changeDefaultStateToTrue(customerUserDocId, newDocId)
        }
    }
}