package controller

import java.awt.Point

import javax.swing.{ImageIcon, JLabel}
import model.{Figure, FigureType, PlayerColor}
import model.PlayerColor.PlayerColor
import view.{BoardPanel, MainWindow}
import model.Constants._

case class Controller(var blackFigures: Vector[Figure] = Vector.empty[Figure], var whiteFigures: Vector[Figure] = Vector.empty[Figure],
    var mainWindow: MainWindow = null, var timeForMove: Long = 100, var playerColor: PlayerColor = PlayerColor.White,
    board: Array[Array[Figure]] = Array.ofDim[Figure](8, 8), inGame: Boolean = true, gameOver: Boolean = false) {

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

  def createFigures(): Unit = {

    playerColor match {
      case PlayerColor.Black =>

        for (i <- 0 to NUMBER_OF_SQUARES - 1)
          blackFigures = blackFigures :+ new Figure(FigureType.Pawn, PlayerColor.Black, i, 1, new JLabel(blackPawnImage))

        for (i <- 0 to NUMBER_OF_SQUARES - 1)
          whiteFigures = whiteFigures :+ new Figure(FigureType.Pawn, PlayerColor.White, i, 6, new JLabel(whitePawnImage))

        blackFigures = blackFigures :+ new Figure(FigureType.Rook, PlayerColor.Black, 0, 0, new JLabel(blackRookImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Rook, PlayerColor.Black, 7, 0, new JLabel(blackRookImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Rook, PlayerColor.White, 0, 7, new JLabel(whiteRookImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Rook, PlayerColor.White, 7, 7, new JLabel(whiteRookImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Knight, PlayerColor.Black, 1, 0, new JLabel(blackKnightImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Knight, PlayerColor.Black, 6, 0, new JLabel(blackKnightImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Knight, PlayerColor.White, 1, 7, new JLabel(whiteKnightImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Knight, PlayerColor.White, 6, 7, new JLabel(whiteKnightImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Bishop, PlayerColor.Black, 2, 0, new JLabel(blackBishopImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Bishop, PlayerColor.Black, 5, 0, new JLabel(blackBishopImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Bishop, PlayerColor.White, 2, 7, new JLabel(whiteBishopImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Bishop, PlayerColor.White, 5, 7, new JLabel(whiteBishopImage))
        blackFigures = blackFigures :+ new Figure(FigureType.King, PlayerColor.Black, 3, 0, new JLabel(blackKingImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Queen, PlayerColor.Black, 4, 0, new JLabel(blackQueenImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.King, PlayerColor.White, 3, 7, new JLabel(whiteKingImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Queen, PlayerColor.White, 4, 7, new JLabel(whiteQueenImage))

      case PlayerColor.White =>

        for (i <- 0 to NUMBER_OF_SQUARES - 1)
          whiteFigures = whiteFigures :+ new Figure(FigureType.Pawn, PlayerColor.White, i, 1, new JLabel(whitePawnImage))

        for (i <- 0 to NUMBER_OF_SQUARES - 1)
          blackFigures = blackFigures :+ new Figure(FigureType.Pawn, PlayerColor.Black, i, 6, new JLabel(blackPawnImage))

        whiteFigures = whiteFigures :+ new Figure(FigureType.Rook, PlayerColor.White, 0, 0, new JLabel(whiteRookImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Rook, PlayerColor.White, 7, 0, new JLabel(whiteRookImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Rook, PlayerColor.Black, 0, 7, new JLabel(blackRookImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Rook, PlayerColor.Black, 7, 7, new JLabel(blackRookImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Knight, PlayerColor.White, 1, 0, new JLabel(whiteKnightImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Knight, PlayerColor.White, 6, 0, new JLabel(whiteKnightImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Knight, PlayerColor.Black, 1, 7, new JLabel(blackKnightImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Knight, PlayerColor.Black, 6, 7, new JLabel(blackKnightImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Bishop, PlayerColor.White, 2, 0, new JLabel(whiteBishopImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Bishop, PlayerColor.White, 5, 0, new JLabel(whiteBishopImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Bishop, PlayerColor.Black, 2, 7, new JLabel(blackBishopImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Bishop, PlayerColor.Black, 5, 7, new JLabel(blackBishopImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.Queen, PlayerColor.White, 3, 0, new JLabel(whiteQueenImage))
        whiteFigures = whiteFigures :+ new Figure(FigureType.King, PlayerColor.White, 4, 0, new JLabel(whiteKingImage))
        blackFigures = blackFigures :+ new Figure(FigureType.Queen, PlayerColor.Black, 3, 7, new JLabel(blackQueenImage))
        blackFigures = blackFigures :+ new Figure(FigureType.King, PlayerColor.Black, 4, 7, new JLabel(blackKingImage))
    }
  }

  def setTimeForMove(timeForMove: Long): Unit = this.timeForMove = timeForMove

  def setPlayerColor(playerColor: PlayerColor): Unit = this.playerColor = playerColor

  def setMainWindow(mainWindow: MainWindow) = this.mainWindow = mainWindow

  def getMainWindow: MainWindow = mainWindow

  def getBlackFigures: Vector[Figure] = blackFigures

  def getWhiteFigures: Vector[Figure] = whiteFigures

  def getBoard: Array[Array[Figure]] = board

  def getMoves(figure: Figure): Unit ={

  }
}