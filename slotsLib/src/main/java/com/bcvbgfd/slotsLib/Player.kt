package com.bcvbgfd.slotsLib

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class Player(
    startMoney: Int = 10_000,
    startBet: Int = 500,
    private val step: Int = 500,
    private val multi: Map<SpinResult, Int> = mapOf(
        SpinResult.BIG to 3,
        SpinResult.JACKPOT to 100,
        SpinResult.SMALL to 2,
        SpinResult.LOOSE to 0
    )
) {

    private val _currentMoney = MutableStateFlow(startMoney)
    val currentMoney = _currentMoney.asStateFlow()

    private val _currentBet = MutableStateFlow(startBet)
    val currentBet = _currentBet.asStateFlow()

    private val _notEnoughMoney = MutableSharedFlow<String>(0, 1, BufferOverflow.DROP_LATEST)
    val notEnoughMoney = _notEnoughMoney.asSharedFlow()

    //Return true if can spin and false if not
    //Decrement current money if spin with the value of current bet
    fun trySpin() = if (currentBet.value <= currentMoney.value) {
        _currentMoney.value -= _currentBet.value
        true
    } else {
        _notEnoughMoney.tryEmit("No money for spin")
        false
    }

    fun incrementBet() {
        if (_currentMoney.value >= _currentBet.value + step)
            _currentBet.value += step
    }

    fun decrementBet() {
        if (_currentBet.value - step > 0)
            _currentBet.value -= step
    }

    fun givePrize(result: SpinResult) {
        when (result) {
            SpinResult.BIG -> _currentMoney.value += _currentBet.value * multi.getValue(SpinResult.BIG)
            SpinResult.JACKPOT -> _currentMoney.value += _currentBet.value * multi.getValue(SpinResult.JACKPOT)
            SpinResult.SMALL -> _currentMoney.value += _currentBet.value * multi.getValue(SpinResult.SMALL)
            SpinResult.LOOSE -> _currentMoney.value += _currentBet.value* multi.getValue(SpinResult.LOOSE)
        }
    }


}