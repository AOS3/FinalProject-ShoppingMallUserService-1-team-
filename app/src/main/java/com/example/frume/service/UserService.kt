package com.example.frume.service

import android.util.Log
import com.example.frume.model.ProductModel
import com.example.frume.model.UserModel
import com.example.frume.repository.ProductRepository
import com.example.frume.repository.UserRepository

class UserService {
    companion object{
        // 사용자 정보를 추가하는 메서드
        fun addCustomerUserData(userModel: UserModel){
            // 데이터를 VO에 담아준다.
            val userVO = userModel.toUserVO()
            // 저장하는 메서드를 호출한다.
            UserRepository.addCustomerUserData(userVO)
        }

        // 가입하려는 아이디가 존재하는지 확인하는 메서드
        suspend fun checkJoinCustomerUserId(customerUserId:String) : Boolean{
            // 아이디를 통해 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByCustomerUserId(customerUserId)
            // 가져온 데이터가 있다면
            return userVoList.isEmpty()
        }

        /*suspend fun getUserInfo(userId: String): MutableList<UserModel> {
            val userModelList = mutableListOf<UserModel>()
            val userVOList = UserRepository.getUserInfo(userId)

            userVOList.forEach {
                userModelList.add(it.toUserModel())
            }
            return userModelList
        }*/
    }
}
