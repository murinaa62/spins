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


    fun init(@DrawableRes spins: List<Int>, callback: (text: Win) -> Unit) {
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
                    callback(checkPrize())
                }
            }
        })
    }

    private fun checkPrize(): Win {
        val currList = curAdapter.currentList.takeLast(9)
        val matrix = arrayOf(
            intArrayOf(
                currList[0].image,
                currList[1].image,
                currList[2].image
            ),
            intArrayOf(
                currList[3].image,
                currList[4].image,
                currList[5].image
            ),
            intArrayOf(currList[6].image, currList[7].image, currList[8].image),
        )
        var multiplier = 0
        for (row in matrix) {
            if (row[0] == row[1] && row[0] == row[2]) {
                multiplier += 1
            }
            if (multiplier == 3) return Win.JACKPOT
        }
        if (matrix[0][0] == matrix[1][1] && matrix[0][0] == matrix[2][2]) {
            multiplier += 1
        }

        if (matrix[2][0] == matrix[1][1] && matrix[1][1] == matrix[0][2]) {
            multiplier += 1
        }
        return if (multiplier > 1) Win.BIG else if (multiplier == 1) Win.SMALL else Win.LOOSE
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
        val list = list.takeLast(9).toMutableList().apply {
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

    enum class Win {
        JACKPOT, SMALL, LOOSE, BIG
    }

}