package controller

import javax.swing.{ImageIcon, JLabel}
import model.Constants.NUMBER_OF_SQUARES
import model.FigureType.FigureType
import model.PlayerColor.PlayerColor
import model.{Figure, FigureType, Images, PlayerColor}

import scala.annotation.tailrec
import scala.collection.immutable.VectorBuilder

case class InternalState(val whiteFigures : Array[Figure], val blackFigures : Array[Figure], val board: Array[Array[Figure]],
                         currentPlayerColor : PlayerColor, var isCurrentPlayerKingAttacked : Boolean = false) extends Images {

  def getActiveFigures()  : Array[Figure] = return getFigures(currentPlayerColor)

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

  def getMoves(figure : Figure) : Array[(Figure, (Int, Int))] = {
    findPossibleMoves(figure).map(e => (figure, e)).toArray
  }
  def setCurrentPlayerKingAttacked(isCurrentPlayerKingAttacked : Boolean) = this.isCurrentPlayerKingAttacked = isCurrentPlayerKingAttacked

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

  def getOpponentColor(color : PlayerColor) : PlayerColor = {
    color match {
      case PlayerColor.White => PlayerColor.Black
      case PlayerColor.Black => PlayerColor.White
    }
  }

	def isKingAttacked(kingColor : PlayerColor) : Boolean = {
		val kingFigure = getFigures(kingColor)(15)
		isFieldAttacked(kingColor, (kingFigure.x, kingFigure.y))
	}

	def isAnyMovePossible(playerColor : PlayerColor) : Boolean = {
		val allPossibleMovesFields : Set[(Int, Int)] = getAllPossibleMovesFields(playerColor)
		allPossibleMovesFields.nonEmpty
	}

	def isMoveDefendingKing(figure : Figure, move : (Int, Int)) : Boolean = {
		val afterMoveState = makeMove(figure, move)
    !afterMoveState.isKingAttacked(figure.getColor)
	}	

	def isAnyMoveDefendingKingPossible(playerColor : PlayerColor) : Boolean = {
		for (figure <- getFigures(playerColor))
      if(figure != null)
        for (move <- findPossibleMoves(figure))
          if(isMoveDefendingKing(figure, move))
						return true
		return false
	}

	def getAllPossibleMovesFields(color : PlayerColor) : Set[(Int, Int)] = {

    @tailrec
    def recursion(i : Int, playerFigures : Array[Figure], allPossibleMovesFields : Set[(Int, Int)]) : Set[(Int, Int)] = {
      if(i < playerFigures.size)
        if(playerFigures(i) != null)
          recursion(i+1, playerFigures, allPossibleMovesFields ++ findPossibleMoves(playerFigures(i)))
        else
          recursion(i+1, playerFigures, allPossibleMovesFields)
      else
        allPossibleMovesFields
    }

    recursion(0, getFigures(color), Set.empty[(Int, Int)])
  }

  def findPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
		val possibleMoves =
    figure.getType() match {
      case FigureType.Bishop => findBishopPossibleMoves(figure)
      case FigureType.Knight => findKnightPossibleMoves(figure)
      case FigureType.Pawn => findPawnPossibleMoves(figure)
      case FigureType.Queen => findQueenPossibleMoves(figure)
      case FigureType.Rook => findRookPossibleMoves(figure)
      case FigureType.King => findKingPossibleMoves(figure)
    }

    if(isCurrentPlayerKingAttacked)
      possibleMoves.filter(move => isMoveDefendingKing(figure, move))
    else
      possibleMoves
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

  def findKnightPossibleMoves(f : Figure) : Vector[(Int, Int)] = {
    val moves = (for(x <- Array[Int](f.x - 1, f.x + 1); y <- Array[Int](f.y - 2, f.y + 2)) yield (x, y)).++(for(x <- Array[Int](f.x - 2, f.x + 2); y <- Array[Int](f.y - 1, f.y + 1)) yield (x, y))
    return moves.filter(e => 8 > e._1 && e._1 >= 0 && e._2 < 8 && e._2 >= 0 && (getFigure(e) == null || getFigure(e).getColor() == getOpponentColor(f.getColor()))).toVector
  }

  def getFigure(e : (Int, Int)) : Figure = board(e._1)(e._2)
  def getFigure(x : Int, y : Int) : Figure = board(x)(y)

  def findKingPossibleMoves(f : Figure) : Vector[(Int, Int)] = {
    val moves = for(x <- Array[Int](f.x - 1, f.x, f.x + 1); y <- Array[Int](f.y-1, f.y, f.y + 1)) yield (x, y)
    return moves.filter(e => 8 > e._1 && e._1 >= 0 && e._2 < 8 && e._2 >= 0 && (getFigure(e) == null || getFigure(e).getColor() == getOpponentColor(f.getColor()))).toVector
  }

	def removeAttackedFields(possibleMoves : Vector[(Int, Int)]) : Vector[(Int, Int)] = {
		val allOpponentPossibleMovesFields : Set[(Int, Int)] = getAllPossibleMovesFields(getOpponentColor(currentPlayerColor))
		possibleMoves.filterNot(move => allOpponentPossibleMovesFields.contains(move) || isFieldAttackedByEnemyPawn(move))
	}

  def isFieldAttackedByEnemyPawn(field : (Int, Int)) : Boolean = {
    currentPlayerColor match {
      case PlayerColor.Black =>
        (field._1 > 0 && field._2 > 0 && board(field._1-1)(field._2-1) != null && board(field._1-1)(field._2-1).getType() == FigureType.Pawn && board(field._1-1)(field._2-1).getColor == getOpponentColor(currentPlayerColor)) ||
        (field._1 < 7 && field._2 > 0 && board(field._1+1)(field._2-1) != null && board(field._1+1)(field._2-1).getType() == FigureType.Pawn && board(field._1+1)(field._2-1).getColor == getOpponentColor(currentPlayerColor))
      case PlayerColor.White =>
        (field._1 > 0 && field._2 < 7 && board(field._1-1)(field._2+1) != null && board(field._1-1)(field._2+1).getType() == FigureType.Pawn && board(field._1-1)(field._2+1).getColor == getOpponentColor(currentPlayerColor)) ||
        (field._1 < 7 && field._2 < 7 && board(field._1+1)(field._2+1) != null && board(field._1+1)(field._2+1).getType() == FigureType.Pawn && board(field._1+1)(field._2+1).getColor == getOpponentColor(currentPlayerColor))
    }
  }

	def isFieldAttacked(currentPlayerColor : PlayerColor, fieldPosition: (Int, Int)) : Boolean = {
		val allOpponentPossibleMovesFields : Set[(Int, Int)] = getAllPossibleMovesFields(getOpponentColor(currentPlayerColor))
		allOpponentPossibleMovesFields.contains(fieldPosition)
	}

	def areFieldsAttacked(fieldPositions : Vector[(Int, Int)]) : Boolean = {
		val allOpponentPossibleMovesFields : Set[(Int, Int)] = getAllPossibleMovesFields(getOpponentColor(currentPlayerColor))
		fieldPositions.exists(fieldPosition => allOpponentPossibleMovesFields.contains(fieldPosition))
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
        newBoard,
        getOpponentColor(figure.getColor()))

    }
    else{
      val newBlackFigures = blackFigures.filterNot(f => f.x == figure.x && f.y == figure.y) :+ newFigure
      return new InternalState(
        if(board(destination._1)(destination._2) != null && board(destination._1)(destination._2).getColor() != figure.getColor())
          getWhiteFigures.filterNot(f => f.x == destination._1 && f.y == destination._2)
        else
          getWhiteFigures,
        newBlackFigures,
        newBoard,
        getOpponentColor(figure.getColor()))
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

  def createBoard(whiteFigures : Array[Figure], blackFigures : Array[Figure]): Array[Array[Figure]] = {

    val board = Array.ofDim[Figure](8, 8)
    for (i <- 0 to NUMBER_OF_SQUARES - 1)
      for (j <- 0 to NUMBER_OF_SQUARES - 1)
        board(i)(j) = null

    for (figure <- whiteFigures) {
      if(figure != null)
        board(figure.x)(figure.y) = figure
    }

    for (figure <- blackFigures) {
      if(figure != null)
        board(figure.x)(figure.y) = figure
    }
    board
  }
}
