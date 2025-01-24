package com.example.frume.data_ye

import com.example.frume.R

// 더미 데이터 목록
object DummyData {

    val CartItemList: List<TempCartProduct> = getCartItems()
    var AddressList: List<TempAddress> = getAddresses()

    // 더미 데이터를 위한 클래스
    private fun getCartItems(): List<TempCartProduct> {
        return listOf(
            TempCartProduct(R.drawable.img_fruit, false,"맛있는 딸기 1kg", 16900, 3),
            TempCartProduct(R.drawable.img_fruit, false,"신선한 바나나 1kg", 8500, 5),
            TempCartProduct(R.drawable.img_fruit, false,"고급 사과 1kg", 12300, 2)

        )
    }

    private fun getAddresses(): List<TempAddress> {
        return listOf(
            TempAddress("우리집", "00시00로 00번길 ㅇㅇ빌라 나동 402호", "123456", R.drawable.ic_home_24px),
            TempAddress("회사", "서울시 강남구 역삼동 123-45", "54321", R.drawable.ic_home_24px)
        )
    }
}

