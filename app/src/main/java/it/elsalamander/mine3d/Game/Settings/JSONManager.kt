package it.elsalamander.mine3d.Game.Settings

import android.app.Activity
import it.elsalamander.mine3d.Game.Settings.BaseSettings.BaseSettings
import it.elsalamander.mine3d.Game.Settings.ControlSettings.ControlSettings

/****************************************************************
 * Impostazioni di base che sono usate pre creare un game, esse sono contenute
 * in un file JSON.
 *
 * La classe deve leggere i dati richiesti.
 *
 * I Dati da salvare sono:
 * - Nome utente
 * - Tipo Tema
 * - Visualizzare il Timer o no
 * - Visualizzare il numero di bombe mancanti
 * - Livello musica
 * - Livello Effetti
 * - Vibbrazione gameover
 *
 * - Controlli:
 *      - Scopri
 *      - Segna bandiera
 *      - SensibilitÃ  drag
 *      - Tempo di Hold
 *
 * Anche le ultime impostazioni usate per il customGame
 * - Numero Lato
 * - DifficoltÃ  (facile,medio,difficile,custom)
 * - Percentuale bombe
 * - Incremento
 * - Valore incremento
 *
 *
 * Eventualmente una classifica
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class JSONManager(val context : Activity) {
    var baseSett : BaseSettings = BaseSettings(context)
    var controlSett : ControlSettings = ControlSettings(context)
}