package com.bcvbgfd.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bcvbgfd.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    @DrawableRes
    private val listOfImages = listOf(
        R.drawable.q01,
        R.drawable.q02,
        R.drawable.q04,
        R.drawable.q05,
        R.drawable.q06,
        R.drawable.q07,
        R.drawable.q8,
        R.drawable.q9,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.slots.init(listOfImages) {
            Log.e("Slots", "win = $it")
        }
        binding.spinButton.setOnClickListener {
            binding.slots.startSpin()
        }
    }
}