package controller

import model.{Figure, PlayerColor}
import model.PlayerColor.PlayerColor

class Algorithm(val initialState : InternalState, val maximizing : PlayerColor) {

  val evaluator = new Evaluator(maximizing)
  val INFINITY = 1000000

  def run(): (Figure, (Int, Int)) = {

    val DEPTH = 2
    var move = (-1, -1)
    var figure : Figure = null;
    var maxi = -INFINITY // minus infinity, score is being maximized
     for (f <- initialState.getFigures(maximizing)) {
       if(f != null)
         for (e <- initialState.findPossibleMoves(f)) {
           val score = alfabeta(DEPTH, initialState.makeMove(f, e), initialState.getOpponentColor(maximizing), -INFINITY, INFINITY)
           //println(f.x + " " + f.y + ": " + score)
           if(score > maxi){
             maxi = score
             move = e
             figure = f
           }
         }
    }
    //println("wynik: " + maxi)
    return (figure, move)
  }

  def alfabeta(depth : Int, internalState : InternalState, color: PlayerColor, alfa : Int, beta : Int) : Int = {
    if(depth == 0) {
      val s = evaluator.evaluateState(internalState)
      //println("score = " + s)
      return s
    }
    if(color == maximizing){
      var newAlfa = alfa
      for(figure <- internalState.getFigures(color)){
        if(figure != null)
          for(e <- internalState.findPossibleMoves(figure)){
            val score = alfabeta(depth - 1, internalState.makeMove(figure, e), internalState.getOpponentColor(color), newAlfa, beta)
            //println(figure.x + " " + figure.y + ": " + score)
            newAlfa = if(score > newAlfa) score else newAlfa
            if(newAlfa >= beta){
              //println("odciecie beta")
              return beta
            }
          }
      }
      return newAlfa
      }
    else{
      var newBeta = beta
      for(figure <- internalState.getFigures(color)){
        if(figure != null)
          for(e <- internalState.findPossibleMoves(figure)){
            val score = alfabeta(depth - 1, internalState.makeMove(figure, e), internalState.getOpponentColor(color), alfa, newBeta)
            //println(figure.x + " " + figure.y + ": " + score)
            newBeta = if(score < newBeta) score else newBeta
            if(newBeta <= alfa){
              //println("odciecie alfa")
              return alfa
            }
          }
      }
      return newBeta
    }
  }
}