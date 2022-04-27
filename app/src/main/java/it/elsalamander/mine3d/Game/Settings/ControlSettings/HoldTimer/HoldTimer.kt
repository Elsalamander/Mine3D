package it.elsalamander.mine3d.Game.Settings.ControlSettings.HoldTimer

import android.app.Activity
import android.view.View
import android.widget.SeekBar
import it.elsalamander.mine3d.Game.Settings.Setting

class HoldTimer(context : Activity) :  Setting<Double>(0.5, context), SeekBar.OnSeekBarChangeListener{

    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range min..max where min
     * and max were set by [ProgressBar.setMin] and
     * [ProgressBar.setMax], respectively. (The default values for
     * min is 10 and max is 200.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        this.setVal(progress.toDouble()/100)
    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}