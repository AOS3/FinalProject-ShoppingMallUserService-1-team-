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
        
            // 로그인 처리 메서드
        suspend fun checkLogin(loginUserId:String, loginUserPw:String) : LoginResult {
            // 로그인 결과
            var result = LoginResult.LOGIN_RESULT_SUCCESS

            // 입력한 아이디로 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(loginUserId)

            // 가져온 사용자 정보가 없다면
            if(userVoList.isEmpty()){
                result = LoginResult.LOGIN_RESULT_ID_NOT_EXIST
            } else {
                if(loginUserPw != userVoList[0].customerUserPw){
                    // 비밀번호가 다르다면
                    result = LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT
                }
                // 탈퇴한 회원이라면
                if(userVoList[0].customerUserState == CustomerUserState.CUSTOMER_USER_STATE_WITHDRAWN.num){
                    result = LoginResult.LOGIN_RESULT_SIGN_OUT_MEMBER
                }
            }
            return result
        }

        // 사용자 아이디를 통해 문서 id와 사용자 정보를 가져온다.
        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : UserModel {
            val tempVO = UserRepository.selectUserDataByUserIdOne(userId)[0]
            val loginUserModel = tempVO.toUserModel()
            return loginUserModel
        }

    }
}
