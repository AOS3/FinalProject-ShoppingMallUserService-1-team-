package com.example.frume.service


import android.content.Context
import androidx.core.content.edit
import com.example.frume.model.UserModel
import com.example.frume.repository.UserRepository
import com.example.frume.util.CustomerUserState
import com.example.frume.util.LoginResult


class UserService {
    companion object{
        // 사용자 정보를 추가하는 메서드
        fun addCustomerUserData(userModel: UserModel):String{
            // 데이터를 VO에 담아준다.
            val userVO = userModel.toUserVO()
            // 저장하는 메서드를 호출한다.
            val userDocId = UserRepository.addCustomerUserData(userVO)
            return userDocId
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


        // hyeonseo 0123
        // 자동로그인 토큰값을 갱신하는 메서드
        suspend fun updateUserAutoLoginToken(context: Context, customerUserDocId:String){
            // 새로운 토큰값을 발행한다.
            val newToken = "${customerUserDocId}${System.nanoTime()}"
            // SharedPreference에 저장한다.
            val pref = context.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
            pref.edit {
                putString("token", newToken)
            }
            // 서버에 저장한다.
            UserRepository.updateUserAutoLoginToken(customerUserDocId, newToken)
        }

        // hyeonseo 0123
        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken:String) : UserModel?{
            val loginUserVO = UserRepository.selectUserDataByLoginToken(loginToken)
            if(loginUserVO == null){
                return null
            } else {

                // 2차 주석 처리 -> VO에 문서ID 이미 있어서 필요 없을듯.
                //val customerUserDocId = loginUserVO.customerUserDocId

                val userModel = loginUserVO.toUserModel()
                return userModel
            }
        }


    }
}

