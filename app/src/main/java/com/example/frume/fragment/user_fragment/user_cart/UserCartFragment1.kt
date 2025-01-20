package com.example.frume.fragment.user_fragment.user_cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.data_ye.DummyData
import com.example.frume.data_ye.TempCartProduct
import com.example.frume.databinding.FragmentUserCart1Binding
import com.example.frume.databinding.ItemUsercartListBinding

class UserCartFragment1 : Fragment() {

    private var _binding: FragmentUserCart1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_cart1, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        settingRecyclerViewUserCartProduct()

        // 버튼 클릭 리스너 설정
        onClickCartOrderProduct()
        onClickCartDeliverySpotChange()
    }

    // 배송지 변경 버튼 클릭 시, UserAddressManageFragment로 이동
    private fun onClickCartDeliverySpotChange() {
        binding.buttonUserCartDialogModifyAddress.setOnClickListener {
            val action = UserCartFragmentDirections.actionNavigationCartToUserAddressManage()
            findNavController().navigate(action)
        }
    }

    // 구매하기 버튼 클릭 시, UserPaymentScreenFragment로 이동
    private fun onClickCartOrderProduct() {
        binding.buttonUserCartOrder.setOnClickListener {
            val action = UserCartFragmentDirections.actionNavigationCartToUserPaymentScreen()
            findNavController().navigate(action)
        }
    }

    // RecyclerView 설정 메서드
    private fun settingRecyclerViewUserCartProduct() {
        binding.recyclerViewUserCart1.apply {
            // LinearLayoutManager를 사용하여 RecyclerView가 세로로 스크롤 되도록 설정
            layoutManager = LinearLayoutManager(context)
            // 어댑터 설정, DummyData.CartItemList를 어댑터에 전달
            adapter = RecyclerViewUserCartProductAdapter(DummyData.CartItemList)
        }
    }

    // RecyclerView의 어댑터
    inner class RecyclerViewUserCartProductAdapter(private val cartItems: List<TempCartProduct>) :
        RecyclerView.Adapter<RecyclerViewUserCartProductAdapter.ViewHolderUserCartProduct>() {

        // ViewHolder (각 아이템을 바인딩할 뷰 홀더)
        inner class ViewHolderUserCartProduct(val binding: ItemUsercartListBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserCartProduct {
            val binding = ItemUsercartListBinding.inflate(layoutInflater, parent, false)
            return ViewHolderUserCartProduct(binding)
        }

        override fun getItemCount(): Int {
            return cartItems.size
        }

        override fun onBindViewHolder(holder: ViewHolderUserCartProduct, position: Int) {
            val item = cartItems[position]

            // 더미 데이터 바인딩
            holder.binding.apply {
                textViewRecyclerViewProductName.text = item.productName
                textViewRecyclerViewProductPrice.text = "${item.productPrice}원"
                imageViewRecyclerViewImage.setImageResource(item.imageResId)
            }
        }
    }
}



