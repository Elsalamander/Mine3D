package it.elsalamander.mine3d.Game.Game.Data.GameSett.Builder

import android.widget.CompoundButton
import it.elsalamander.mine3d.Game.Game.GameBuildSettingsFragment

class IncrementCheckBoxEvent(var builder: GameSettBuilder, var fragment: GameBuildSettingsFragment): CompoundButton.OnCheckedChangeListener{
    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        builder.next = isChecked
    }
}