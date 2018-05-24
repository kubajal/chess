package controller

import model.{Figure, FigureType}

class Evaluator {

  val pawnTable : Array[Array[Int]] = Array(
    Array(0, 0, 0, 0, 0, 0, 0, 0),
    Array(1, 2, 2, 2, 2, 2, 2, 1),
    Array(1, 2, 2, 2, 2, 2, 2, 1),
    Array(1, 2, 2, 2, 2, 2, 2, 1),
    Array(1, 2, 2, 2, 2, 2, 2, 1),
    Array(1, 2, 2, 2, 2, 2, 2, 1),
    Array(1, 2, 2, 2, 2, 2, 2, 1),
    Array(0, 0, 0, 0, 0, 0, 0, 0)
  )
  val knightTable : Array[Array[Int]] = Array(
    Array(3, 3, 5, 5, 5, 5, 3, 3),
    Array(3, 5, 6, 6, 6, 6, 5, 3),
    Array(5, 6, 8, 8, 8, 8, 5, 5),
    Array(5, 6, 8, 9, 9, 8, 6, 5),
    Array(5, 6, 8, 9, 9, 8, 6, 5),
    Array(5, 6, 8, 8, 8, 8, 5, 5),
    Array(3, 5, 6, 6, 6, 6, 5, 3),
    Array(3, 3, 5, 5, 5, 5, 3, 3)
  )
  val bishopTable : Array[Array[Int]] = Array(
    Array(5, 5, 5, 5, 5, 5, 5, 5),
    Array(5, 6, 6, 6, 6, 6, 6, 5),
    Array(5, 6, 6, 7, 7, 6, 6, 5),
    Array(5, 6, 7, 9, 9, 7, 6, 5),
    Array(5, 6, 7, 9, 9, 7, 6, 5),
    Array(5, 6, 6, 7, 7, 6, 6, 5),
    Array(5, 6, 6, 6, 6, 6, 6, 5),
    Array(5, 5, 5, 5, 5, 5, 5, 5)
  )
  val rookTable : Array[Array[Int]] = Array(
    Array(9, 9, 9, 9, 9, 9, 9, 9),
    Array(9,10,10,10,10,10,10, 9),
    Array(9,10,10,10,10,10,10, 9),
    Array(9,10,10,12,12,10,10, 9),
    Array(9,10,10,12,12,10,10, 9),
    Array(9,10,10,10,10,10,10, 9),
    Array(9,10,10,10,10,10,10, 9),
    Array(9, 9, 9, 9, 9, 9, 9, 9)
  )
  val queenTable : Array[Array[Int]] = Array(
    Array(12, 12, 12, 12, 12, 12, 12, 12),
    Array(12, 15, 16, 16, 16, 16, 15, 12),
    Array(12, 15, 18, 20, 20, 18, 15, 12),
    Array(12, 15, 20, 20, 20, 20, 15, 12),
    Array(12, 15, 20, 20, 20, 20, 15, 12),
    Array(12, 15, 18, 20, 20, 18, 15, 12),
    Array(12, 15, 16, 16, 16, 16, 15, 12),
    Array(12, 12, 12, 12, 12, 12, 12, 12),
  )
  val kingTable : Array[Array[Int]] = Array(
    Array(12, 12, 12, 12, 12, 12, 12, 12),
    Array(5, 5, 5, 5, 5, 5, 5, 5),
    Array(0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 0, 0),
    Array(5, 5, 5, 5, 5, 5, 5, 5),
    Array(12, 12, 12, 12, 12, 12, 12, 12)
  )

  def evaluate(figure : Figure) : Int = {
    figure.figureType match {
      case FigureType.King => return kingTable(figure.x)(figure.y)
      case FigureType.Pawn => return pawnTable(figure.x)(figure.y)
      case FigureType.Bishop => return bishopTable(figure.x)(figure.y)
      case FigureType.Knight => return knightTable(figure.x)(figure.y)
      case FigureType.Rook => return rookTable(figure.x)(figure.y)
      case FigureType.Queen => return  queenTable(figure.x)(figure.y)
    }
  }

  def evaluateState(internalState : InternalState) : Int = {
    return internalState.blackFigures.map(f => evaluate(f)).sum - internalState.whiteFigures.map(f => evaluate(f)).sum
  }
}
