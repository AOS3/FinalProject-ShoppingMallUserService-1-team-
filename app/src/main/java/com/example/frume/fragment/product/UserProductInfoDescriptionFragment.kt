package com.example.frume.fragment.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.frume.R
import com.example.frume.activity.HomeActivity
import com.example.frume.activity.LoginActivity
import com.example.frume.databinding.FragmentUserProductInfoDescriptionBinding
import com.example.frume.factory.ProductInfoModelFactory
import com.example.frume.adapter.ProductCarouselAdapter
import com.example.frume.adapter.ProductImgAdapter
import com.example.frume.fragment.product.info_screen.ProductInfoViewModel
import com.example.frume.fragment.product.info_screen.UserProductInfoFragmentDirections
import com.example.frume.repository.ProductRepository
import com.example.frume.util.convertThreeDigitComma
import com.google.android.material.carousel.CarouselSnapHelper


class UserProductInfoDescriptionFragment : Fragment() {
    private var _binding: FragmentUserProductInfoDescriptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductImgAdapter
    private lateinit var adapter2: ProductCarouselAdapter
    lateinit var homeActivity: HomeActivity

    private var productDocId: String? = null
    private val viewModel: ProductInfoViewModel by lazy {
        val repository = ProductRepository()
        val factory = ProductInfoModelFactory(repository)
        ViewModelProvider(this, factory)[ProductInfoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productDocId = it.getString(ARG_PRODUCT_DOC_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeActivity = activity as HomeActivity
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_description, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setLayout()
    }


    private fun setLayout() {
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerViewUserProductInfoDescriptionCarouselImage)
        observeData()
        onClickBuyBtn()
    }

    // 구매버튼 리스너
    private fun onClickBuyBtn() {

        Log.d("test333", "${homeActivity.loginUserDocumentId}")

        if (homeActivity.loginUserDocumentId == "noUser"){
                // 다이얼로그 표시
                onClickTextDeleteProducts()
        }
        else{
            binding.buttonUserProductInfoDescriptionBuy.setOnClickListener {
                val action = UserProductInfoFragmentDirections.actionUserProductInfoToUserProductInfoDialog(productDocId!!)
                findNavController().navigate(action)}
        }
    }


    private fun observeData() {
        viewModel.loadProduct(productDocId!!)
        viewModel.items.observe(viewLifecycleOwner) {
            adapter = ProductImgAdapter(it.productImages)
            binding.recyclerViewUserProductInfoDescriptionDescriptionImage.adapter = adapter
            binding.textViewUserProductInfoDescriptionName.text = it.productName
            binding.textViewUserProductInfoDescriptionPrice.text = it.productPrice.convertThreeDigitComma()
            binding.textViewUserProductInfoDescription.text = it.productDescription

            adapter2 = ProductCarouselAdapter(it.productImages)
            binding.recyclerViewUserProductInfoDescriptionCarouselImage.adapter = adapter2
        }
    }

    // 문서 선택 삭제 리스너 참고하기
    private fun onClickTextDeleteProducts() {
        binding.apply {
            buttonUserProductInfoDescriptionBuy.setOnClickListener {
                homeActivity.showConfirmationDialog(
                    "Frume 로그인하기",
                    "로그인을 하시면 구매 서비스를 이용하실 수 있습니다",
                    "로그인하기",
                    "비회원으로 진행하기",
                    fun() {
                        // 로그인하기를 누르면 로그인 화면으로 이동

                        // 로그아웃 후 다시 로그인 화면으로 이동
                        val intent = Intent(homeActivity, LoginActivity::class.java)
                        startActivity(intent)

                        // 현재 액티비티 종료
                        homeActivity.finish()

                        // 비회원으로 진행하면 다이얼로그 내리기
                    })
            }
        }
    }

    companion object {
        private const val ARG_PRODUCT_DOC_ID = "product_doc_id"

        fun newInstance(productDocId: String): UserProductInfoDescriptionFragment {
            return UserProductInfoDescriptionFragment().apply {
                // 값 전달 코드 번들 사용
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_DOC_ID, productDocId)
                }
            }
        }
    }
}