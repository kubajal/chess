package controller

import model.{Figure, PlayerColor}
import model.PlayerColor.PlayerColor

import scala.annotation.tailrec

class Algorithm(val initialState : InternalState, val maximizing : PlayerColor, val depth : Int = 2) {

  val evaluator = new Evaluator(maximizing)
  val INFINITY = 1000000

  @tailrec
  private def minIteration(alpha : Int, beta : Int, height : Int, i : Int, states: Vector[InternalState]) : Int = {
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
  private def maxIteration(alpha : Int, beta : Int, height : Int, i : Int, states: Vector[InternalState]) : Int = {
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
    val newBeta = minIteration(alpha, beta, height, 0, state.getChildren())
//    println("min: " + newBeta)
    return newBeta
  }

  def max(alpha : Int, beta : Int, height : Int, state : InternalState): Int = {
//    println("max - poziom " + height)
    if(height == 0){
//      println("max: " + evaluator.evaluateState(state))
      return evaluator.evaluateState(state)
    }
    val newAlpha = maxIteration(alpha, beta, height, 0, state.getChildren())
//    println("max: " + newAlpha)
    return newAlpha
  }

  def run() : (Figure, (Int, Int)) = {
//    println("inicjalizacja minimaxa")
    val moves = initialState.getAllMoves()
    val x = moves.map(m => (m, min(-INFINITY, INFINITY, depth - 1, initialState.makeMove(m))))
    val e = x.maxBy(_._2)
    return e._1
  }
}