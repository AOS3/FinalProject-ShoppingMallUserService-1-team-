package com.example.frume.fragment.user_fragment.product_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frume.home.HomeActivity
import com.example.frume.R
import com.example.frume.data_hj.DummyData
import com.example.frume.databinding.FragmentUserProductInfoReviewBinding
import com.example.frume.databinding.ItemReviewBinding
import com.google.android.material.divider.MaterialDividerItemDecoration


class UserProductInfoReviewFragment() : Fragment() {

    lateinit var fragmentUserProductInfoReviewBinding: FragmentUserProductInfoReviewBinding
    lateinit var homeActivity: HomeActivity

    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserProductInfoReviewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_product_info_review, container, false)
        homeActivity = activity as HomeActivity
        // test
        Log.d("test100", "UserProductInfoFragment")

        // 어뎁터 설정
        settingRecyclerView()

        // 리뷰쓰기 화면전환 버튼
        onClickButtonWriteReview()

        return fragmentUserProductInfoReviewBinding.root
    }

    // sehoon 리뷰작성 화면 이동 메서드
    private fun onClickButtonWriteReview() {
        fragmentUserProductInfoReviewBinding.buttonUserProductInfoReview.setOnClickListener {
            val action = UserProductInfoFragmentDirections.actionUserProductInfoToUserProductInfoWriteReview()
            findNavController().navigate(action)
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView() {
        fragmentUserProductInfoReviewBinding.apply {
            // 어뎁터
            recyclerViewUserProductInfoReview.adapter = RecyclerViewReviewAdapter()
            // LayoutManager
            recyclerViewUserProductInfoReview.layoutManager = LinearLayoutManager(homeActivity)
            // 구분선
            val deco = MaterialDividerItemDecoration(homeActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewUserProductInfoReview.addItemDecoration(deco)
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewReviewAdapter : RecyclerView.Adapter<RecyclerViewReviewAdapter.ViewHolderMain>() {
        // ViewHolder
        inner class ViewHolderMain(val itemReviewBinding: ItemReviewBinding) : RecyclerView.ViewHolder(itemReviewBinding.root),
            View.OnClickListener {
            override fun onClick(v: View?) {
                // 사용자가 누른 동물 인덱스 담아준다.
                val dataBundle = Bundle()
                // dataBundle.putInt("animalIdx", testList[adapterPosition].)
                // ShowFragment로 이동한다.
                //  mainActivity.replaceFragment(FragmentName.SHOW_FRAGMENT, true, dataBundle)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {

            val rowMainBinding = ItemReviewBinding.inflate(layoutInflater, parent, false)

            val viewHolderMain = ViewHolderMain(rowMainBinding)

            // 리스너를 설정해준다.
            rowMainBinding.root.setOnClickListener(viewHolderMain)

            return viewHolderMain
        }

        override fun getItemCount(): Int {
            return DummyData.dummyList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.itemReviewBinding.ratingBarItemReview.rating = DummyData.dummyList[position].ratingPoint
            holder.itemReviewBinding.textViewRecyclerViewReviewTitle.text = DummyData.dummyList[position].title
            holder.itemReviewBinding.textViewRecyclerViewReviewDate.text = DummyData.dummyList[position].data
            holder.itemReviewBinding.textViewRecyclerViewReviewText.text = DummyData.dummyList[position].content
            holder.itemReviewBinding.imageViewRecyclerViewReview.setImageResource(DummyData.dummyList[position].imgPath)
        }
    }

    companion object {
        private const val ARG_NUMBER = "arg_number"
        fun newInstance(): UserProductInfoReviewFragment {
            return UserProductInfoReviewFragment().apply {
                // 값 전달 코드 번들 사용
                arguments = Bundle().apply {
                    // putInt(ARG_NUMBER, number)

                }
            }
        }

    }
}