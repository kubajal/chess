package controller

import javax.swing.{ImageIcon, JLabel}
import model.{Figure, FigureType, Images, PlayerColor}
import model.PlayerColor.PlayerColor
import view.MainWindow
import model.Constants._
import model.FigureType.FigureType

case class Controller(var mainWindow: MainWindow = null, var timeForMove: Long = 100, var playerColor: PlayerColor = PlayerColor.White,
                 var currentPlayerColor: PlayerColor = PlayerColor.White, var algorithmDepth : Int = 2) extends Images {

  val whiteFigures = createWhiteFigures
  val blackFigures = createBlackFigures

  var currentState = new InternalState(whiteFigures, blackFigures , createBoard(whiteFigures, blackFigures ), currentPlayerColor)

  def getBoard() = currentState.getBoard

  def createWhiteFigures(): Vector[Figure] = {

    return Vector[Figure](
      new Figure(FigureType.Queen, PlayerColor.White, 3, 0, new JLabel(whiteQueenImage)),
      new Figure(FigureType.Rook, PlayerColor.White, 7, 0, new JLabel(whiteRookImage)),
      new Figure(FigureType.Rook, PlayerColor.White, 0, 0, new JLabel(whiteRookImage)),
      new Figure(FigureType.Knight, PlayerColor.White, 1, 0, new JLabel(whiteKnightImage)),
      new Figure(FigureType.Knight, PlayerColor.White, 6, 0, new JLabel(whiteKnightImage)),
      new Figure(FigureType.Bishop, PlayerColor.White, 2, 0, new JLabel(whiteBishopImage)),
      new Figure(FigureType.Bishop, PlayerColor.White, 5, 0, new JLabel(whiteBishopImage)),
      new Figure(FigureType.King, PlayerColor.White, 4, 0, new JLabel(whiteKingImage)),
      new Figure(FigureType.Pawn, PlayerColor.White, 0, 1, new JLabel(whitePawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.White, 1, 1, new JLabel(whitePawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.White, 2, 1, new JLabel(whitePawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.White, 3, 1, new JLabel(whitePawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.White, 4, 1, new JLabel(whitePawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.White, 5, 1, new JLabel(whitePawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.White, 6, 1, new JLabel(whitePawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.White, 7, 1, new JLabel(whitePawnImage))
    )
  }

  def createBlackFigures(): Vector[Figure] = {

    return Vector[Figure](
      new Figure(FigureType.Queen, PlayerColor.Black, 3, 7, new JLabel(blackQueenImage)),
      new Figure(FigureType.Rook, PlayerColor.Black, 7, 7, new JLabel(blackRookImage)),
      new Figure(FigureType.Rook, PlayerColor.Black, 0, 7, new JLabel(blackRookImage)),
      new Figure(FigureType.Knight, PlayerColor.Black, 1, 7, new JLabel(blackKnightImage)),
      new Figure(FigureType.Knight, PlayerColor.Black, 6, 7, new JLabel(blackKnightImage)),
      new Figure(FigureType.Bishop, PlayerColor.Black, 2, 7, new JLabel(blackBishopImage)),
      new Figure(FigureType.Bishop, PlayerColor.Black, 5, 7, new JLabel(blackBishopImage)),
      new Figure(FigureType.King, PlayerColor.Black, 4, 7, new JLabel(blackKingImage)),
      new Figure(FigureType.Pawn, PlayerColor.Black, 0, 6, new JLabel(blackPawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.Black, 1, 6, new JLabel(blackPawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.Black, 2, 6, new JLabel(blackPawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.Black, 3, 6, new JLabel(blackPawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.Black, 4, 6, new JLabel(blackPawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.Black, 5, 6, new JLabel(blackPawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.Black, 6, 6, new JLabel(blackPawnImage)),
      new Figure(FigureType.Pawn, PlayerColor.Black, 7, 6, new JLabel(blackPawnImage))
    )
  }

  def createBoard(w : Vector[Figure], b : Vector[Figure]): Vector[Vector[Figure]] = {

    return Vector[Vector[Figure]](
      Vector[Figure](w(1), w(3), w(5), w(7), w(0), w(6), w(4), w(2)),
      Vector[Figure](w(8), w(9), w(10), w(11), w(12), w(13), w(14), w(15)),
      Vector[Figure](null, null, null, null, null, null, null, null),
      Vector[Figure](null, null, null, null, null, null, null, null),
      Vector[Figure](null, null, null, null, null, null, null, null),
      Vector[Figure](null, null, null, null, null, null, null, null),
      Vector[Figure](b(8), b(9), b(10), b(11), b(12), b(13), b(14), b(15)),
      Vector[Figure](b(1), b(3), b(5), b(7), b(0), b(6), b(4), b(2)))
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
	}

  def makeComputerMove() : Unit = {
    val minimax = new Algorithm(currentState.copy(), currentPlayerColor, algorithmDepth)
    val move = minimax.run()
    currentState = currentState.makeMove(move)
    mainWindow.getBoardPanel.repaintFigures()
    mainWindow.getBoardPanel.enableFigures()
    currentPlayerColor = getOpponentColor(currentPlayerColor)
  }

  def computerVsComputer() : Unit = {
    val minimaxFirst = new Algorithm(currentState.copy(), currentPlayerColor, algorithmDepth)
    val firstMove = minimaxFirst.run()
    currentState = currentState.makeMove(firstMove)
    mainWindow.getBoardPanel.repaintFigures()
    currentPlayerColor = getOpponentColor(currentPlayerColor)

    val minimaxSecond = new Algorithm(currentState.copy(), currentPlayerColor, algorithmDepth)
    val secondMove = minimaxSecond.run()
    currentState = currentState.makeMove(secondMove)
    mainWindow.getBoardPanel.repaintFigures()
    currentPlayerColor = getOpponentColor(currentPlayerColor)
  }

	def gameOver(winnerPlayerColor : PlayerColor) : Unit = {
		winnerPlayerColor match {
      case PlayerColor.Black => mainWindow.getBoardPanel.displayGameOverInfo("Koniec gry. Wygrały czarne figury")
      case PlayerColor.White => mainWindow.getBoardPanel.displayGameOverInfo("Koniec gry. Wygrały białe figury")
    }
	}

  def setCurrentPlayersColor(color : PlayerColor) = currentPlayerColor = color

  def findPossibleMoves(figure : Figure) = currentState.findPossibleMoves(figure)

  def isPlayersMove : Boolean = currentPlayerColor == playerColor
  
  def figureAtSquareBelongsToPlayer(x : Int, y : Int) : Boolean = getBoard()(x)(y).getColor == playerColor

  def getBlackFigures: Vector[Figure] = currentState.getBlackFigures

  def getWhiteFigures: Vector[Figure] = currentState.getWhiteFigures

  def setPlayerColor(_playerColor: PlayerColor): Unit = this.playerColor = _playerColor

  def setTimeForMove(timeForMove: Long): Unit = this.timeForMove = timeForMove

  def setMainWindow(mainWindow: MainWindow) = this.mainWindow = mainWindow

  def getMainWindow: MainWindow = mainWindow

  def setAlgorithmDepth(algorithmDepth : Int) = this.algorithmDepth = algorithmDepth
}
