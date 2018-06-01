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

  def min(height : Int, state : InternalState): Int = {
//    println("min - poziom " + height)
    if(height == 0)
      return evaluator.evaluateState(state)
    val values = getChildren(state).map(child => max(height - 1, child))
//    println("min: " + values.min)
    return values.min
  }

  def max(height : Int, state : InternalState): Int = {
//    println("max - poziom " + height)
    if(height == 0)
      return evaluator.evaluateState(state)
    val values = getChildren(state).map(child => min(height - 1, child))
//    println("max: " + values.max)
    return values.max
  }

  def minimax_algorithm(initialState : InternalState) : (Figure, (Int, Int)) = {
//    println("inicjalizacja minimaxa")
    val moves = getAllMoves(initialState)
    val x = moves.map(m => (m, max( depth - 1, initialState.makeMove(m))))
    val e = x.maxBy(_._2)
    return e._1
  }
}