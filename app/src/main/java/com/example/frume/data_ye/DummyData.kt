package com.example.frume.data_ye

import com.example.frume.R

// 더미 데이터 목록
object DummyData {

    // 더미 데이터를 위한 클래스
    data class CartItem(
        val imageResId: Int,
        val productName: String,
        val productPrice: Int,
        val quantity: Int
    )

    data class UserAddress(
        val addressName: String,   // 주소지 이름
        val addressDetail: String, // 주소지 상세
        val postalCode: String,    // 우편번호
        val addressIconResId: Int  // 아이콘 리소스 ID
    )

    // 상품 목록
    fun getDummyCartItems(): List<CartItem> {
        return listOf(
            CartItem(
                imageResId = R.drawable.img_fruit,
                productName = "맛있는 딸기 1kg",
                productPrice = 16900,
                quantity = 3
            ),
            CartItem(
                imageResId = R.drawable.img_fruit,
                productName = "신선한 바나나 1kg",
                productPrice = 8500,
                quantity = 5
            ),
            CartItem(
                imageResId = R.drawable.img_fruit,
                productName = "고급 사과 1kg",
                productPrice = 12300,
                quantity = 2
            )
        )
    }

    // 주소 목록
    fun getDummyUserAddresses(): List<UserAddress> {
        return listOf(
            UserAddress(
                addressName = "우리집",
                addressDetail = "00시00로 00번길 ㅇㅇ빌라 나동 402호",
                postalCode = "12456",
                addressIconResId = R.drawable.ic_home_24px
            ),
            UserAddress(
                addressName = "회사",
                addressDetail = "서울시 강남구 역삼동 123-45",
                postalCode = "54321",
                addressIconResId = R.drawable.ic_home_24px
            )
        )
    }
}

