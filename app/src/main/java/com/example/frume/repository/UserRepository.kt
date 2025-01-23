package com.example.frume.repository

import android.util.Log
import com.example.frume.vo.ProductVO
import com.example.frume.vo.UserVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object{

        // 사용자 아이디를 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserId(customerUserId:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val result = collectionReference.whereEqualTo("customerUserId", customerUserId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }

        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("userData")
            val result = collectionReference.whereEqualTo("customerUserId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)

            return userVoList
        }



        // 자동로그인 토큰값을 갱신하는 메서드
        suspend fun updateUserAutoLoginToken(customerUserDocId:String, customerUserLocalToken:String){
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

            if(userVOList.isEmpty()){
                return null
            } else {

                val returnUserVO = userVOList[0]
                // 이거 잘 확인되면 return userVOList으로 바로 가도될듯..

                Log.d("test100", "UserRepository.selectUserDataByLoginToken() -> userVOList ${userVOList}")

                return returnUserVO
            }
        }


    }
}