package com.example.frume.data

import android.os.Parcelable
import com.example.frume.R
import com.example.frume.util.CategoryType
import kotlinx.parcelize.Parcelize
import java.util.Date

object Storage {
    val categoryList: List<String> = getCategoryData()
    val bannerList: List<TempBanner> = getBannerData()
    val detailList: List<String> = getDetailData()
    val imgList: List<TempImg> = getImgData()

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

    private fun getImgData(): List<TempImg> {
        return listOf(
            TempImg(R.drawable.img_straw1_png),
            TempImg(R.drawable.img_straw2_png),
            TempImg(R.drawable.img_straw3_png),
        )
    }

    @Parcelize
    data class Test(
        val name: String,
        val age: Int,
    ) : Parcelable

}
