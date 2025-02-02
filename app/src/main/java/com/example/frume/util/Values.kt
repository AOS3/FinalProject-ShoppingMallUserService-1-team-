package com.example.frume.util

enum class UserInfoType(var number: Int, var str: String) {
    // 주문 내역 및 배송조회
    USER_ORDER_HISTORY_FRAGMENT(0, "주문 내역 및 배송조회"),

    // 주문 상세 내역
    USER_ORDER_DETAIL_FRAGMENT(1, "주문 상세 내역"),

    // 주문 반품 및 취소
    USER_CANCEL_AND_RETURN_FRAGMENT(2, "주문 반품 및 취소"),

    // 회원 정보 관리 및 탈퇴
    USER_INFO_MANAGE_FRAGMENT(3, "회원 정보 관리 및 탈퇴"),

    // 회원 정보 수정
    USER_INFO_MODIFY_FRAGMENT(4, "회원 정보 수정"),

    // 배송지 관리
    USER_ADDRESS_MANAGE_FRAGMENT(5, "배송지 관리")
}

// 홈 화면 탭 레이아웃 분류
enum class CategoryType(val number: Int, val category: String) {
    CATEGORY_HOME_MAIN(0, "홈"),
    CATEGORY_HOME_NEW(1, "신제품"),
    CATEGORY_HOME_SALE(2, "특가"),
    CATEGORY_HOME_BEST(3, "베스트"),
    CATEGORY_HOME_SINGLE(4, "1인 가구"),
    CATEGORY_HOME_PACKAGE(5, "패키지");

    companion object {
        fun fromNumber(number: Int): String? {
            return entries.find { it.number == number }?.category
        }
    }
}

enum class UserPaymentType(var number: Int, var str: String) {
    // 장바구니
    USER_CART_FRAGMENT(1, "UserCartFragment"),

    // 장바구니 2 (1회 구매)
    USER_CART_FRAGMENT1(2, "UserCartFragment1"),

    // 결제
    USER_PAYMENT_SCREEN_FRAGMENT(3, "UserPaymentScreenFragment")
}

enum class ProductInfoType(var number: Int, var str: String) {
    // 상품 (tabLayout을 갖고있는 화면)
    USER_PRODUCT_INFO_TYPE(0, "UserProductInfoFragment"),

    // 상품 설명
    USER_PRODUCT_INFO_DESCRIPTION_TYPE(1, "UserProductInfoDescription"),

    // 상품 상세 정보
    USER_PRODUCT_INFO_DETAIL_TYPE(2, "UserProductInfoDetailFragment"),

    // 상품 후기
    USER_PRODUCT_INFO_REVIEW_TYPE(3, "UserProductInfoReviewFragment"),

    // 상품 후기 작성
    USER_PRODUCT_INFO_WRITE_TYPE(4, "UserProductWriteReviewFragment"),

    // 상품 주문 다이얼로그
    USER_PRODUCT_INFO_DIALOG_TYPE(5, "UserProductInfoDialogFragment"),

    // 상품 리스트 뷰
    USER_PRODUCT_SHOW_LIST_TYPE(6, "userProductShowListFragment")
    // 구매 뷰
}


enum class ProductCategoryDetailType(var number: Int, var str: String) {
    PRODUCT_CATEGORY_STRAWBERRY(0, "딸기"),
    PRODUCT_CATEGORY_APPLE(1, "사과"),
    PRODUCT_CATEGORY_TANGERINE(2, "감귤"),
    PRODUCT_CATEGORY_GRAPE(3, "포도"),
    PRODUCT_CATEGORY_MANGO(4, "망고"),
    PRODUCT_CATEGORY_BLUEBERRY(5, "블루베리"),
    PRODUCT_CATEGORY_KIWI(6, "키위"),
    PRODUCT_CATEGORY_ORANGE(7, "오렌지"),
    PRODUCT_CATEGORY_SINGLE(8, "1인 가구"),
    PRODUCT_CATEGORY_BULK(9, "대용량"),
    PRODUCT_CATEGORY_PACKAGE(10, "패키지"),
    PRODUCT_CATEGORY_SALE(11, "특가"),
    PRODUCT_CATEGORY_SEARCH(12, "검색"),
    PRODUCT_CATEGORY_DOMESTIC(13, "국산"),
    PRODUCT_CATEGORY_IMPORTED(14, "수입")

}

// 관리자 상태
enum class AdminSate(val number: Int, val str: String) {
    // 정상
    ADMIN_STATE_NORMAL(1, "정상"),

    // 탈퇴
    ADMIN_STATE_SIGN_OUT(2, "탈퇴")
}

enum class AdminSalesState(val num: Int, val str: String) {
    ADMIN_SALES_STATE_NORMAL(1, "정상"),
    ADMIN_SALES_STATE_ABNORMAL(2, "비정상"),

}

enum class CartState(val num: Int, val str: String) {
    CART_STATE_NORMAL(1, "정상"),
    CART_STATE_ABNORMAL(2, "비정상"),

}

enum class DeliveryCycleWeeks(val num: Int, val str: String) {
    DELIVERY_CYCLE_WEEKS_NONE(0, "1회 구매"),
    DELIVERY_CYCLE_WEEKS_ONE(1, "1 주"),
    DELIVERY_CYCLE_WEEKS_TWO(2, "2 주"),
    DELIVERY_CYCLE_WEEKS_THREE(3, "3 주"),
    DELIVERY_CYCLE_WEEKS_FOUR(4, "4 주"),
}

enum class DeliveryCycleDays(val num: Int, val str: String) {
    DELIVERY_CYCLE_DAYS_NONE(0, "1회 구매"),
    DELIVERY_CYCLE_DAYS_MONDAY(1, "월요일"),
    DELIVERY_CYCLE_DAYS_TUESDAY(2, "화요일"),
    DELIVERY_CYCLE_DAYS_WEDNESDAY(3, "수요일"),
    DELIVERY_CYCLE_DAYS_THURSDAY(4, "목요일"),
    DELIVERY_CYCLE_DAYS_FRIDAY(5, "금요일"),
    DELIVERY_CYCLE_DAYS_SATURDAY(6, "토요일"),
    DELIVERY_CYCLE_DAYS_SUNDAY(7, "일요일"),
}

enum class CartProductState(val num: Int, val str: String) {
    CART_PRODUCT_STATE_NORMAL(1, "정상"),
    CART_PRODUCT_STATE_ABNORMAL(2, "비정상"),
}

enum class CartProductIsCheckStateBoolType(val bool: Boolean, val str: String) {
    CART_PRODUCT_IS_CHECKED_TRUE(true, "체크된 품목"),
    CART_PRODUCT_IS_CHECKED_FALSE(false, "체크 되지 않은 품목"),
}

enum class CartProductSubscribeState(val num: Int, val str: String) {
    CART_PRODUCT_STATE_NOT_SUBSCRIBE(0, "비구독"),
    CART_PRODUCT_STATE_SUBSCRIBE(1, "구독"),
}

enum class DeliveryDefaultAddressBoolType(val bool: Boolean, val str: String) {
    DELIVERY_ADDRESS_TYPE_IS_DEFAULT(true, "기본 배송지"),
    DELIVERY_ADDRESS_TYPE_IS_NOT_DEFAULT(false, "일반 배송지"),
}

enum class DeliveryAddressState(val num: Int, val str: String) {
    DELIVERY_ADDRESS_STATE_NORMAL(1, "정상"),
    DELIVERY_ADDRESS_STATE_ABNORMAL(2, "비정상"),
}

enum class DeliverySubscribeState(val num: Int, val str: String) {
    DELIVERY_STATE_NOT_SUBSCRIBE(0, "비구독"),
    DELIVERY_STATE_SUBSCRIBE(1, "구독"),
}

enum class DeliveryState(val num: Int, val str: String) {
    DELIVERY_STATE_READY_FOR_SHIPMENT(1, "출고준비"),
    DELIVERY_STATE_SHIPMENT_COMPLETE(2, "출고완료"),
    DELIVERY_STATE_READY_FOR_DELIVERY(3, "배송준비"),
    DELIVERY_STATE_IN_DELIVERY(4, "배송중"),
    DELIVERY_STATE_DELIVERED(5, "배송완료")
}

enum class InquiryIsSecretBoolType(val bool: Boolean, val str: String) {
    INQUIRY_IS_SECRET_TRUE(true, "비공개"),
    INQUIRY_IS_SECRET_FALSE(false, "공개"),
}

enum class InquiryIsOpenBoolType(val bool: Boolean, val str: String) {
    INQUIRY_IS_OPEN_TRUE(true, "공개 문의"),
    INQUIRY_IS_OPEN_FALSE(false, "비공개 문의"),
}

enum class OrderPaymentOption(val num: Int, val str: String) {
    ORDER_PAYMENT_OPTION_ACCOUNT(1, "계좌이체"),
    ORDER_PAYMENT_OPTION_CARD(2, "카드결제"),
    ORDER_PAYMENT_OPTION_KAKAO_PAY(3, "카카오페이"),
    ORDER_PAYMENT_OPTION_NAVER_PAY(4, "네이버페이")
}

enum class DeliveryOption(val num: Int, val str: String) {
    DOOR_DELIVERY(1, "문앞배송"),
    PARCEL_LOCKER(2, "택배함"),
    SECURITY_OFFICE(3, "경비실")
}

enum class IsOneTimeDeliveryBoolType(val bool: Boolean, val str: String) {
    ONE_TIME_DELIVERY(true, "일회성배송"),
    REGULAR_DELIVERY(false, "정기배송")
}

enum class OrderSearchPeriod(val num: Int, val str: String) {
    ORDER_SEARCH_PERIOD_ALL(1, "전체"),
    ORDER_SEARCH_PERIOD_15DAYS(2, "15일"),
    ORDER_SEARCH_PERIOD_ONE_MONTH(3, "1개월"),
    ORDER_SEARCH_PERIOD_THREE_MONTH(4, "3개월"),
    ORDER_SEARCH_PERIOD_SIX_MONTH(5, "6개월"),


}

enum class OrderState(val num: Int, val str: String) {
    ORDER_STATE_PAYMENT_PENDING(1, "결제대기중"),
    ORDER_STATE_PAYMENT_COMPLETED(2, "결제 완료"),
    ORDER_STATE_CANCELLED(3, "주문 취소"),
    ORDER_STATE_RETURNED(4, "반품"),
    ORDER_STATE_EXCHANGED(5, "교환")
}

enum class OrderProductState(val num: Int, val str: String) {
    ORDER_PRODUCT_STATE_NORMAL(1, "정상"),
    ORDER_PRODUCT_STATE_ABNORMAL(2, "비정상"),
}

enum class ProductType(val num: Int, val str: String) {
    PRODUCT_TYPE_FRESH(1, "신선 과일"),      // 신선 상태의 상품 ex) 생과일, 유기농
    PRODUCT_TYPE_PROCESSED(2, "가공 과일"),  // 가공된 상품 ex) 건조, 냉동,
    PRODUCT_TYPE_MIXED(3, "혼합 과일"),      // 혼합 상품 ex) 과일세트, 바구니
    PRODUCT_TYPE_OTHER(4, "기타")       // 기타 상품
}

enum class ProductSellingState(val num: Int, val str: String) {
    PRODUCT_STATE_NORMAL(1, "정상"),
    PRODUCT_STATE_ABNORMAL(2, "비정상"),
}

enum class ReviewState(val num: Int, val str: String) {
    REVIEW_STATE_VISIBLE(1, "노출상태"),  // 리뷰가 노출되는 상태
    REVIEW_STATE_HIDDEN(2, "숨김상태")    // 리뷰가 숨겨진 상태
}

enum class SubscribeState(val num: Int, val str: String) {
    SUBSCRIBE_STATE_NOT_SUBSCRIBE(0, "비구독"),
    SUBSCRIBE_STATE_SUBSCRIBE(1, "구독"),
}

enum class CustomerUserGender(val num: Int, val str: String) {
    CUSTOMER_USER_GENDER_MALE(1, "남자"),
    CUSTOMER_USER_GENDER_FEMALE(2, "여자")
}

enum class CustomerUserState(val num: Int, val str: String) {
    CUSTOMER_USER_STATE_ACTIVE(1, "활동"),    // 활동 중인 사용자
    CUSTOMER_USER_STATE_WITHDRAWN(2, "탈퇴"), // 탈퇴한 사용자
    CUSTOMER_USER_STATE_SUSPENDED(3, "정지")  // 정지된 사용자
}


// hyeonseo 0123
// 로그인 결과
enum class LoginResult(val number: Int, val str: String) {
    LOGIN_RESULT_SUCCESS(1, "로그인 성공"),
    LOGIN_RESULT_ID_NOT_EXIST(2, "존재하지 않는 아이디"),
    LOGIN_RESULT_PASSWORD_INCORRECT(3, "잘못된 비밀번호"),
    LOGIN_RESULT_SIGN_OUT_MEMBER(4, "탈퇴한 회원"),
}



