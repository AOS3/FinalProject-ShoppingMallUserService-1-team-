package com.example.frume.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemMarginDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(margin, 0, 0, margin) // 상, 좌, 우, 하 여백 설정
    }
}