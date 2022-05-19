package it.elsalamander.mine3d.Game.Event.Manager

import it.elsalamander.mine3d.Game.Game.Data.GameInstance


/****************************************************************
 * Classe astratta per definire la base di ogni evento presento
 * nel gioco.
 * Ogni evento deve sapere l'istanza di gioco corrente.
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
abstract class EventGame(var instanceGame : GameInstance)