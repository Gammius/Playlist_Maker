package com.example.playlist_maker.Utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val leftRightSpacing: Int,
    private val topSpacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.top = topSpacing

        if (column == 0) {
            outRect.left = leftRightSpacing
        } else {
            outRect.left = spacing
        }

        if (column == spanCount - 1) {
            outRect.right = leftRightSpacing
        } else {
            outRect.right = spacing
            outRect.bottom = 0
        }
    }
}
