package controller

import model.{Figure, FigureType}

class Evaluator {

  val pawnTable : Array[Array[Int]] = Array(
    Array(0,  0,  0,  0,  0,  0,  0,  0),
    Array(50, 50, 50, 50, 50, 50, 50, 50),
    Array(10, 10, 20, 30, 30, 20, 10, 10),
    Array(5,  5, 10, 25, 25, 10,  5,  5),
    Array(0,  0,  0, 20, 20,  0,  0,  0),
    Array(5, -5,-10,  0,  0,-10, -5,  5),
    Array(5, 10, 10,-20,-20, 10, 10,  5),
    Array(0,  0,  0,  0,  0,  0,  0,  0)
  )
  val knightTable : Array[Array[Int]] = Array(
    Array(-50,-40,-30,-30,-30,-30,-40,-50),
    Array(-40,-20,  0,  0,  0,  0,-20,-40),
    Array(-30,  0, 10, 15, 15, 10,  0,-30),
    Array(-30,  5, 15, 20, 20, 15,  5,-30),
    Array(-30,  0, 15, 20, 20, 15,  0,-30),
    Array(-30,  5, 10, 15, 15, 10,  5,-30),
    Array(-40,-20,  0,  5,  5,  0,-20,-40),
    Array(-50,-40,-30,-30,-30,-30,-40,-50)
  )
  val bishopTable : Array[Array[Int]] = Array(
    Array(-20,-10,-10,-10,-10,-10,-10,-20),
    Array (-10,  0,  0,  0,  0,  0,  0,-10),
    Array(-10,  0,  5, 10, 10,  5,  0,-10),
    Array (-10,  5,  5, 10, 10,  5,  5,-10),
    Array(-10,  0, 10, 10, 10, 10,  0,-10),
    Array(-10, 10, 10, 10, 10, 10, 10,-10),
    Array (-10,  5,  0,  0,  0,  0,  5,-10),
    Array(-20,-10,-10,-10,-10,-10,-10,-20)
  )
  val rookTable : Array[Array[Int]] = Array(
    Array(0,  0,  0,  0,  0,  0,  0,  0),
    Array(5, 10, 10, 10, 10, 10, 10,  5),
    Array(-5,  0,  0,  0,  0,  0,  0, -5),
    Array(-5,  0,  0,  0,  0,  0,  0, -5),
    Array(-5,  0,  0,  0,  0,  0,  0, -5),
    Array(-5,  0,  0,  0,  0,  0,  0, -5),
    Array(-5,  0,  0,  0,  0,  0,  0, -5),
    Array(0,  0,  0,  5,  5,  0,  0,  0)
  )
  val queenTable : Array[Array[Int]] = Array(
    Array(-20,-10,-10, -5, -5,-10,-10,-20),
    Array(-10,  0,  0,  0,  0,  0,  0,-10),
    Array(-10,  0,  5,  5,  5,  5,  0,-10),
    Array(-5,  0,  5,  5,  5,  5,  0, -5),
    Array(0,  0,  5,  5,  5,  5,  0, -5),
    Array(-10,  5,  5,  5,  5,  5,  0,-10),
    Array(-10,  0,  5,  0,  0,  0,  0,-10),
    Array(-20,-10,-10, -5, -5,-10,-10,-20)
  )
  val kingTable : Array[Array[Int]] = Array(
    Array(20, 20,  0,  0,  0,  0, 20, 20),
    Array(20, 30, 10,  0,  0, 10, 30, 20),
    Array(-30,-40,-40,-50,-50,-40,-40,-30),
    Array(-30,-40,-40,-50,-50,-40,-40,-30),
    Array(-20,-30,-30,-40,-40,-30,-30,-20),
    Array(-10,-20,-20,-20,-20,-20,-20,-10),
    Array(20, 20,  0,  0,  0,  0, 20, 20),
    Array(20, 30, 10,  0,  0, 10, 30, 20)
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
