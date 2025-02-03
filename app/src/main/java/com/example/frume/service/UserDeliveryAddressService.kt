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
            Log.d("test500", "UserDeliveryAddressService -> gettingSelectedDeliveryAddress() - Start")

            try {
                // 로그 찍어서 deliveryAddressDocId 값 확인
                Log.d("test500", "UserDeliveryAddressService -> Getting delivery address for docId: $deliveryAddressDocId")

                // Repository에서 주소를 가져옴
                val deliverAddressVO =
                    UserDeliveryAddressRepository.gettingSelectedDeliveryAddress(deliveryAddressDocId)

                // 전달받은 VO 확인
                Log.d("test500", "UserDeliveryAddressService -> Retrieved DeliveryAddressVO: $deliverAddressVO")

                // VO를 Model로 변환
                val result = deliverAddressVO.toDeliverAddressModel()

                // 변환된 Model 값 확인
                Log.d("test500", "UserDeliveryAddressService -> Converted to DeliveryAddressModel: $result")

                return result
            } catch (e: Exception) {
                Log.e("test500", "UserDeliveryAddressService -> Error getting selected delivery address", e)
                throw e // 예외를 다시 던져서 호출자에게 알림
            }
        }


        // 배송지 목록 가져오기 hj
        /*suspend fun gettingDeliveryAddressList(customerUserDocId: String): MutableList<DeliveryAddressModel> {
            val result = mutableListOf<DeliveryAddressModel>()
            val deliverAddressVoList =
                UserDeliveryAddressRepository.gettingDeliveryAddressList(customerUserDocId)
            deliverAddressVoList.forEach {
                result.add(it.toDeliverAddressModel())
            }
            return result
        }*/

        suspend fun gettingDeliveryAddressList(customerUserDocId: String): MutableList<DeliveryAddressModel> {
            val result = mutableListOf<DeliveryAddressModel>()
            val deliverAddressVoList = UserDeliveryAddressRepository.gettingDeliveryAddressList(customerUserDocId)
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

        // 배송지 상태를 비정상으로 변경하는 메서드
        suspend fun markDeliveryAddressAsInvalid(deliveryAddressDocId: String) {
            UserDeliveryAddressRepository.markDeliveryAddressAsInvalid(deliveryAddressDocId)
        }

        // 배송지를 수정하는 메서드
        suspend fun updateUserDeliveryAddress(deliveryAddressModel: DeliveryAddressModel){
            val deliveryAddressVO = deliveryAddressModel.toDeliverAddressVO()
            // repository 연결
            UserDeliveryAddressRepository.updateUserDeliveryAddress(deliveryAddressVO, deliveryAddressModel.deliveryAddressDocId)
        }

        /*suspend fun selectUserAddressByuserDocumentId(deliveryAddressDocId:String) : DeliveryAddressModel{
            val deliveryAddressVO = UserDeliveryAddressRepository.selectUserAddressByuserDocumentId(deliveryAddressDocId)
            val deliveryAddressModel = deliveryAddressVO.toDeliverAddressModel()
            return deliveryAddressModel
        }*/
    }
}