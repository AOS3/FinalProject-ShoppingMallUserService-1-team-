package com.example.frume.repository

import android.util.Log
import com.example.frume.vo.DeliveryAddressVO
import com.example.frume.vo.UserVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDeliveryAddressRepository {
    companion object {
        // 기본 배송지 가져오기 hj
        suspend fun gettingDefaultDeliveryAddress(customerUserDocId: String): DeliveryAddressVO {

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryAddressData")
            val result = DeliveryAddressVO()

            try {
                val defaultDeliveryAddress = collectionReference
                    .whereEqualTo("deliveryAddressUserDocId", customerUserDocId)
                    .whereEqualTo("deliveryAddressIsDefaultAddress", true)
                    .get()
                    .await()

                    val defaultDeliveryAddressVO = defaultDeliveryAddress.toObjects(DeliveryAddressVO::class.java)

                    return defaultDeliveryAddressVO[0]

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("test100","UserDeliveryAddressRepository -> gettingDefaultDeliveryAddress() catch($e)")
            }
            return result
        }


        // 선택된 배송지 가져오기 hj
        suspend fun gettingSelectedDeliveryAddress(deliveryAddressDocId: String): DeliveryAddressVO {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryAddressData")
            val result = DeliveryAddressVO()

            try {
                val defaultDeliveryAddress = collectionReference
                    .whereEqualTo("deliveryAddressDocId", deliveryAddressDocId)
                    .get()
                    .await()

                val defaultDeliveryAddressVO = defaultDeliveryAddress.toObjects(DeliveryAddressVO::class.java)

                return defaultDeliveryAddressVO[0]

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("test500","UserDeliveryAddressRepository -> gettingDefaultDeliveryAddress() catch($e)")
            }
            return result
        }


        // 배송지 목록 가져오기 hj
        /*suspend fun gettingDeliveryAddressList(customerUserDocId: String): MutableList<DeliveryAddressVO> {
            Log.d("test100","UserDeliveryAddressRepository -> gettingDeliveryAddressList()")

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryAddressData")
            Log.d("test100","UserDeliveryAddressRepository -> deliveryAddressData ${collectionReference.id}")

            val result = mutableListOf<DeliveryAddressVO>()

            try {
                // 배송지 목록 snapshot
                val defaultDeliveryAddress = collectionReference
                    .whereEqualTo("deliveryAddressUserDocId", customerUserDocId)
                    .get()
                    .await()

                val deliveryAddressVoList = defaultDeliveryAddress.toObjects(DeliveryAddressVO::class.java)
                Log.d("test100","UserDeliveryAddressRepository -> deliveryAddressVoList ${deliveryAddressVoList[0].deliveryAddressDocId}")

                return deliveryAddressVoList

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("test100","UserDeliveryAddressRepository -> gettingDeliveryAddressList() catch(e)")
            }
            return result
        }*/

        suspend fun gettingDeliveryAddressList(customerUserDocId: String): MutableList<DeliveryAddressVO> {
            Log.d("test100","UserDeliveryAddressRepository -> gettingDeliveryAddressList()")

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryAddressData")
            Log.d("test100","UserDeliveryAddressRepository -> deliveryAddressData ${collectionReference.id}")

            val result = mutableListOf<DeliveryAddressVO>()

            try {
                // 배송지 목록 snapshot
                val defaultDeliveryAddress = collectionReference
                    .whereEqualTo("deliveryAddressUserDocId", customerUserDocId)
                    .whereEqualTo("deliveryAddressState", 1)  // 상태가 1인 배송지만 가져옴
                    .get()
                    .await()

                val deliveryAddressVoList = defaultDeliveryAddress.toObjects(DeliveryAddressVO::class.java)
                Log.d("test100","UserDeliveryAddressRepository -> deliveryAddressVoList ${deliveryAddressVoList[0].deliveryAddressDocId}")

                return deliveryAddressVoList

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("test100","UserDeliveryAddressRepository -> gettingDeliveryAddressList() catch(e)")
            }
            return result
        }

        // 배송지 목록에 추가하기 hj
        fun addDeliveryAddress(customerUserDocId: String, deliveryAddressVO: DeliveryAddressVO): String {
            Log.d("test100","UserDeliveryAddressRepository -> addDeliveryAddress()")

            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryAddressData")
            val documentReference = collectionReference.document()

            val addDeliveryAddressVo = deliveryAddressVO
            addDeliveryAddressVo.deliveryAddressDocId = documentReference.id
            addDeliveryAddressVo.deliveryAddressUserDocId = customerUserDocId
            documentReference.set(addDeliveryAddressVo)

            return documentReference.id
        }

        // 기본배송지 추가할때 이미 등록된 기본 배송지 상태 변경하기 hj
        suspend fun changeDefaultStateToFalse(customerUserDocId: String, newDeliveryDocId: String?) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryAddressData")

            try {
                // 기본 배송지 찾기
                val defaultDeliveryAddress = collectionReference
                    .whereEqualTo("deliveryAddressUserDocId", customerUserDocId)
                    .whereEqualTo("deliveryAddressIsDefaultAddress", true)
                    .get()
                    .await()

                // 기본 배송지 상태를 false로 업데이트
                defaultDeliveryAddress.documents.forEach { document ->
                    if (document.id != newDeliveryDocId) { // 새 배송지 제외
                        collectionReference.document(document.id)
                            .update("deliveryAddressIsDefaultAddress", false)
                            .await()

                        Log.d("test100", "기본 배송지 상태를 false로 변경했습니다.")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("test100", "setDefaultStateToFalse() -> 오류 발생")
            }
        }


        // 배송지를 기본 배송지로 변경하기
        suspend fun changeDefaultStateToTrue(customerUserDocId: String, newDocId : String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryAddressData")

            try {
                // 새로운 기본 배송지 설정
                val newDefaultAddress = collectionReference
                    .whereEqualTo("deliveryAddressUserDocId", customerUserDocId)
                    .whereEqualTo("deliveryAddressDocId", newDocId)
                    .get()
                    .await()

                if (newDefaultAddress.documents.isNotEmpty()) {
                    newDefaultAddress.documents[0].reference.update("deliveryAddressIsDefaultAddress", true).await()
                    Log.d("test100", "기본 배송지 상태를 true로 변경했습니다.")
                } else {
                    Log.d("test100", "해당하는 배송지를 찾을 수 없습니다.")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("test100", "changeDefaultStateToTrue() -> 오류 발생")
            }
        }

        // 배송지 상태를 비정상으로 업데이트하기
        suspend fun markDeliveryAddressAsInvalid(deliveryAddressDocId: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryAddressData")

            try {
                // 배송지 문서 업데이트 (비정상 상태로 변경)
                val documentReference = collectionReference.document(deliveryAddressDocId)
                documentReference.update("deliveryAddressState", 2).await()
                Log.d("test100", "배송지 상태를 비정상(2)으로 변경했습니다.")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("test100", "markDeliveryAddressAsInvalid() -> 오류 발생")
            }
        }


        // 배송지 데이터를 수정하는 메서드
        suspend fun updateUserDeliveryAddress(deliveryAddressVO: DeliveryAddressVO, deliveryAddressDocId: String){

            // 수정할 데이터를 담을 맵
            val userDeliveryAddressMap = mapOf(
                // 배송지 이름
                "deliveryAddressName" to deliveryAddressVO.deliveryAddressName,
                // 이름
                "deliveryAddressReceiverName" to deliveryAddressVO.deliveryAddressReceiverName,
                // 휴대폰 번호
                "deliveryAddressPhoneNumber" to deliveryAddressVO.deliveryAddressPhoneNumber,
                // 사용자 주소
                "deliveryAddressBasicAddress" to deliveryAddressVO.deliveryAddressBasicAddress,
                // 우편 번호
                // "deliveryAddressPostNumber" to deliveryAddressVO.deliveryAddressPostNumber,
                // 상세 주소
                "deliveryAddressDetailAddress" to deliveryAddressVO.deliveryAddressDetailAddress,
            )

            // 수정할 문서에 접근할 수 있는 객체를 가져온다.
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("deliveryAddressData")
            val documentReference = collectionReference.document(deliveryAddressDocId)
            documentReference.update(userDeliveryAddressMap).await()
        }

        /*suspend fun selectUserAddressByuserDocumentId(deliveryAddressDocId:String) : DeliveryAddressVO{
           val firestore = FirebaseFirestore.getInstance()
           val collectionReference = firestore.collection("deliveryAddressData")
           val result = collectionReference.document(deliveryAddressDocId).get().await()
           val deliveryAddressVO = result.toObject(DeliveryAddressVO::class.java)
           return deliveryAddressVO
       }*/
    }
}