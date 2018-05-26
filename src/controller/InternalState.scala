package controller

import javax.swing.{ImageIcon, JLabel}
import model.Constants.NUMBER_OF_SQUARES
import model.FigureType.FigureType
import model.PlayerColor.PlayerColor
import model.{Figure, FigureType, PlayerColor}

import scala.annotation.tailrec
import scala.collection.immutable.VectorBuilder

case class InternalState(val whiteFigures : Array[Figure], val blackFigures : Array[Figure], val board: Array[Array[Figure]],
                         currentPlayerColor : PlayerColor, isCurrentPlayerKingAttacked : Boolean = false) {

  private val blackQueenImage = new ImageIcon(getClass.getResource("/images/black_queen.png"))
  private val whiteQueenImage = new ImageIcon(getClass.getResource("/images/white_queen.png"))

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

  def getOpponentColor(color : PlayerColor) : PlayerColor = {
    color match {
      case PlayerColor.White => PlayerColor.Black
      case PlayerColor.Black => PlayerColor.White
    }
  }

	def isKingAttacked(kingColor : PlayerColor) : Boolean = {
		val kingFigure = getFigures(kingColor)(15)
		isFieldAttacked((kingFigure.x, kingFigure.y))
	}

	def isAnyMovePossible(playerColor : PlayerColor) : Boolean = {
		val allPossibleMovesFields : Set[(Int, Int)] = getAllPossibleMovesFields(playerColor)
		allPossibleMovesFields.nonEmpty
	}
	
	def isAnyMoveDefendingKingPossible(playerColor : PlayerColor) : Boolean = {
		for (figure <- getFigures(playerColor))
      if(figure != null)
        for (move <- findPossibleMoves(figure)) {
          val afterMoveState = makeMove(figure, move)
					if(!afterMoveState.isKingAttacked(playerColor))
						true
				}
		false
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

		if(figure.getColor() == currentPlayerColor && figure.hasMoved == false && board(figure.x+1)(figure.y) == null && board(figure.x+2)(figure.y) == null &&
      board(figure.x+3)(figure.y) != null && board(figure.x+3)(figure.y).getType == FigureType.Rook && board(figure.x+3)(figure.y).hasMoved == false &&
			!areFieldsAttacked(Vector((figure.x, figure.y), (figure.x+1, figure.y), (figure.x+2, figure.y), (figure.x+3, figure.y))))
			possibleMoves += ((figure.x+2, figure.y))

		if(figure.getColor() == currentPlayerColor && figure.hasMoved == false && board(figure.x-1)(figure.y) == null && board(figure.x-2)(figure.y) == null && board(figure.x-3)(figure.y) == null &&
			board(figure.x-4)(figure.y) != null && board(figure.x-4)(figure.y).getType == FigureType.Rook && board(figure.x-4)(figure.y).hasMoved == false &&
			!areFieldsAttacked(Vector((figure.x, figure.y), (figure.x-1, figure.y), (figure.x-2, figure.y), (figure.x-3, figure.y), (figure.x-4, figure.y))))
			possibleMoves += ((figure.x-2, figure.y))

    if(figure.getColor() == currentPlayerColor)
      removeAttackedFields(possibleMoves.result())
    else
      possibleMoves.result()
  }

	def removeAttackedFields(possibleMoves : Vector[(Int, Int)]) : Vector[(Int, Int)] = {
		val allOpponentPossibleMovesFields : Set[(Int, Int)] = getAllPossibleMovesFields(getOpponentColor(currentPlayerColor))
		possibleMoves.filterNot(move => allOpponentPossibleMovesFields.contains(move))
	}

	def isFieldAttacked(fieldPosition: (Int, Int)) : Boolean = {
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

    val newBlackFigures = blackFigures.map(f => if(f == null) null else f.copy())
    val newWhiteFigures = whiteFigures.map(f => if(f == null) null else f.copy())
    val newBoard = createBoard(newWhiteFigures, newBlackFigures)
    val newFigure = newBoard(figure.x)(figure.y)

    newBoard(figure.x)(figure.y) = null
		newFigure.setPoint(destination._1, destination._2)
    newBoard(destination._1)(destination._2) = newFigure
		newFigure.setMoved(true)

    if(newFigure.getColor == PlayerColor.White)
    {
      for(i <- 0 to 15)
        if(newWhiteFigures(i) != null && newWhiteFigures(i).x == figure.x && newWhiteFigures(i).y == figure.y)
          newWhiteFigures(i) = newFigure
      for(i <- 0 to 15)
        if(newBlackFigures(i) != null && newBlackFigures(i).x == destination._1 && newBlackFigures(i).y == destination._2)
          newBlackFigures(i) = null
    }
    else
    {
      for(i <- 0 to 15)
        if(newBlackFigures(i) != null && newBlackFigures(i).x == figure.x && newBlackFigures(i).y == figure.y)
          newBlackFigures(i) = newFigure
      for(i <- 0 to 15)
        if(newWhiteFigures(i) != null && newWhiteFigures(i).x == destination._1 && newWhiteFigures(i).y == destination._2)
          newWhiteFigures(i) = null
    }

		if(newFigure.getType == FigureType.Pawn && currentPlayerColor == PlayerColor.Black && newFigure.y == 0)
		{
			newFigure.setFigureType(FigureType.Queen)
			newFigure.setFigureImage(new JLabel(blackQueenImage))
		} 
		else if(newFigure.getType == FigureType.Pawn && currentPlayerColor == PlayerColor.White && newFigure.y == 7)
		{
			newFigure.setFigureType(FigureType.Queen)
			newFigure.setFigureImage(new JLabel(whiteQueenImage))
		}

		if(figure.getType == FigureType.King && destination == (figure.x-2, figure.y))
		{
			newBoard(figure.x-4)(figure.y).setMoved(true)
			newBoard(figure.x-4)(figure.y).setPoint(figure.x-1, figure.y)
		  newBoard(figure.x-1)(figure.y) = newBoard(figure.x-4)(figure.y)
			newBoard(figure.x-4)(figure.y) = null
		}
		if(figure.getType == FigureType.King && destination == (figure.x+2, figure.y))
		{
			newBoard(figure.x+3)(figure.y).setMoved(true)
			newBoard(figure.x+3)(figure.y).setPoint(figure.x+1, figure.y)
		  newBoard(figure.x+1)(figure.y) = newBoard(figure.x+3)(figure.y)
			newBoard(figure.x+3)(figure.y) = null
		}

		new InternalState(newWhiteFigures, newBlackFigures, newBoard,
      getOpponentColor(currentPlayerColor), isKingAttacked(getOpponentColor(currentPlayerColor)))
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
    return board
  }
}
