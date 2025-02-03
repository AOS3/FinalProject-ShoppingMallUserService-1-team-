package com.example.frume.fragment.home_fragment.my_info.order_inquiry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.frume.R
import com.example.frume.activity.HomeActivity
import com.example.frume.databinding.FragmentUserOrderDetailBinding
import com.example.frume.model.DeliveryAddressModel
import com.example.frume.model.DeliveryModel
import com.example.frume.model.OrderModel
import com.example.frume.model.OrderProductModel
import com.example.frume.service.DeliveryService
import com.example.frume.service.OrderProductService
import com.example.frume.service.OrderService
import com.example.frume.service.UserDeliveryAddressService
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


// sehoon 주문 상세 내역
class UserOrderDetailFragment : Fragment() {
    private var _binding: FragmentUserOrderDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var homeActivity: HomeActivity

    var orderProductModel : OrderProductModel? = null
    var deliveryModel : DeliveryModel? = null
    var deliverAddressModel : DeliveryAddressModel? =null
    var orderModel : OrderModel? = null

    // args 변수의 값을 직접 초기화하지 않고, navArgs()가 제공하는 위임 로직에 따라 자동으로 값을 가져오게 됩니다.
    private val args: UserOrderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeActivity = activity as HomeActivity
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_order_detail, container, false)

        // 뒤로가기 버튼 리스너
        onClickNavigationIcon()

        // 반품/취소 화면 연결 버튼
        moveToCancelAndReturn()

        // 해당 모델들 가져오는 메서드
        gettingModels()

        // 가져온 모델을 이용해 화면을 그리는 메서드 호출
        settingView()


        return binding.root
    }


    // 뒤로가기 버튼 리스너
    private fun onClickNavigationIcon() {

        binding.toolbarUserOrderDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 반품 교환 및 취소 화면 연결
    private fun moveToCancelAndReturn() {
        binding.buttonUserOrderDetailCancel.setOnClickListener {
            val action =
                UserOrderDetailFragmentDirections.actionUserOrderDetailToUserCancelAndReturn()
            findNavController().navigate(action)
        }
    }

    // orderProductModel 가져오는 suspend 메서드
    private suspend fun gettingOrderProductModel(): OrderProductModel {
        // IO 디스패처를 사용하여 OrderProductService의 결과를 기다린 후 반환
        return withContext(Dispatchers.IO) {
            OrderProductService.gettingMyOrderProductItem(args.orderDocId, args.orderProductDocId)
        }
    }

    // orderProductModel 가져오는 suspend 메서드
    private suspend fun gettingOrderModel(orderDocId : String): OrderModel {
        // IO 디스패처를 사용하여 OrderProductService의 결과를 기다린 후 반환
        return withContext(Dispatchers.IO) {
            OrderService.gettingOrderByOrderDocId(orderDocId)
        }
    }

    // deliveryModel 가져오는 메서드
    private suspend fun gettingDeliveryModel(deliveryDocId : String) :DeliveryModel{
        // IO 디스패처를 사용하여 OrderProductService의 결과를 기다린 후 반환
        return withContext(Dispatchers.IO) {
            DeliveryService.gettingDeliveryByDocId(deliveryDocId)
        }
    }

    // deliveryAddressModel 가져오는 메서드
    private suspend fun gettingDeliveryAddressModel(deliveryAddressDocId : String) :DeliveryAddressModel{
        // IO 디스패처를 사용하여 OrderProductService의 결과를 기다린 후 반환
        return withContext(Dispatchers.IO) {
            UserDeliveryAddressService.gettingSelectedDeliveryAddress(deliveryAddressDocId)
        }

    }

    // 해당 모델들 가져오는 메서드
    fun gettingModels() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // 1. orderProductModel 가져오기
                orderProductModel = withContext(Dispatchers.IO) { gettingOrderProductModel() }
                Log.d("test100", "orderProductModel 가져옴: $orderProductModel")

                // 2. orderModel 가져오기 (orderProductModel에서 orderId 사용)
                orderModel = withContext(Dispatchers.IO) { gettingOrderModel(orderProductModel!!.orderId) }
                Log.d("test100", "orderModel 가져옴: $orderModel")

                // 3. deliveryModel 가져오기 (orderModel에서 deliverDocId 사용)
                deliveryModel = withContext(Dispatchers.IO) { gettingDeliveryModel(orderModel!!.deliverDocId) }
                Log.d("test100", "deliveryModel 가져옴: $deliveryModel")

                // 4. deliveryAddressModel 가져오기 (deliveryModel에서 deliveryAddressDocId 사용)
                deliverAddressModel = withContext(Dispatchers.IO) { gettingDeliveryAddressModel(deliveryModel!!.deliveryAddressDocId) }
                Log.d("test100", "deliverAddressModel 가져옴: $deliverAddressModel")

            } catch (e: Exception) {
                Log.e("test100", "gettingModels() 실행 중 오류 발생: ${e.message}", e)
            }
        }
    }

     // timeStamp -> String 변환
         private fun convertToDate(timeStamp: Timestamp): String {
             // Firestore Timestamp를 Date 객체로 변환
             val date = timeStamp.toDate()

             // 한국 시간대 (Asia/Seoul)로 설정
         val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.KOREA)
             dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

             return dateFormat.format(date)
         }

    // 가져온 모델을 이용해 화면을 그리는 메서드
    private fun settingView() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // 2초 동안 데이터를 기다림
                withTimeout(2000) { // 2초 제한
                    // 최대 2초 동안 기다리기
                    while (deliverAddressModel == null) {
                        delay(100) // 0.1초 간격으로 확인
                    }
                }

                // 데이터가 준비되었으면 화면 업데이트
                binding.apply {
                    textViewUserOrderDetailProductNameContent.text = orderProductModel!!.orderProductName
                    textViewUserOrderDetailPriceContent.text = "${orderProductModel!!.orderProductTotalPrice} 원"
                    textViewUserOrderDetailQuantityContent.text = "${orderProductModel!!.orderProductCount} 개"
                    textViewUserOrderDetailShippingAddressContent.text = "${deliverAddressModel!!.deliveryAddressBasicAddress} ${deliverAddressModel!!.deliveryAddressDetailAddress}"

                    Glide.with(imageViewUserOrderDetailImage.context)
                        .load(orderProductModel!!.orderProductImagePath)
                        .into(imageViewUserOrderDetailImage)


                    // 날짜 변환
                    val deliveryDateString = convertToDate(deliveryModel!!.deliveryTimeStamp)
                    textViewUserOrderDetailOrderDateContent.text = deliveryDateString

                    textViewUserOrderDetailShippingStatusContent.text = deliveryModel!!.deliveryState.str
                    textViewUserOrderDetailOrderStatusContent.text = orderProductModel!!.orderState.str
                }

            } catch (e: TimeoutCancellationException) {
                // 데이터가 2초 내에 로딩되지 않으면 "데이터가 없습니다" 메시지 처리
                binding.apply {
                    textViewUserOrderDetailProductNameContent.text = "데이터가 없습니다"
                    textViewUserOrderDetailPriceContent.text = "데이터가 없습니다"
                    textViewUserOrderDetailQuantityContent.text = "데이터가 없습니다"
                    textViewUserOrderDetailShippingAddressContent.text = "데이터가 없습니다"
                    textViewUserOrderDetailOrderDateContent.text = "데이터가 없습니다"
                    textViewUserOrderDetailShippingStatusContent.text = "데이터가 없습니다"
                    textViewUserOrderDetailOrderStatusContent.text = "데이터가 없습니다"
                }
                // 추가로 데이터가 없다는 메시지를 Toast로 표시
                Toast.makeText(context, "데이터가 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

}