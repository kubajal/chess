package controller

import com.sun.media.jfxmedia.events.PlayerEvent
import model.PlayerColor
import model.PlayerColor.PlayerColor

import scala.annotation.tailrec

class Algorithm(val controller : Controller, val maximizing : PlayerColor) {

  val initialState = new InternalState(controller.getWhiteFigures, controller.getBlackFigures, controller.getBoard)

  //@tailrec
  def minimax(depth : Int, internalState : InternalState, playerColor: PlayerColor) : Int = {
    0
  }
}