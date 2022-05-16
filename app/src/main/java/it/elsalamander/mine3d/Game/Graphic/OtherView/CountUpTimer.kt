package it.elsalamander.mine3d.Game.Graphic.OtherView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*


class CountUpTimer: ViewModel() {

    private val mutableLiveData = MutableLiveData<Int>()

    private val BEGIN_AFTER = 1000L
    private var INTERVAL = 1000L
    private var counter = 0

    init{
        startTimer()
    }

    private fun startTimer() {
        val timer = Timer()
        //Schedule the specified task for repeated fixed-rate execution,
        // beginning after the specified delay. Subsequent executions
        // take place at approximately regular intervals, separated by
        // the specified period.
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                mutableLiveData.postValue(counter++)
            }
        }, BEGIN_AFTER, INTERVAL)
    }

    fun getLiveData(): MutableLiveData<Int> {
        return mutableLiveData
    }
}