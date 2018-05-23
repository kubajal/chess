package controller

import com.sun.media.jfxmedia.events.PlayerEvent
import model.{Figure, PlayerColor}
import model.PlayerColor.PlayerColor

import scala.annotation.tailrec

class Algorithm(val initialState : InternalState, val maximizing : PlayerColor) {

  val evaluator = new Evaluator

  def run(): (Figure, (Int, Int)) = {

    val DEPTH = 4
    var move = (-1, -1)
    var figure : Figure = null;
    var maxi = -10000000 // minus infinity, score is being maximized
     for (f <- initialState.getFigures(maximizing)) {
      for (e <- initialState.findPossibleMoves(f)) {
        val score = recursion(DEPTH, initialState.makeMove(f, e), initialState.getOpponentColor(maximizing))
        println(f.x + " " + f.y + ": " + score)
        if(score > maxi){
          maxi = score
          move = e
          figure = f
        }
      }
    }
    return (figure, move)
  }

  def recursion(depth : Int, internalState : InternalState, color: PlayerColor) : Int = {
    if(depth == 0)
      evaluator.evaluateState(internalState)
    else if(color == maximizing){
      var maxi = -10000000 // minus infinity, score is being maximized
      for(figure <- internalState.getFigures(color)){
        if(figure != null){
          for(e <- internalState.findPossibleMoves(figure)){
            val score = recursion(depth - 1, internalState.makeMove(figure, e), internalState.getOpponentColor(color))
            if(score > maxi){
              maxi = score
            }
          }
        }
      }
      if(maxi == -10000000)
        evaluator.evaluateState(internalState)
      return maxi
    }
    else{
      var mini = 10000000 // plus infinity, score is being minimalized
      for(figure <- internalState.getFigures(color)){
        if(figure != null){
          for(e <- internalState.findPossibleMoves(figure)){
            val score = recursion(depth - 1, internalState.makeMove(figure, e), internalState.getOpponentColor(color))
            if(score < mini){
              mini = score
            }
          }
        }
      }
      if(mini == 10000000)
        evaluator.evaluateState(internalState)
      return mini
    }
  }
}