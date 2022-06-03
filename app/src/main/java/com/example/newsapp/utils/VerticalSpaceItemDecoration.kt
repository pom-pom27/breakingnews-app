package com.example.newsapp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.adapter?.also {

            if (parent.getChildAdapterPosition(view) != it.itemCount - 1) {
                outRect.bottom = verticalSpaceHeight;
            }
        }
    }
}
