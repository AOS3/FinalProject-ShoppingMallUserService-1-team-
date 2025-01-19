package com.example.frume.data

import android.os.Parcelable
import com.example.frume.R
import com.example.frume.util.CategoryType
import kotlinx.parcelize.Parcelize

object Storage {
    val categoryList: List<String> = getCategoryData()
    val productList: List<TempProduct> = getProductData()
    val bannerList: List<TempBanner> = getBannerData()
    val detailList: List<String> = getDetailData()

    private fun getCategoryData(): List<String> {
        return listOf(
            "홈", "신제품", "특가", "베스트", "1인", "패키지"
        )
    }

    private fun getDetailData(): List<String> {
        return listOf(
            "상품설명", "상세정보", "후기", "문의"
        )
    }

    private fun getBannerData(): List<TempBanner> {
        return listOf(
            TempBanner(R.drawable.img_banner1),
            TempBanner(R.drawable.img_banner2),
            TempBanner(R.drawable.img_banner3),
            TempBanner(R.drawable.img_banner4),
            TempBanner(R.drawable.img_banner5)
        )
    }

    private fun getProductData(): List<TempProduct> {
        return listOf(
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "어쩌구 저쩌구 어쩌구 저쩌구 어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_MAIN),
            TempProduct(R.drawable.img_fruit, "사과", 12000, "어쩌구 저쩌구 사과", CategoryType.CATEGORY_HOME_MAIN),
            TempProduct(R.drawable.img_fruit, "포도", 13000, "어쩌구 저쩌구 포도", CategoryType.CATEGORY_HOME_MAIN),
            TempProduct(R.drawable.img_fruit, "망고", 14000, "(베스트)어쩌구 저쩌구 망고", CategoryType.CATEGORY_HOME_BEST),
            TempProduct(R.drawable.img_fruit, "오렌지", 15000, "(베스트)어쩌구 저쩌구 오렌지", CategoryType.CATEGORY_HOME_BEST),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(베스트)어쩌구 저쩌구 어쩌구 저쩌구 어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_BEST),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(베스트)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_BEST),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(베스트)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_BEST),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(베스트)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_BEST),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(신상품)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_NEW),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(신상품)어쩌구 저쩌구 어쩌구 저쩌구 어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_NEW),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(신상품)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_NEW),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(특가)어쩌구 저쩌구 어쩌구 저쩌구 어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_SALE),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(특가)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_SALE),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(특가)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_SALE),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(패키지)어쩌구 저쩌구 어쩌구 저쩌구 어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_PACKAGE),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(패키지)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_PACKAGE),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(패키지)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_PACKAGE),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(패키지)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_PACKAGE),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(1인가구)어쩌구 저쩌구 어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_SINGLE),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(1인가구)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_SINGLE),
            TempProduct(R.drawable.img_fruit, "딸기", 10000, "(1인가구)어쩌구 저쩌구 딸기", CategoryType.CATEGORY_HOME_SINGLE),
        )
    }

    @Parcelize
    data class Test(
        val name:String,
        val age:Int,
    ):Parcelable

}
