package com.example.otusproject_ermoshina.utill

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.utill.base.BaseDecorator

class DecoratorParentChannels (private val sizelist: Int, private val context: Context) :
    BaseDecorator(sizelist) {
    /***
     ****** This paints can draw background. You can change color
     */

    init {
        myPaintStroke.color = context.getColor(R.color.color_on_primary)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

    }
    companion object{
        const val MARGIN_TOP = 20
    }

}