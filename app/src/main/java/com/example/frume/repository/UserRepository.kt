package com.example.frume.repository

import android.util.Log
import com.example.frume.model.UserModel
import com.example.frume.util.CustomerUserState
import com.example.frume.vo.UserVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object {
        // 사용자 정보를 추가하는 메서드, userDocID를 리턴해야함 장바구니 생성을 위해
        fun addCustomerUserData(userVO: UserVO): String {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val documentReference = collectionReference.document()
            val addUserVO = userVO
            addUserVO.customerUserDocId = documentReference.id
            documentReference.set(addUserVO)
            return addUserVO.customerUserDocId
        }

        // 사용자 아이디를 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByCustomerUserId(customerUserId: String): MutableList<UserVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val result = collectionReference.whereEqualTo("customerUserId", customerUserId).get().await()

            // Log.d("test100", "${result.documents}")
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }

        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(customerUserLocalToken: String): UserVO? {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val resultList = collectionReference.whereEqualTo("customerUserLocalToken", customerUserLocalToken).get().await()
            val userVOList = resultList.toObjects(UserVO::class.java)

            return if (userVOList.isNotEmpty()) userVOList[0] else null
        }


        // 사용자 아이디를 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserId(customerUserId: String): MutableList<UserVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val result = collectionReference.whereEqualTo("customerUserId", customerUserId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }

        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(customerUserId: String): MutableList<UserVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val result = collectionReference.whereEqualTo("customerUserId", customerUserId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)

            return userVoList
        }


        // 토큰 관련
        // 자동로그인 토큰값을 갱신하는 메서드
        suspend fun updateUserAutoLoginToken(customerUserDocId: String, customerUserLocalToken: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val documentReference = collectionReference.document(customerUserDocId)
            val tokenMap = mapOf(
                "customerUserLocalToken" to customerUserLocalToken
            )
            documentReference.update(tokenMap).await()
        }


        // productDocId로 상품 정보 가져오기
        suspend fun getUserInfo(userDocId: String): MutableList<UserVO> {
            val firestore = FirebaseFirestore.getInstance()

            val collectionReference = firestore.collection("userData")
            val userResult = mutableListOf<UserVO>()

            try {
                val product = collectionReference.whereEqualTo("customerUserDocId", userDocId).get().await()

                for (document in product) {
                    val userVO = document.toObject(UserVO::class.java)
                    Log.d("test100", "ProductRepository -> productVO: $userVO")

                    userResult.add(userVO)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return userResult
        }



        // 사용자의 상태를 변경하는 메서드 : 탈퇴 처리
        suspend fun updateUserState(customerUserDocId:String, newState: CustomerUserState){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val documentReference = collectionReference.document(customerUserDocId)
            val updateMap = mapOf(
                "customerUserState" to newState.num
            )
            documentReference.update(updateMap).await()
        }

        // 사용자의 데이터를 수정하는 메서드 : 내정보 -> 내정보 관리 -> 수정
        suspend fun updateUserData(userVO: UserVO, customerUserDocId:String){

            // 수정할 데이터를 담을 맵
            val userMap = mapOf(

                // 이름
                "customerUserName" to userVO.customerUserName,
                // 휴대폰 번호
                "customerUserPhoneNumber" to userVO.customerUserPhoneNumber,
                // 사용자 주소 (주소 웹뷰 연결 및 저장) + 상세 주소
                "customerUserAddress" to userVO.customerUserAddress,
                // 사용자 적립금
                "customerUserReward" to userVO.customerUserReward
            )

            // 수정할 문서에 접근할 수 있는 객체를 가져온다.
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val documentReference = collectionReference.document(customerUserDocId)
            documentReference.update(userMap).await()
        }

        // 사용자의 비밀번호를 수정하는 메서드 :  내정보 -> 내정보 관리 -> 비밀번호 수정
        suspend fun updateUserPassword(customerUserDocId:String, newPassword : String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val documentReference = collectionReference.document(customerUserDocId)

            val updateMap = mapOf(
                "customerUserPw" to newPassword // 비밀번호 업데이트
            )
            documentReference.update(updateMap).await()
        }


        // 현재 비밀번호 가져오기
        suspend fun selectUserPasswordByUserDocId(customerUserDocId:String): String? {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")

            // 특정 사용자 데이터 가져오기
            val result = collectionReference
                .whereEqualTo("customerUserDocId", customerUserDocId)
                .get()
                .await()

            // 결과에서 비밀번호 필드만 추출
            if (result.documents.isNotEmpty()) {
                val document = result.documents[0] // 첫 번째 문서 가져오기 (단일 사용자라 가정)
                return document.getString("customerUserPw") // Firestore에서 "password" 필드 추출
            }

            // 사용자가 없을 경우 null 반환
            return null
        }

        suspend fun selectUserDataByuserDocumentId(customerUserDocId:String) : UserVO{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val result = collectionReference.document(customerUserDocId).get().await()
            val userVO = result.toObject(UserVO::class.java)!!
            return userVO
        }
    }





}