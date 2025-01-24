package com.example.frume.repository

import com.example.frume.vo.UserVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.example.frume.vo.UserVO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object{
        // 사용자 정보를 추가하는 메서드, userDocID를 리턴해야함 장바구니 생성을 위해
        fun addCustomerUserData(userVO: UserVO):String{

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



        // 자동로그인 토큰값을 갱신하는 메서드
        suspend fun updateUserAutoLoginToken(customerUserDocId: String, customerUserLocalToken: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val documentReference = collectionReference.document(customerUserDocId)
            val tokenMap = mapOf(
                "userAutoLoginToken" to customerUserLocalToken
            )
            documentReference.update(tokenMap).await()
        }

        
        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(customerUserLocalToken:String) : UserVO?{

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
        suspend fun selectUserDataByUserIdOne(userId: String): MutableList<UserVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val result = collectionReference.whereEqualTo("customerUserId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)

            return userVoList
        }


        // 토큰 관련
        // 자동로그인 토큰값을 갱신하는 메서드
        suspend fun updateUserAutoLoginToken(customerUserDocId:String, customerUserLocalToken:String){
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
    }
}