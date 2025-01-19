package com.example.frume.fragment.user_fragment.user_cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.R
import com.example.frume.data_ye.DummyData
import com.example.frume.databinding.FragmentUserCart1Binding
import com.example.frume.databinding.ItemUsercartListBinding

class UserCartFragment1() : Fragment() {

    private var _binding: FragmentUserCart1Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_cart1, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickCartOrderProduct()
    }
    // 배송지 변경 버튼 클릭 시, UserAddressManageFragment로 이동
    fun onClickCartDeliverySpotChange() {
        binding.buttonUserCartDialogModifyAddress.setOnClickListener {

        }
    }

    // 구매하기 버튼 클릭 시, UserPaymentScreenFragment로 이동
    private fun onClickCartOrderProduct() {
        binding.buttonUserCartOrder.setOnClickListener {
            val action = UserCartFragmentDirections.actionNavigationCartToUserPaymentScreen()
            findNavController().navigate(action)
        }
    }

    // 프래그먼트 이동을 처리하는 함수
    private fun navigateToFragment(fragment: Fragment) {

    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerViewUserCartProduct() {
        binding.apply {

        }
    }

    // RecyclerView의 어댑터
    inner class RecyclerViewUserCartProductAdapter(private val cartItems: List<DummyData.CartItem>) : RecyclerView.Adapter<RecyclerViewUserCartProductAdapter.ViewHolderStudentList>() {

        // ViewHolder
        inner class ViewHolderStudentList(val itemUsercartListBinding: ItemUsercartListBinding) : RecyclerView.ViewHolder(itemUsercartListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStudentList {
            val itemUsercartListBinding = ItemUsercartListBinding.inflate(layoutInflater, parent, false)
            return ViewHolderStudentList(itemUsercartListBinding)
        }

        override fun getItemCount(): Int {
            return cartItems.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentList, position: Int) {
            val item = cartItems[position]
            // 더미 데이터 바인딩
            holder.itemUsercartListBinding.textViewRecyclerViewProductName.text = item.productName
            holder.itemUsercartListBinding.textViewRecyclerViewProductPrice.text = "${item.productPrice}"

            // Drawable 리소스를 바인딩
            holder.itemUsercartListBinding.imageViewRecyclerViewImage.setImageResource(item.imageResId)
        }
    }
}


