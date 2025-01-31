package com.example.frume.service


import android.content.Context
import androidx.core.content.edit
import com.example.frume.model.ReviewModel
import com.example.frume.model.UserModel
import com.example.frume.repository.UserRepository
import com.example.frume.util.CustomerUserState
import com.example.frume.util.LoginResult


class UserService {
    companion object {
        // 사용자 정보를 추가하는 메서드
        fun addCustomerUserData(userModel: UserModel): String {
            // 데이터를 VO에 담아준다.
            val userVO = userModel.toUserVO()
            // 저장하는 메서드를 호출한다.
            val userDocId = UserRepository.addCustomerUserData(userVO)
            return userDocId
        }

        // 가입하려는 아이디가 존재하는지 확인하는 메서드
        suspend fun checkJoinCustomerUserId(customerUserId: String): Boolean {
            // 아이디를 통해 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByCustomerUserId(customerUserId)
            // 가져온 데이터가 있다면
            return userVoList.isEmpty()
        }

        // 로그인 처리 메서드
        suspend fun checkLogin(loginUserId: String, loginUserPw: String): LoginResult {
            // 로그인 결과
            var result = LoginResult.LOGIN_RESULT_SUCCESS

            // 입력한 아이디로 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(loginUserId)

            // 가져온 사용자 정보가 없다면
            if (userVoList.isEmpty()) {
                result = LoginResult.LOGIN_RESULT_ID_NOT_EXIST
            } else {
                if (loginUserPw != userVoList[0].customerUserPw) {
                    // 비밀번호가 다르다면
                    result = LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT
                }
                // 탈퇴한 회원이라면
                if (userVoList[0].customerUserState == CustomerUserState.CUSTOMER_USER_STATE_WITHDRAWN.num) {
                    result = LoginResult.LOGIN_RESULT_SIGN_OUT_MEMBER
                }

            }
            return result
        }

        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId: String): UserModel {
            val tempVO = UserRepository.selectUserDataByUserIdOne(userId)[0]
            val loginUserModel = tempVO.toUserModel()
            return loginUserModel
        }


        // sehoon productDocId로 제품의 정보를 가져온다
        suspend fun getUserInfo(userDocId: String): MutableList<UserModel> {
            val userModelList = mutableListOf<UserModel>()
            val userVoList = UserRepository.getUserInfo(userDocId)
            userVoList.forEach {
                userModelList.add(it.toUserModel())
            }
            return userModelList
        }

        // hyeonseo 0123
        // 자동로그인 토큰값을 갱신하는 메서드
        suspend fun updateUserAutoLoginToken(context: Context, customerUserDocId: String) {
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
        suspend fun selectUserDataByLoginToken(loginToken: String): UserModel? {
            val loginUserVO = UserRepository.selectUserDataByLoginToken(loginToken)
            if (loginUserVO == null) {
                return null
            } else {
                val userModel = loginUserVO.toUserModel()
                return userModel
            }
        }


        // 사용자의 상태를 변경하는 메서드
        suspend fun updateUserState(customerUserDocId:String, newState:CustomerUserState){
            UserRepository.updateUserState(customerUserDocId, newState)
        }

        // 사용자의 데이터를 수정하는 메서드 : 내정보 -> 내정보 관리 -> 수정
        suspend fun updateUserData(userModel: UserModel){
            val userVO = userModel.toUserVO()
            // repository 연결
            UserRepository.updateUserData(userVO, userModel.customerUserDocId)
        }

        // 사용자의 비밀번호를 수정하는 메서드 :  내정보 -> 내정보 관리 -> 비밀번호 수정
        suspend fun updateUserPassword(customerUserDocId:String, newPassword : String){
            UserRepository.updateUserPassword(customerUserDocId,newPassword)
        }

        // 사용자의 현재 비밀번호만 가져오는 메서드  :  내정보 -> 내정보 관리 -> 비밀번호 수정
        suspend fun selectUserPasswordByUserDocId(customerUserDocId:String): String? {
            return UserRepository.selectUserPasswordByUserDocId(customerUserDocId)
        }

        // customerUserDocId로 사용자에 대한 정보를 불러오는 메서드 :  내정보 -> 내정보 관리
        suspend fun selectUserDataByuserDocumentId(customerUserDocId:String) : UserModel{
            val userVO = UserRepository.selectUserDataByuserDocumentId(customerUserDocId)
            val userModel = userVO.toUserModel()
            return userModel
        }
    }

}

