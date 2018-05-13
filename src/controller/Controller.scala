package controller

import java.awt.Point

import javax.swing.{ImageIcon, JLabel}
import model.{Figure, FigureType, PlayerColor}
import model.PlayerColor.PlayerColor
import view.{BoardPanel, MainWindow}
import model.Constants._

import scala.annotation.tailrec
import scala.collection.JavaConverters
import scala.collection.immutable.VectorBuilder
import scala.concurrent.JavaConversions

class Controller(var blackFigures: Array[Figure] = Array.ofDim[Figure](16), var whiteFigures: Array[Figure] = Array.ofDim[Figure](16),
                      var mainWindow: MainWindow = null, var timeForMove: Long = 100, var playerColor: PlayerColor = PlayerColor.White,
                      var currentPlayerColor: PlayerColor = PlayerColor.White, board: Array[Array[Figure]] = Array.ofDim[Figure](8, 8),
                      inGame: Boolean = true, gameOver: Boolean = false) {

  private val whitePawnImage = new ImageIcon(getClass.getResource("/images/white_pawn.png"))
  private val whiteKnightImage = new ImageIcon(getClass.getResource("/images/white_knight.png"))
  private val whiteBishopImage = new ImageIcon(getClass.getResource("/images/white_bishop.png"))
  private val whiteRookImage = new ImageIcon(getClass.getResource("/images/white_rook.png"))
  private val whiteQueenImage = new ImageIcon(getClass.getResource("/images/white_queen.png"))
  private val whiteKingImage = new ImageIcon(getClass.getResource("/images/white_king.png"))
  private val blackPawnImage = new ImageIcon(getClass.getResource("/images/black_pawn.png"))
  private val blackKnightImage = new ImageIcon(getClass.getResource("/images/black_knight.png"))
  private val blackBishopImage = new ImageIcon(getClass.getResource("/images/black_bishop.png"))
  private val blackRookImage = new ImageIcon(getClass.getResource("/images/black_rook.png"))
  private val blackQueenImage = new ImageIcon(getClass.getResource("/images/black_queen.png"))
  private val blackKingImage = new ImageIcon(getClass.getResource("/images/black_king.png"))

  createFigures()
  createBoard()

  def createFigures(): Unit = {

    for (i <- 0 to NUMBER_OF_SQUARES - 1)
      whiteFigures(i) = new Figure(FigureType.Pawn, PlayerColor.White, i, 1, new JLabel(whitePawnImage))

    for (i <- 0 to NUMBER_OF_SQUARES - 1)
      blackFigures(i) = new Figure(FigureType.Pawn, PlayerColor.Black, i, 6, new JLabel(blackPawnImage))

    whiteFigures(8) = new Figure(FigureType.Rook, PlayerColor.White, 0, 0, new JLabel(whiteRookImage))
    whiteFigures(9) = new Figure(FigureType.Rook, PlayerColor.White, 7, 0, new JLabel(whiteRookImage))
    blackFigures(8) = new Figure(FigureType.Rook, PlayerColor.Black, 0, 7, new JLabel(blackRookImage))
    blackFigures(9) = new Figure(FigureType.Rook, PlayerColor.Black, 7, 7, new JLabel(blackRookImage))
    whiteFigures(10) = new Figure(FigureType.Knight, PlayerColor.White, 1, 0, new JLabel(whiteKnightImage))
    whiteFigures(11) = new Figure(FigureType.Knight, PlayerColor.White, 6, 0, new JLabel(whiteKnightImage))
    blackFigures(10) = new Figure(FigureType.Knight, PlayerColor.Black, 1, 7, new JLabel(blackKnightImage))
    blackFigures(11) = new Figure(FigureType.Knight, PlayerColor.Black, 6, 7, new JLabel(blackKnightImage))
    whiteFigures(12) = new Figure(FigureType.Bishop, PlayerColor.White, 2, 0, new JLabel(whiteBishopImage))
    whiteFigures(13) = new Figure(FigureType.Bishop, PlayerColor.White, 5, 0, new JLabel(whiteBishopImage))
    blackFigures(12) = new Figure(FigureType.Bishop, PlayerColor.Black, 2, 7, new JLabel(blackBishopImage))
    blackFigures(13) = new Figure(FigureType.Bishop, PlayerColor.Black, 5, 7, new JLabel(blackBishopImage))
    whiteFigures(14) = new Figure(FigureType.Queen, PlayerColor.White, 3, 0, new JLabel(whiteQueenImage))
    whiteFigures(15) = new Figure(FigureType.King, PlayerColor.White, 4, 0, new JLabel(whiteKingImage))
    blackFigures(14) = new Figure(FigureType.Queen, PlayerColor.Black, 3, 7, new JLabel(blackQueenImage))
    blackFigures(15) = new Figure(FigureType.King, PlayerColor.Black, 4, 7, new JLabel(blackKingImage))
  }

  def createBoard(): Unit = {

    for (i <- 0 to NUMBER_OF_SQUARES - 1)
      for (j <- 0 to NUMBER_OF_SQUARES - 1)
        board(i)(j) = null

    for (figure <- whiteFigures) {
      board(figure.x)(figure.y) = figure
    }

    for (figure <- blackFigures) {
      board(figure.x)(figure.y) = figure
    }
  }

  /*def whitePawnPossibleMoves(figure: Figure): Vector[(Int, Int)] = {
    var possibleMoves = Vector[(Int, Int)]((figure.x, figure.y + 1), (figure.x - 1, figure.y + 1), (figure.x + 1, figure.y + 1))
    var move = List[(Int, Int)]()
    if(getFigure(figure.x, figure.y + 1) == null)
      move = move :+ (figure.x, figure.y + 1)
    move = move ::: List[(Int, Int)]((figure.x - 1, figure.y + 1), (figure.x + 1, figure.y + 1)).filter(f => getFigure(f._1, f._2) != null && getFigure(f._1, f._2).getColor != figure.getColor)
    possibleMoves = possibleMoves.filter(x => x._1 >= 0 && x._1 < 8 && x._2 >= 0 && x._1 < 8)
    possibleMoves.filter(x => getFigure(x) == null || getFigure(x).getColor != figure.getColor)
    return move.toVector

  }*/

  def getOpponentColor(playerColor: PlayerColor): PlayerColor = {
    playerColor match {
      case PlayerColor.Black => PlayerColor.White
      case PlayerColor.White => PlayerColor.Black
    }
  }

  def findPawnPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
    val possibleMoves = new VectorBuilder[(Int, Int)]

    figure.getColor match {
      case PlayerColor.Black => {
        if(figure.y > 0 && board(figure.x)(figure.y-1) == null)
          possibleMoves += ((figure.x, figure.y-1))
        if(figure.y == 6 && board(figure.x)(5) == null && board(figure.x)(4) == null)
          possibleMoves += ((figure.x, 4))
        if(figure.x > 0 && figure.y > 0 && board(figure.x-1)(figure.y-1) != null && board(figure.x-1)(figure.y-1).getColor == getOpponentColor(currentPlayerColor))
          possibleMoves += ((figure.x-1, figure.y-1))
        if(figure.x < 7 && figure.y > 0 && board(figure.x+1)(figure.y-1) != null && board(figure.x+1)(figure.y-1).getColor == getOpponentColor(currentPlayerColor))
          possibleMoves += ((figure.x+1, figure.y-1))
      }
      case PlayerColor.White => {
        if(figure.y < 7 && board(figure.x)(figure.y+1) == null)
          possibleMoves += ((figure.x, figure.y+1))
        if(figure.y == 1 && board(figure.x)(2) == null && board(figure.x)(3) == null)
          possibleMoves += ((figure.x, 3))
        if(figure.x > 0 && figure.y < 7 && board(figure.x-1)(figure.y+1) != null && board(figure.x-1)(figure.y+1).getColor == getOpponentColor(currentPlayerColor))
          possibleMoves += ((figure.x-1, figure.y+1))
        if(figure.x < 7 && figure.y < 7 && board(figure.x+1)(figure.y+1) != null && board(figure.x+1)(figure.y+1).getColor == getOpponentColor(currentPlayerColor))
          possibleMoves += ((figure.x+1, figure.y+1))
      }
    }
    possibleMoves.result()
  }

  def findKnightPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
    val possibleMoves = new VectorBuilder[(Int, Int)]
    
		if(figure.x < 7 && figure.y < 6)
			if(board(figure.x+1)(figure.y+2) == null || board(figure.x+1)(figure.y+2).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x+1, figure.y+2))
			
		if(figure.x < 6 && figure.y < 7)
			if(board(figure.x+2)(figure.y+1) == null || board(figure.x+2)(figure.y+1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x+2, figure.y+1))

		if(figure.x < 6 && figure.y > 0)
			if(board(figure.x+2)(figure.y-1) == null || board(figure.x+2)(figure.y-1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x+2, figure.y-1))

		if(figure.x < 7 && figure.y > 1)
			if(board(figure.x+1)(figure.y-2) == null || board(figure.x+1)(figure.y-2).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x+1, figure.y-2))

		if(figure.x > 0 && figure.y > 1)
			if(board(figure.x-1)(figure.y-2) == null || board(figure.x-1)(figure.y-2).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x-1, figure.y-2))

		if(figure.x > 1 && figure.y > 0)
			if(board(figure.x-2)(figure.y-1) == null || board(figure.x-2)(figure.y-1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x-2, figure.y-1))

		if(figure.x > 1 && figure.y < 7)
			if(board(figure.x-2)(figure.y+1) == null || board(figure.x-2)(figure.y+1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x-2, figure.y+1))

		if(figure.x > 0 && figure.y < 6)
			if(board(figure.x-1)(figure.y+2) == null || board(figure.x-1)(figure.y+2).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x-1, figure.y+2))

    possibleMoves.result()
  }

  def findKingPossibleMoves(figure : Figure) : Vector[(Int, Int)] = {
    val possibleMoves = new VectorBuilder[(Int, Int)]

		if(figure.y < 7)
			if(board(figure.x)(figure.y+1) == null || board(figure.x)(figure.y+1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x, figure.y+1))
			
		if(figure.x < 7 && figure.y < 7)
			if(board(figure.x+1)(figure.y+1) == null || board(figure.x+1)(figure.y+1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x+1, figure.y+1))

		if(figure.x < 7)
			if(board(figure.x+1)(figure.y) == null || board(figure.x+1)(figure.y).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x+1, figure.y))

		if(figure.x < 7 && figure.y > 0)
			if(board(figure.x+1)(figure.y-1) == null || board(figure.x+1)(figure.y-1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x+1, figure.y-1))

		if(figure.y > 0)
			if(board(figure.x)(figure.y-1) == null || board(figure.x)(figure.y-1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x, figure.y-1))

		if(figure.x > 0 && figure.y > 0)
			if(board(figure.x-1)(figure.y-1) == null || board(figure.x-1)(figure.y-1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x-1, figure.y-1))

		if(figure.x > 0)
			if(board(figure.x-1)(figure.y) == null || board(figure.x-1)(figure.y).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x-1, figure.y))

		if(figure.x > 0 && figure.y < 7)
			if(board(figure.x-1)(figure.y+1) == null || board(figure.x-1)(figure.y+1).getColor == getOpponentColor(currentPlayerColor))
				possibleMoves += ((figure.x-1, figure.y+1))

    possibleMoves.result()
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
          else if(board(x)(y).getColor == getOpponentColor(currentPlayerColor))
          	vector :+ (x, y)
          else if(board(x)(y).getColor == currentPlayerColor)
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

  def findPossibleMoves(figure: Figure) : Vector[(Int, Int)] = {

    figure.getType match {
      case FigureType.Pawn => findPawnPossibleMoves(figure)
      case FigureType.Knight => findKnightPossibleMoves(figure)
      case FigureType.Bishop => findBishopPossibleMoves(figure)
      case FigureType.Rook => findRookPossibleMoves(figure)
      case FigureType.Queen => findQueenPossibleMoves(figure)
      case FigureType.King => findKingPossibleMoves(figure)
    }
  }

  /*def opponentsMove() = {
    System.out.println("ruch przeciwnika, sytuacja przed:");
    System.out.println(opponentsFigures.map(figure => (figure.x, figure.y)));
    opponentsFigures = opponentsFigures.filterNot(figure => figure.x == 6 && figure.y == 6)
    opponentsFigures = opponentsFigures :+ new Figure(FigureType.Pawn, opponentsColor, 6, 5, new JLabel(blackPawnImage));
    //getMainWindow.getBoardPanel.repaintFigures
    System.out.println("ruch przeciwnika, sytuacja po:");
    System.out.println(opponentsFigures.map(figure => (figure.x, figure.y)));
    //Thread.sleep(1500)
    getMainWindow.getBoardPanel.repaintFigures


  }

  def playersMove(f: Figure, to: (Int, Int)) {
    System.out.printf("move (" + f.x + ", " + f.y + ") to (" + to._1 + ", " + to._2 + ")\n");
    System.out.println("ruch gracza, sytuacja przed:");
    System.out.println(playersFigures.map(figure => (figure.x, figure.y)));
    playersFigures = playersFigures.filterNot(figure => figure == f);
    playersFigures = playersFigures :+ new Figure(f.getType, playerColor, to._1, to._2, f.getFigureImage);
    System.out.println("ruch gracza, sytuacja po:");
    System.out.println(playersFigures.map(figure => (figure.x, figure.y)));
    getMainWindow.getBoardPanel.repaintFigures

  }*/

	def makePlayerMove(figure : Figure, destination: (Int, Int)) : Unit = {

		if(board(destination._1)(destination._2) != null) {
			playerColor match {
				case PlayerColor.Black => {
					for(i <- 0 to 15) {
						if(whiteFigures(i) == board(destination._1)(destination._2))
							whiteFigures(i) = null
					}				
				}
				case PlayerColor.White => {
					for(i <- 0 to 15) {
						if(blackFigures(i) == board(destination._1)(destination._2))
							blackFigures(i) = null
					}	
				}
			}
		}
		board(figure.x)(figure.y) = null
		board(destination._1)(destination._2)	= figure				
		figure.x = destination._1
		figure.y = destination._2
    currentPlayerColor = getOpponentColor(currentPlayerColor)
	}

	def makeComputerMove() : Unit = {
		
		getOpponentColor(playerColor) match {
      case PlayerColor.Black => {
				for(figure <- blackFigures) {
          if(figure != null) {
            val possibleMoves = findPossibleMoves(figure)
            if(!possibleMoves.isEmpty) {
              if(board(possibleMoves.last._1)(possibleMoves.last._2) != null) {
                for(i <- 0 to 15) {
                  if(whiteFigures(i) == board(possibleMoves.last._1)(possibleMoves.last._2))
                    whiteFigures(i) = null
                }
              }
              board(figure.x)(figure.y) = null
              board(possibleMoves.last._1)(possibleMoves.last._2)	= figure
              figure.x = possibleMoves.last._1
              figure.y = possibleMoves.last._2
              currentPlayerColor = getOpponentColor(currentPlayerColor)
              return
            }
          }
				}
			}
      case PlayerColor.White => {
				for(figure <- whiteFigures) {
          if(figure != null) {
            val possibleMoves = findPossibleMoves(figure)
            if(!possibleMoves.isEmpty) {
              if(board(possibleMoves.last._1)(possibleMoves.last._2) != null) {
                for(i <- 0 to 15) {
                  if(blackFigures(i) == board(possibleMoves.last._1)(possibleMoves.last._2))
                    blackFigures(i) = null
                }
              }
              board(figure.x)(figure.y) = null
              board(possibleMoves.last._1)(possibleMoves.last._2)	= figure
              figure.x = possibleMoves.last._1
              figure.y = possibleMoves.last._2
              currentPlayerColor = getOpponentColor(currentPlayerColor)
              return
            }
          }
				}
			}
    }
	}

  def isPlayersMove : Boolean = currentPlayerColor == playerColor
  
  def figureAtSquareBelongsToPlayer(x : Int, y : Int) : Boolean = board(x)(y).getColor == playerColor
  
  //def enablePlayersMove() = playersMove = true

  def getBlackFigures: Array[Figure] = blackFigures

  def getWhiteFigures: Array[Figure] = whiteFigures

  def setPlayerColor(playerColor: PlayerColor): Unit = this.playerColor = playerColor

  def setTimeForMove(timeForMove: Long): Unit = this.timeForMove = timeForMove

  def setMainWindow(mainWindow: MainWindow) = this.mainWindow = mainWindow

  def getMainWindow: MainWindow = mainWindow
}
