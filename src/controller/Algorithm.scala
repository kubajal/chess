package controller

import model.{Figure, PlayerColor}
import model.PlayerColor.PlayerColor

import scala.annotation.tailrec

class Algorithm(val initialState : InternalState, val maximizing : PlayerColor, val depth : Int = 2) {

  val evaluator = new Evaluator(maximizing)
  val INFINITY = 1000000

  def run(): (Figure, (Int, Int)) = {

    var move = (-1, -1)
    var figure : Figure = null
    var maxi = -INFINITY // minus infinity, score is being maximized
     for (f <- initialState.getFigures(maximizing)) {
       if(f != null)
         for (e <- initialState.findPossibleMoves(f)) {
           val score = alfabeta(depth, initialState.makeMove(f, e), initialState.getOpponentColor(maximizing), -INFINITY, INFINITY)
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

  def getAllMoves(state : InternalState) : Array[(Figure, (Int, Int))] = {

    state.getActiveFigures().map(f => state.getMoves(f)).reduce(_ ++ _)
  }

  def getChildren(state : InternalState) : Array[InternalState] = {

    val moves = state.getActiveFigures().map(f => state.getMoves(f)).reduce(_ ++ _)
    moves.map(m => state.makeMove(m))
  }

  @tailrec
  private def minIteration(alpha : Int, beta : Int, height : Int, i : Int, states: Array[InternalState]) : Int = {
    val newBeta = max(alpha, beta, height - 1, states(i))
    if(newBeta <= alpha)
      return alpha
    if(i == states.size - 1){

      if(newBeta < beta)
        return newBeta
      else
       return beta
    }
    return minIteration(alpha, if(newBeta < beta) newBeta else beta, height, i+1, states)
  }

  @tailrec
  private def maxIteration(alpha : Int, beta : Int, height : Int, i : Int, states: Array[InternalState]) : Int = {
    val newAlpha = min(alpha, beta, height - 1, states(i))
    if(newAlpha>= beta)
      return beta
    if(i == states.size - 1){
      if(newAlpha > alpha)
        return newAlpha
      else
        return alpha
    }
    return maxIteration(if(newAlpha > alpha) newAlpha else alpha, beta, height, i+1, states)
  }

  def min(alpha : Int, beta : Int, height : Int, state : InternalState): Int = {
//    println("min - poziom " + height)
    if(height == 0){

//      println("min: " + evaluator.evaluateState(state))
      return evaluator.evaluateState(state)
    }
    val newBeta = minIteration(alpha, beta, height, 0, getChildren(state))
//    println("min: " + newBeta)
    return newBeta
  }

  def max(alpha : Int, beta : Int, height : Int, state : InternalState): Int = {
//    println("max - poziom " + height)
    if(height == 0){
//      println("max: " + evaluator.evaluateState(state))
      return evaluator.evaluateState(state)
    }
    val newAlpha = maxIteration(alpha, beta, height, 0, getChildren(state))
//    println("max: " + newAlpha)
    return newAlpha
  }

  def minimax_algorithm(initialState : InternalState) : (Figure, (Int, Int)) = {
//    println("inicjalizacja minimaxa")
    val moves = getAllMoves(initialState)
    val x = moves.map(m => (m, min(-INFINITY, INFINITY, depth - 1, initialState.makeMove(m))))
    val e = x.maxBy(_._2)
    return e._1
  }
}