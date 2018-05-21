package controller

import javax.swing.JLabel
import model.FigureType.FigureType
import model.PlayerColor.PlayerColor
import model.{Figure, FigureType, PlayerColor}

import scala.annotation.tailrec
import scala.collection.immutable.VectorBuilder

case class InternalState(val whiteFigures : Array[Figure], val blackFigures : Array[Figure], val board: Array[Array[Figure]]) {

  def points(figure : Figure) : Int = {
    figure.getType match {
      case FigureType.Pawn => 1
      case FigureType.Knight => 3
      case FigureType.Bishop => 3
      case FigureType.Rook => 5
      case FigureType.Queen => 9
      case FigureType.King => 1000
    }
  }
  def whiteHeuristic() : Int = whiteFigures.map(f => points(f)).sum
  def blackHeuristic() : Int = blackFigures.map(f => points(f)).sum
  def heuristic(color : PlayerColor) : Int = {
    color match {
      case PlayerColor.White => whiteHeuristic
      case PlayerColor.Black => blackHeuristic
    }
  }

  def getWhiteFigures() : Array[Figure] = whiteFigures
  def getBlackFigures() : Array[Figure] = blackFigures
  def getFigures(color : PlayerColor) : Array[Figure] = {
    color match {
      case PlayerColor.White => getWhiteFigures
      case PlayerColor.Black => getBlackFigures
    }
  }

  def getBoard : Array[Array[Figure]] = board

  def getOpponentColor(color : PlayerColor ) : PlayerColor = {
    color match {
      case PlayerColor.White => PlayerColor.Black
      case PlayerColor.Black => PlayerColor.White
    }
  }

  def findPossibleMoves(figure : Figure) : Vector[(Int, Int)] =
    figure.getType() match {
      case FigureType.Bishop => findBishopPossibleMoves(figure)
      case FigureType.Knight => findKnightPossibleMoves(figure)
      case FigureType.Pawn => findPawnPossibleMoves(figure)
      case FigureType.Queen => findQueenPossibleMoves(figure)
      case FigureType.Rook => findRookPossibleMoves(figure)
      case FigureType.King => findKingPossibleMoves(figure)
    }

  def findPawnPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
    val possibleMoves = new VectorBuilder[(Int, Int)]

    figure.getColor match {
      case PlayerColor.Black => {
        if(figure.y > 0 && board(figure.x)(figure.y-1) == null)
          possibleMoves += ((figure.x, figure.y-1))
        if(figure.y == 6 && board(figure.x)(5) == null && board(figure.x)(4) == null)
          possibleMoves += ((figure.x, 4))
        if(figure.x > 0 && figure.y > 0 && board(figure.x-1)(figure.y-1) != null && board(figure.x-1)(figure.y-1).getColor == getOpponentColor(figure.getColor))
          possibleMoves += ((figure.x-1, figure.y-1))
        if(figure.x < 7 && figure.y > 0 && board(figure.x+1)(figure.y-1) != null && board(figure.x+1)(figure.y-1).getColor == getOpponentColor(figure.getColor))
          possibleMoves += ((figure.x+1, figure.y-1))
      }
      case PlayerColor.White => {
        if(figure.y < 7 && board(figure.x)(figure.y+1) == null)
          possibleMoves += ((figure.x, figure.y+1))
        if(figure.y == 1 && board(figure.x)(2) == null && board(figure.x)(3) == null)
          possibleMoves += ((figure.x, 3))
        if(figure.x > 0 && figure.y < 7 && board(figure.x-1)(figure.y+1) != null && board(figure.x-1)(figure.y+1).getColor == getOpponentColor(figure.getColor))
          possibleMoves += ((figure.x-1, figure.y+1))
        if(figure.x < 7 && figure.y < 7 && board(figure.x+1)(figure.y+1) != null && board(figure.x+1)(figure.y+1).getColor == getOpponentColor(figure.getColor))
          possibleMoves += ((figure.x+1, figure.y+1))
      }
    }
    possibleMoves.result()
  }

  def findKnightPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
    val possibleMoves = new VectorBuilder[(Int, Int)]

    if(figure.x < 7 && figure.y < 6)
      if(board(figure.x+1)(figure.y+2) == null || board(figure.x+1)(figure.y+2).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x+1, figure.y+2))

    if(figure.x < 6 && figure.y < 7)
      if(board(figure.x+2)(figure.y+1) == null || board(figure.x+2)(figure.y+1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x+2, figure.y+1))

    if(figure.x < 6 && figure.y > 0)
      if(board(figure.x+2)(figure.y-1) == null || board(figure.x+2)(figure.y-1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x+2, figure.y-1))

    if(figure.x < 7 && figure.y > 1)
      if(board(figure.x+1)(figure.y-2) == null || board(figure.x+1)(figure.y-2).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x+1, figure.y-2))

    if(figure.x > 0 && figure.y > 1)
      if(board(figure.x-1)(figure.y-2) == null || board(figure.x-1)(figure.y-2).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x-1, figure.y-2))

    if(figure.x > 1 && figure.y > 0)
      if(board(figure.x-2)(figure.y-1) == null || board(figure.x-2)(figure.y-1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x-2, figure.y-1))

    if(figure.x > 1 && figure.y < 7)
      if(board(figure.x-2)(figure.y+1) == null || board(figure.x-2)(figure.y+1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x-2, figure.y+1))

    if(figure.x > 0 && figure.y < 6)
      if(board(figure.x-1)(figure.y+2) == null || board(figure.x-1)(figure.y+2).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x-1, figure.y+2))

    possibleMoves.result()
  }

  def findKingPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
    val possibleMoves = new VectorBuilder[(Int, Int)]

    if(figure.y < 7)
      if(board(figure.x)(figure.y+1) == null || board(figure.x)(figure.y+1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x, figure.y+1))

    if(figure.x < 7 && figure.y < 7)
      if(board(figure.x+1)(figure.y+1) == null || board(figure.x+1)(figure.y+1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x+1, figure.y+1))

    if(figure.x < 7)
      if(board(figure.x+1)(figure.y) == null || board(figure.x+1)(figure.y).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x+1, figure.y))

    if(figure.x < 7 && figure.y > 0)
      if(board(figure.x+1)(figure.y-1) == null || board(figure.x+1)(figure.y-1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x+1, figure.y-1))

    if(figure.y > 0)
      if(board(figure.x)(figure.y-1) == null || board(figure.x)(figure.y-1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x, figure.y-1))

    if(figure.x > 0 && figure.y > 0)
      if(board(figure.x-1)(figure.y-1) == null || board(figure.x-1)(figure.y-1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x-1, figure.y-1))

    if(figure.x > 0)
      if(board(figure.x-1)(figure.y) == null || board(figure.x-1)(figure.y).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x-1, figure.y))

    if(figure.x > 0 && figure.y < 7)
      if(board(figure.x-1)(figure.y+1) == null || board(figure.x-1)(figure.y+1).getColor == getOpponentColor(figure.getColor))
        possibleMoves += ((figure.x-1, figure.y+1))

    possibleMoves.result()
  }

  def makeMove(x : (Figure, (Int, Int))) : InternalState = {
    makeMove(x._1, x._2)
  }



  def makeMove(figure : Figure, destination: (Int, Int)) : InternalState = {

    val newFigure = new Figure(figure.getType, figure.getColor, destination._1, destination._2, figure.getFigureImage)
    val newBoard = board.map(_.clone)
    newBoard(figure.x)(figure.y) = null
    newBoard(destination._1)(destination._2) = newFigure
    if(figure.getColor == PlayerColor.White){
      val newWhiteFigures = whiteFigures.filterNot(f => f.x == figure.x && f.y == figure.y) :+ newFigure
      return new InternalState(newWhiteFigures,
        if(board(destination._1)(destination._2) != null && board(destination._1)(destination._2).getColor() != figure.getColor())
          getBlackFigures.filterNot(f => f.x == destination._1 && f.y == destination._2)
        else
          getBlackFigures,
        newBoard)
    }
    else{
      val newBlackFigures = blackFigures.filterNot(f => f.x == figure.x && f.y == figure.y) :+ newFigure
      return new InternalState(
        if(board(destination._1)(destination._2) != null && board(destination._1)(destination._2).getColor() != figure.getColor())
          getWhiteFigures.filterNot(f => f.x == destination._1 && f.y == destination._2)
        else
          getWhiteFigures,
        newBlackFigures,
        newBoard)
    }
  }

  // function which tries to add consistently all possible moves in direction chosen by h - horizontal factor and v - vertical factor, v and h can be only 3 values (-1, 0, 1)
  def addAllPossibleMovesInDirection(figure : Figure, h : Int, v : Int) : Vector[(Int, Int)] = {

    @tailrec
    def recursion(i : Int, vector : Vector[(Int, Int)]) : Vector[(Int, Int)] = {
      val x = figure.x + h * i
      val y = figure.y + v * i

      if(0 <= x && x <= 7 && 0 <= y && y <= 7)
      {
        if(board(x)(y) == null)
          recursion(i+1, vector :+ (x, y))
        else if(board(x)(y).getColor == getOpponentColor(figure.getColor))
          vector :+ (x, y)
        else if(board(x)(y).getColor == figure.getColor)
          vector
        else
          vector
      }
      else
        vector
    }

    recursion(1, Vector.empty[(Int, Int)])
  }

  def findBishopPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
    val possibleMoves = new VectorBuilder[(Int, Int)]

    possibleMoves ++= addAllPossibleMovesInDirection(figure, 1, 1); // up right
    possibleMoves ++= addAllPossibleMovesInDirection(figure, 1, -1); // down right
    possibleMoves ++= addAllPossibleMovesInDirection(figure, -1, -1); // down left
    possibleMoves ++= addAllPossibleMovesInDirection(figure, -1, 1); // up left

    possibleMoves.result()
  }

  def findRookPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
    val possibleMoves = new VectorBuilder[(Int, Int)]

    possibleMoves ++= addAllPossibleMovesInDirection(figure, 0, 1); // up
    possibleMoves ++= addAllPossibleMovesInDirection(figure, 1, 0); // right
    possibleMoves ++= addAllPossibleMovesInDirection(figure, 0, -1); // down
    possibleMoves ++= addAllPossibleMovesInDirection(figure, -1, 0); // left

    possibleMoves.result()
  }

  def findQueenPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
    val possibleMoves = new VectorBuilder[(Int, Int)]

    possibleMoves ++= addAllPossibleMovesInDirection(figure, 0, 1); // up
    possibleMoves ++= addAllPossibleMovesInDirection(figure, 1, 1); // up right
    possibleMoves ++= addAllPossibleMovesInDirection(figure, 1, 0); // right
    possibleMoves ++= addAllPossibleMovesInDirection(figure, 1, -1); // down right
    possibleMoves ++= addAllPossibleMovesInDirection(figure, 0, -1); // down
    possibleMoves ++= addAllPossibleMovesInDirection(figure, -1, -1); // down left
    possibleMoves ++= addAllPossibleMovesInDirection(figure, -1, 0); // left
    possibleMoves ++= addAllPossibleMovesInDirection(figure, -1, 1); // up left

    possibleMoves.result()
  }

}