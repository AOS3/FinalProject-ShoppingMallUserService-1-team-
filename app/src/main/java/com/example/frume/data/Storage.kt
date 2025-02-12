package com.example.frume.data

import android.os.Parcelable
import com.example.frume.R
import kotlinx.parcelize.Parcelize

object Storage {
    val categoryList: List<String> = getCategoryData()
    val bannerList: List<TempBanner> = getBannerData()
    val detailList: List<String> = getDetailData()
    val inquiryList: List<Inquiry> = getInquiryData()


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

    private fun getInquiryData(): List<Inquiry> {
        return listOf(
            Inquiry("상품상세문의", "상품 관련 문의 입니다.", "답변예정", "seh***", "25.02.03", true),
            Inquiry("배송", "배송 관련 문의 입니다.", "답변예정", "hoj***", "25.02.03", true),
            Inquiry("상품상세문의", "상품 관련 문의 입니다.", "답변예정", "hye***", "25.02.04", false),
            Inquiry("상품상세문의", "상품 관련 문의 입니다.", "답변예정", "you***", "25.02.04", false),
            Inquiry("배송", "배송 관련 문의 입니다.", "답변예정", "min***", "25.02.05", true),
            Inquiry("상품상세문의", "상품 관련 문의 입니다.", "답변완료", "eun***", "25.02.05", false),
            Inquiry("상품상세문의", "상품 관련 문의 입니다.", "답변완료", "na****", "25.02.06", false),
            Inquiry("상품상세문의", "상품 관련 문의 입니다.", "답변완료", "su****", "25.02.06", true),
        )

    }

    private fun getBannerData(): List<TempBanner> {
        return listOf(
            TempBanner(R.drawable.img_banner1),
            TempBanner(R.drawable.img_banner2),
            TempBanner(R.drawable.img_banner3),
            TempBanner(R.drawable.img_banner4),
            TempBanner(R.drawable.banner),
            TempBanner(R.drawable.img_banner5)
        )
    }

    @Parcelize
    data class Test(
        val name: String,
        val age: Int,
    ) : Parcelable

}
