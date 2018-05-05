package controller

import java.awt.Point

import javax.swing.{ImageIcon, JLabel}
import model.{Figure, FigureType, PlayerColor}
import model.PlayerColor.PlayerColor
import view.{BoardPanel, MainWindow}
import model.Constants._

case class Controller(var playersFigures: Vector[Figure] = Vector.empty[Figure], var opponentsFigures: Vector[Figure] = Vector.empty[Figure],
                      var mainWindow: MainWindow = null, var timeForMove: Long = 100, var playerColor: PlayerColor = PlayerColor.White,
                      var opponentsColor: PlayerColor = PlayerColor.Black, board: Array[Array[Figure]] = Array.ofDim[Figure](8, 8),
                      inGame: Boolean = true, gameOver: Boolean = false, var playersMove: Boolean = false) {

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
          playersFigures = playersFigures :+ new Figure(FigureType.Pawn, PlayerColor.Black, i, 1, new JLabel(blackPawnImage))

        for (i <- 0 to NUMBER_OF_SQUARES - 1)
          opponentsFigures = opponentsFigures :+ new Figure(FigureType.Pawn, PlayerColor.White, i, 6, new JLabel(whitePawnImage))

        playersFigures = playersFigures :+ new Figure(FigureType.Rook, PlayerColor.Black, 0, 0, new JLabel(blackRookImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Rook, PlayerColor.Black, 7, 0, new JLabel(blackRookImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Rook, PlayerColor.White, 0, 7, new JLabel(whiteRookImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Rook, PlayerColor.White, 7, 7, new JLabel(whiteRookImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Knight, PlayerColor.Black, 1, 0, new JLabel(blackKnightImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Knight, PlayerColor.Black, 6, 0, new JLabel(blackKnightImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Knight, PlayerColor.White, 1, 7, new JLabel(whiteKnightImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Knight, PlayerColor.White, 6, 7, new JLabel(whiteKnightImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Bishop, PlayerColor.Black, 2, 0, new JLabel(blackBishopImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Bishop, PlayerColor.Black, 5, 0, new JLabel(blackBishopImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Bishop, PlayerColor.White, 2, 7, new JLabel(whiteBishopImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Bishop, PlayerColor.White, 5, 7, new JLabel(whiteBishopImage))
        playersFigures = playersFigures :+ new Figure(FigureType.King, PlayerColor.Black, 3, 0, new JLabel(blackKingImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Queen, PlayerColor.Black, 4, 0, new JLabel(blackQueenImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.King, PlayerColor.White, 3, 7, new JLabel(whiteKingImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Queen, PlayerColor.White, 4, 7, new JLabel(whiteQueenImage))

      case PlayerColor.White =>

        for (i <- 0 to NUMBER_OF_SQUARES - 1)
          playersFigures = playersFigures :+ new Figure(FigureType.Pawn, PlayerColor.White, i, 1, new JLabel(whitePawnImage))

        for (i <- 0 to NUMBER_OF_SQUARES - 1)
          opponentsFigures = opponentsFigures :+ new Figure(FigureType.Pawn, PlayerColor.Black, i, 6, new JLabel(blackPawnImage))

        playersFigures = playersFigures :+ new Figure(FigureType.Rook, PlayerColor.White, 0, 0, new JLabel(whiteRookImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Rook, PlayerColor.White, 7, 0, new JLabel(whiteRookImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Rook, PlayerColor.Black, 0, 7, new JLabel(blackRookImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Rook, PlayerColor.Black, 7, 7, new JLabel(blackRookImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Knight, PlayerColor.White, 1, 0, new JLabel(whiteKnightImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Knight, PlayerColor.White, 6, 0, new JLabel(whiteKnightImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Knight, PlayerColor.Black, 1, 7, new JLabel(blackKnightImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Knight, PlayerColor.Black, 6, 7, new JLabel(blackKnightImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Bishop, PlayerColor.White, 2, 0, new JLabel(whiteBishopImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Bishop, PlayerColor.White, 5, 0, new JLabel(whiteBishopImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Bishop, PlayerColor.Black, 2, 7, new JLabel(blackBishopImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Bishop, PlayerColor.Black, 5, 7, new JLabel(blackBishopImage))
        playersFigures = playersFigures :+ new Figure(FigureType.Queen, PlayerColor.White, 3, 0, new JLabel(whiteQueenImage))
        playersFigures = playersFigures :+ new Figure(FigureType.King, PlayerColor.White, 4, 0, new JLabel(whiteKingImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.Queen, PlayerColor.Black, 3, 7, new JLabel(blackQueenImage))
        opponentsFigures = opponentsFigures :+ new Figure(FigureType.King, PlayerColor.Black, 4, 7, new JLabel(blackKingImage))
    }
  }

  def setTimeForMove(timeForMove: Long): Unit = this.timeForMove = timeForMove

  def setPlayerColor(playerColor: PlayerColor): Unit = {
    playerColor match {
      case PlayerColor.Black => {
        this.playerColor = PlayerColor.Black
        this.opponentsColor = PlayerColor.White
      }
      case PlayerColor.White => {
        this.playerColor = PlayerColor.White
        this.opponentsColor = PlayerColor.Black
      }
    }
    createFigures
  }

  def setMainWindow(mainWindow: MainWindow) = this.mainWindow = mainWindow

  def getMainWindow: MainWindow = mainWindow

  def getBoard: Array[Array[Figure]] = board

  def getMoves(figure: Figure): Vector[(scala.Int, scala.Int)] = {
    println(figure);
    val x = figure.x
    val y = figure.y
    figure.getType match {
      case FigureType.Pawn => {
        figure.getColor match {
          case PlayerColor.White => {
            println(" -bialy")
          }
          case PlayerColor.Black => s{
            println(" -czarny")
          }
        }
      }
      case FigureType.Rook => {
        println("wieza")
      }
      case FigureType.Knight => {
        println("kon")
      }
      case FigureType.Bishop => {
        println("goniec")
      }
      case FigureType.King=> {
        println("krol")
      }
      case FigureType.Queen => {
        println("hetman")
      }
    }

    return Vector[(Int, Int)]((x, y+1));
  }

  def opponentsMove() = {
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

  }

  def enablePlayersMove() = playersMove = true

  def getPlayersFigures: Vector[Figure] = playersFigures
  def getOpponentsFigures: Vector[Figure] = opponentsFigures
}