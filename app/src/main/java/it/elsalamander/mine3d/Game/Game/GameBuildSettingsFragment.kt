package it.elsalamander.mine3d.Game.Game

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import it.elsalamander.mine3d.Game.Game.Data.GameSett.Builder.*
import it.elsalamander.mine3d.Game.Game.Data.GameSett.Difficulty
import it.elsalamander.mine3d.R

/****************************************************************
 * Fragment della schermata del build della partita Custom
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GameBuildSettingsFragment : Fragment() {

    private lateinit var inputCell      : EditText
    private lateinit var viewCellSuBomb : TextView
    private lateinit var bombBar        : SeekBar
    private lateinit var bombText       : TextView
    private lateinit var easyButton     : Button
    private lateinit var mediumButton   : Button
    private lateinit var hardButton     : Button
    private lateinit var increment      : CheckBox
    private lateinit var inputIncrement : EditText
    private lateinit var startGame      : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_build_game_settings, container, false)

        val activity = this.activity as Game

        inputCell      = view.findViewById(R.id.build_game_settings_size_input)
        viewCellSuBomb = view.findViewById(R.id.build_game_settings_bombe_su_celle)
        bombBar        = view.findViewById(R.id.build_game_settings_bombe_bar)
        bombText       = view.findViewById(R.id.build_game_settings_bomb_num)
        easyButton     = view.findViewById(R.id.build_game_settings_difficulty_easy)
        mediumButton   = view.findViewById(R.id.build_game_settings_difficulty_medium)
        hardButton     = view.findViewById(R.id.build_game_settings_difficulty_hard)
        increment      = view.findViewById(R.id.build_game_settings_next)
        inputIncrement = view.findViewById(R.id.build_game_settings_next_input_text)
        startGame      = view.findViewById(R.id.build_game_settings_start)

        val buildGameSett = GameSettBuilder()

        //imposta il builder
        inputCell.setOnEditorActionListener(InputCellEvent(buildGameSett, this))
        bombBar.setOnSeekBarChangeListener(BombBarEvent(buildGameSett, this))

        easyButton.setOnClickListener(DifficultyButtonEvent(buildGameSett, this, Difficulty.EASY))
        mediumButton.setOnClickListener(DifficultyButtonEvent(buildGameSett, this, Difficulty.MEDIUM))
        hardButton.setOnClickListener(DifficultyButtonEvent(buildGameSett, this, Difficulty.HARD))

        increment.setOnCheckedChangeListener(IncrementCheckBoxEvent(buildGameSett, this))
        inputIncrement.setOnEditorActionListener(InputIncrementEvent(buildGameSett,this))

        //Evento startGameButton -> fai il Build del GameSettings -> Crea l'istanza di gioco -> passa al fragment GameFragment
        startGame.setOnClickListener {
            if(activity.gameInstance != null){
                if(activity.gameInstance?.grid != null){
                    activity.gameInstance?.grid?.resetGrid()
                }
            }
            activity.gameSett = buildGameSett.build()
            view.findNavController().navigate(R.id.action_game_build_settings_to_game)
        }

        this.upDateLayout(buildGameSett)
        return view
    }

    /**
     * Aggiorna il testo
     */
    @SuppressLint("SetTextI18n")    //soppresione poichè devo editare il contenuto di una textView
    fun upDateLayout(builder : GameSettBuilder){
        val size = this.inputCell.text.toString().toInt()
        if(size <= 2){
            this.inputCell.setText("3")
        }

        //aggiorna al textView che mostra numero di bombe su celle
        val n = builder.size
        val nC = (n*n*2 + n*(n-2)*2 + (n-2)*(n-2)*2)
        val bombe = (nC * builder.difficulty).toInt()
        viewCellSuBomb.text = "$bombe/$nC"

        //aggiorna la posizione della seekbar
        this.bombBar.progress = (builder.difficulty*100).toInt()
        this.bombText.text = "${this.bombBar.progress}%"

        //aggiorna la checkBox
        if(this.increment.isChecked){
            builder.incr = this.inputIncrement.text.toString().toInt()
        }else{
            builder.incr = 0
        }
    }
}