package it.elsalamander.mine3d.Game.Game.Data.GameSett.Builder

import android.annotation.SuppressLint
import android.widget.SeekBar
import it.elsalamander.mine3d.Game.Game.GameBuildSettingsFragment

class BombBarEvent(var builder: GameSettBuilder, var fragment: GameBuildSettingsFragment) : SeekBar.OnSeekBarChangeListener {
    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range min..max where min
     * and max were set by [ProgressBar.setMin] and
     * [ProgressBar.setMax], respectively. (The default values for
     * min is 0 and max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    @SuppressLint("SetTextI18n")
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(!fromUser){
            //se la modifica non è fatta dall'utente ignora, perchè la barra può essere modificata dalla
            // funzione chiamata sotto
            return
        }
        builder.difficulty = progress/100.0
        fragment.upDateLayout(builder)
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