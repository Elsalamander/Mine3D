package it.elsalamander.mine3d.Game.Graphic.OtherView

import android.util.Log
import android.widget.Chronometer
import android.widget.TextView


class CountUpTimer(var text : TextView) : Chronometer.OnChronometerTickListener {

    private var countUpTimer : Long = 0

    init {
        countUpTimer = System.currentTimeMillis()
    }
    /**
     * Notification that the chronometer has changed.
     */
    override fun onChronometerTick(chronometer: Chronometer) {
        val countUp = (System.currentTimeMillis() - countUpTimer) / 1000
        val asText: String = (countUp / 60).toString() + ":" + countUp % 60
        text.text = asText
        Log.d("Timer", "Tick: $countUp")
    }
}