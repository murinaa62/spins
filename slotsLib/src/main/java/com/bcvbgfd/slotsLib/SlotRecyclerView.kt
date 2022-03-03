package com.bcvbgfd.slotsLib

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bcvbgfd.slotsLib.adapter.SlotListAdapter
import kotlin.random.Random

class SlotRecyclerView @JvmOverloads
constructor(context: Context, attr: AttributeSet? = null, defStyleAttr: Int = 0) :
    RecyclerView(context, attr, defStyleAttr) {

    private val curAdapter: SlotListAdapter
        get() = (adapter as SlotListAdapter)


    fun init(@DrawableRes spins: List<Int>, callback: (text: String) -> Unit) {
        listOfImages = spins
        setLayoutManager()
        adapter = SlotListAdapter()
        fetchList()
        setOnTouchListener { view, motionEvent -> true }
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    deleteList(curAdapter.currentList)
                    callback("Success")
                }
            }
        })
    }

    private fun setLayoutManager() {
        this.layoutManager = GridLayoutManager(context, 3)
    }

    @DrawableRes
    private var listOfImages = listOf(
        androidx.appcompat.R.drawable.abc_ab_share_pack_mtrl_alpha
    )

    private fun fetchList() {
        curAdapter.submitList((0..101).map {
            Slot(image = listOfImages.random())
        })
    }

    fun startSpin() {
        smoothScrollToPosition(adapter!!.itemCount)
    }

    private fun deleteList(list: List<Slot>) {
        val list =  list.takeLast(9).toMutableList().apply {
            addAll(getNewSlots())
        }.toList()
        curAdapter.submitList(list)
    }

    private fun getNewSlots(): List<Slot> =
        (0..101).map {
            Slot(image = listOfImages.random())
        }

    data class Slot(
        val id: Long = Random.nextLong(),
        @DrawableRes val image: Int,
    )

}