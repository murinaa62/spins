package com.bcvbgfd.slotsLib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bcvbgfd.slotsLib.adapter.SlotListAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class SlotRecyclerView @JvmOverloads
constructor(context: Context, attr: AttributeSet? = null, defStyleAttr: Int = 0) :
    RecyclerView(context, attr, defStyleAttr) {

    lateinit var player: Player

    private val _isSpinning = MutableStateFlow(false)
    val isSpinning = _isSpinning.asStateFlow()

    private val curAdapter: SlotListAdapter
        get() = (adapter as SlotListAdapter)


    fun init(
        @DrawableRes spins: List<Int>,
        player: Player = Player(),
        callback: (text: SpinResult) -> Unit
    ) {
        this.player = player
        listOfImages = spins
        setLayoutManager()
        adapter = SlotListAdapter()
        fetchList()
        setOnTouchListener { view, motionEvent -> true }
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    _isSpinning.value = false
                    deleteList(curAdapter.currentList)
                    val result = checkPrize()
                    player.givePrize(result)
                    callback(result)
                }
            }
        })
    }

    private fun checkPrize(): SpinResult {
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
            if (multiplier == 3) return SpinResult.JACKPOT
        }
        if (matrix[0][0] == matrix[1][1] && matrix[0][0] == matrix[2][2]) {
            multiplier += 1
        }

        if (matrix[2][0] == matrix[1][1] && matrix[1][1] == matrix[0][2]) {
            multiplier += 1
        }
        return if (multiplier > 1) SpinResult.BIG else if (multiplier == 1) SpinResult.SMALL else SpinResult.LOOSE
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
        if (_isSpinning.value) return
        if (player.trySpin()){
            _isSpinning.value = true
            smoothScrollToPosition(adapter!!.itemCount)
        }
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

}