package com.example.frume.repository

import android.util.Log
import com.example.frume.vo.ProductVO
import com.example.frume.vo.UserVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object{
        // 사용자 정보를 추가하는 메서드
        fun addCustomerUserData(userVO: UserVO){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val documentReference = collectionReference.document()
            val addUserVO = userVO
            addUserVO.customerUserDocId = documentReference.id
            documentReference.set(addUserVO)
        }

        // 사용자 아이디를 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByCustomerUserId(customerUserId:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val result = collectionReference.whereEqualTo("customerUserId", customerUserId).get().await()

            // Log.d("test100", "${result.documents}")
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }

        /*suspend fun getUserInfo(userID: String): MutableList<UserVO> {
            val firestore = FirebaseFirestore.getInstance()

            val collectionReference = firestore.collection("userData")
            val userResult = mutableListOf<UserVO>()

            try {
                val user = collectionReference.whereEqualTo("customerUserDocId", userID).get().await()

                for (document in user) {
                    val userVO = document.toObject(UserVO::class.java)
                    Log.d("test100", "UserRepository -> userVO: $userVO")

                    userResult.add(userVO)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return userResult
        }*/
    }
}