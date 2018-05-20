package controller

import controller.Main.controller
import javax.swing.{ImageIcon, JLabel}
import model.{Figure, FigureType, PlayerColor}
import model.PlayerColor.PlayerColor
import view.MainWindow
import model.Constants._

class Controller(var mainWindow: MainWindow = null, var timeForMove: Long = 100, var playerColor: PlayerColor = PlayerColor.White,
                 var currentPlayerColor: PlayerColor = PlayerColor.White, inGame: Boolean = true, gameOver: Boolean = false) {

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

  val tmp1 = createWhiteFigures
  val tmp2 = createBlackFigures

  var currentState = new InternalState(tmp1, tmp2, createBoard(tmp1, tmp2))

  def getBoard() = currentState.getBoard

  def createWhiteFigures(): Array[Figure] = {

    var whiteFigures = new Array[Figure](16)
    for (i <- 0 to NUMBER_OF_SQUARES - 1)
      whiteFigures(i) = new Figure(FigureType.Pawn, PlayerColor.White, i, 1, new JLabel(whitePawnImage))

    whiteFigures(8) = new Figure(FigureType.Rook, PlayerColor.White, 0, 0, new JLabel(whiteRookImage))
    whiteFigures(9) = new Figure(FigureType.Rook, PlayerColor.White, 7, 0, new JLabel(whiteRookImage))
    whiteFigures(10) = new Figure(FigureType.Knight, PlayerColor.White, 1, 0, new JLabel(whiteKnightImage))
    whiteFigures(11) = new Figure(FigureType.Knight, PlayerColor.White, 6, 0, new JLabel(whiteKnightImage))
    whiteFigures(12) = new Figure(FigureType.Bishop, PlayerColor.White, 2, 0, new JLabel(whiteBishopImage))
    whiteFigures(13) = new Figure(FigureType.Bishop, PlayerColor.White, 5, 0, new JLabel(whiteBishopImage))
    whiteFigures(14) = new Figure(FigureType.Queen, PlayerColor.White, 3, 0, new JLabel(whiteQueenImage))
    whiteFigures(15) = new Figure(FigureType.King, PlayerColor.White, 4, 0, new JLabel(whiteKingImage))
    return whiteFigures
  }

  def createBlackFigures(): Array[Figure] = {

    var blackFigures = new Array[Figure](16)
    for (i <- 0 to NUMBER_OF_SQUARES - 1)
      blackFigures(i) = new Figure(FigureType.Pawn, PlayerColor.Black, i, 6, new JLabel(blackPawnImage))

    blackFigures(8) = new Figure(FigureType.Rook, PlayerColor.Black, 0, 7, new JLabel(blackRookImage))
    blackFigures(9) = new Figure(FigureType.Rook, PlayerColor.Black, 7, 7, new JLabel(blackRookImage))
    blackFigures(10) = new Figure(FigureType.Knight, PlayerColor.Black, 1, 7, new JLabel(blackKnightImage))
    blackFigures(11) = new Figure(FigureType.Knight, PlayerColor.Black, 6, 7, new JLabel(blackKnightImage))
    blackFigures(12) = new Figure(FigureType.Bishop, PlayerColor.Black, 2, 7, new JLabel(blackBishopImage))
    blackFigures(13) = new Figure(FigureType.Bishop, PlayerColor.Black, 5, 7, new JLabel(blackBishopImage))
    blackFigures(14) = new Figure(FigureType.Queen, PlayerColor.Black, 3, 7, new JLabel(blackQueenImage))
    blackFigures(15) = new Figure(FigureType.King, PlayerColor.Black, 4, 7, new JLabel(blackKingImage))
    return blackFigures
  }

  def createBoard(whiteFigures : Array[Figure], blackFigures : Array[Figure]): Array[Array[Figure]] = {

    var board = Array.ofDim[Figure](8, 8)
    for (i <- 0 to NUMBER_OF_SQUARES - 1)
      for (j <- 0 to NUMBER_OF_SQUARES - 1)
        board(i)(j) = null

    for (figure <- whiteFigures) {
      board(figure.x)(figure.y) = figure
    }

    for (figure <- blackFigures) {
      board(figure.x)(figure.y) = figure
    }
    return board
  }

  def getOpponentColor(playerColor: PlayerColor): PlayerColor = {
    playerColor match {
      case PlayerColor.Black => PlayerColor.White
      case PlayerColor.White => PlayerColor.Black
    }
  }

  def getOpponentColor(): PlayerColor = {
    if(playerColor == PlayerColor.White)
      PlayerColor.Black
    PlayerColor.White
  }

	def makePlayerMove(figure : Figure, destination: (Int, Int)) : Unit = {
    currentState = currentState.makeMove(figure, destination)
    currentPlayerColor = getOpponentColor(currentPlayerColor)
	}

  def makeComputerMove() : Unit = {

    val minimax = new Algorithm(currentState, getOpponentColor)
    val move = minimax.run()
    currentState = currentState.makeMove(move)
    currentPlayerColor = getOpponentColor(currentPlayerColor)
  }

	/*def makeComputerMove() : Unit = {

		getOpponentColor(playerColor) match {
      case PlayerColor.Black => {
				for(figure <- blackFigures) {
          if(figure != null) {
            val possibleMoves = currentState.findPossibleMoves(figure)
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
            val possibleMoves = currentState.findPossibleMoves(figure)
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
	}*/

  def findPossibleMoves(figure : Figure) = currentState.findPossibleMoves(figure)

  def isPlayersMove : Boolean = currentPlayerColor == playerColor
  
  def figureAtSquareBelongsToPlayer(x : Int, y : Int) : Boolean = getBoard()(x)(y).getColor == playerColor

  def getBlackFigures: Array[Figure] = currentState.getWhiteFigures

  def getWhiteFigures: Array[Figure] = currentState.getBlackFigures

  def setPlayerColor(playerColor: PlayerColor): Unit = this.playerColor = playerColor

  def setTimeForMove(timeForMove: Long): Unit = this.timeForMove = timeForMove

  def setMainWindow(mainWindow: MainWindow) = this.mainWindow = mainWindow

  def getMainWindow: MainWindow = mainWindow
}
