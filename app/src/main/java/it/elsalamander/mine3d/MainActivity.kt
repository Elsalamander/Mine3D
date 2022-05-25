package it.elsalamander.mine3d

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/****************************************************************
 * Progetto per il corso: Programmazione di sistemi Embedded
 * App: Gioco -> Campo minato 3D
 * Per rendere un po dinamica l'app sono presenti varie
 * configurazioni, i quali:
 * - Sfondo in gioco.
 * - Mostrare o no il timer
 * - Mostrare o no il numero di bandiere piazzate, decrescente.
 * - Livello musica
 * - Livello effetti
 * - Controlli:
 *      - Scoprire
 *      - Piazzare bandiera
 *      - Sensibilità per la rotazione e zoom
 *      - Tempo di hold per il commando hold.
 *
 * Ci sono vari preset fra le impostazioni di controllo.
 *
 * Per la istanza di gioco ci sono 6 preset differenti, ma c'è la
 * possibilità di creare un game completamente custom, dove si può
 * scegliere la dimensione del Cubo e la percentuale di bombe presenti
 * con alcuni preset.
 *
 * Oltre a questi 2 modi per aprire un game, c'è la possibilità di recuperare
 * e continuare un game non terminato in precedenza.
 *
 * -----------------------------------------------------------------
 * Per realizzare tutto ciò sono state create 2 Activity:
 * - Activity 1:
 *      - Menu avvio app
 *      - Impostazioni Base
 *      - Impostazioni controlli
 *      - Selection Standard Game
 *
 * - Activity 2:
 *      - Game
 *      - GamePause
 *      - GameOver
 *      - Create Custom Game
 *
 * Ogni activity è costituita da fragment.
 *
 * Durante l'activity 2, più precisamente quando si avvia il game,
 * si fa uso particolare del contenuto del packege:
 * "it.elsalamander.mine3d.Game.Event" la classe più "importante"
 * che gestisce gli eventi è: "it.elsalamander.mine3d.Game.Event.Manager.EventManager"
 *
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //imposta lo schermo
        setContentView(R.layout.activity_main)
    }
}