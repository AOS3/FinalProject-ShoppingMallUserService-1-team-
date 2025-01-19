package com.example.frume.data_hj

import com.example.frume.R

class DummyReviewClass(var ratingPoint :Float = 0.0f, var data : String, var title : String, var content : String, var imgPath: Int){

}

class DummyOrderItemClass(var deliverState : String, var productName : String, var imgPath: Int){

}

class DummyData {
    companion object {
        val dummyImages = listOf(
            R.drawable.img_straw1_png,
            R.drawable.img_straw2_png,
            R.drawable.img_straw3_png,
        )

        val dummyList: MutableList<DummyReviewClass> = MutableList(3) { index ->
            DummyReviewClass(
                ratingPoint = 4.0f, // 1부터 5까지의 랜덤 점수
                data = "2025-01-${index + 1}", // 날짜 생성
                title = "제목@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ${index + 1}", // 제목 생성
                content = "내용@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ${index + 1}.", // 내용 생성
                imgPath = dummyImages[index ] // 이미지를 순환하여 할당
            )
        }

        val dummyShippingItems : MutableList<DummyOrderItemClass> = MutableList(3){index ->
            DummyOrderItemClass("배송상태 ${index}","상품 이름 : ${index}", dummyImages[index]

            )
        }

    }
}