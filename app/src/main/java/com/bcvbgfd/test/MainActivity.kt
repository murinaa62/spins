package com.bcvbgfd.test

import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bcvbgfd.slotsLib.Player
import com.bcvbgfd.test.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    @DrawableRes
    private val listOfImages = listOf(
        R.drawable.q01,
        R.drawable.q02
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.slots.init(listOfImages, Player(10000, 200)) {
            Log.e("Slots", "win = $it")
        }
        binding.spinButton.setOnClickListener {
            binding.slots.startSpin()
        }
        observeState()
    }

    private fun observeState() {
        val player = binding.slots.player
        player.currentMoney.mapLatest {
            Log.e("Player", "currentMoney = $it")
        }.launchIn(lifecycleScope)
        player.currentBet.mapLatest {
            Log.e("Player", "currentBet = $it")
        }.launchIn(lifecycleScope)
        player.notEnoughMoney.mapLatest {
            Log.e("Player", "error = $it")
        }.launchIn(lifecycleScope)
        binding.slots.isSpinning.mapLatest {
            Log.e("Player", "error = $it")
        }.launchIn(lifecycleScope)
    }
}