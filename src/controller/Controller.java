package controller;

import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import model.Constants;
import model.Figure;
import view.MainWindow;

public class Controller implements Constants {

	private MainWindow mainWindow;
	private Algorithm algorithm;
	private long timeForMove;
	private PlayerColor playerColor;
	private Figure board[][];
	private ArrayList<Figure> blackFigures, whiteFigures;
	private boolean ingame;
	private boolean gameover;
	
	private ImageIcon whitePawnImage, whiteKnightImage, whiteBishopImage, whiteRookImage, whiteQueenImage, whiteKingImage;
    private ImageIcon blackPawnImage, blackKnightImage, blackBishopImage, blackRookImage, blackQueenImage, blackKingImage;
    

	public Controller(Algorithm algorithm, long timeForMove, PlayerColor playerColor, boolean ingame,
			boolean gameover) {
		super();
		this.algorithm = algorithm;
		this.timeForMove = timeForMove;
		this.playerColor = playerColor;
		this.ingame = ingame;
		this.gameover = gameover;
		loadImages();
	}

	public void createFigures() {
		blackFigures = new ArrayList<Figure>();
		whiteFigures = new ArrayList<Figure>();

		switch (playerColor) {
		case Black:
			for(int i = 0; i < NUMBER_OF_SQUARES; i++)
				blackFigures.add(new Figure(FigureType.Pawn, PlayerColor.Black, new Point(i, 1), new JLabel(blackPawnImage)));
			for(int i = 0; i < NUMBER_OF_SQUARES; i++)
				whiteFigures.add(new Figure(FigureType.Pawn, PlayerColor.White, new Point(i, 6), new JLabel(whitePawnImage)));
			
			blackFigures.add(new Figure(FigureType.Rook, PlayerColor.Black, new Point(0, 0), new JLabel(blackRookImage)));
			blackFigures.add(new Figure(FigureType.Rook, PlayerColor.Black, new Point(7, 0), new JLabel(blackRookImage)));
			whiteFigures.add(new Figure(FigureType.Rook, PlayerColor.White, new Point(0, 7), new JLabel(whiteRookImage)));
			whiteFigures.add(new Figure(FigureType.Rook, PlayerColor.White, new Point(7, 7), new JLabel(whiteRookImage)));
		
			blackFigures.add(new Figure(FigureType.Knight, PlayerColor.Black, new Point(1, 0), new JLabel(blackKnightImage)));
			blackFigures.add(new Figure(FigureType.Knight, PlayerColor.Black, new Point(6, 0), new JLabel(blackKnightImage)));
			whiteFigures.add(new Figure(FigureType.Knight, PlayerColor.White, new Point(1, 7), new JLabel(whiteKnightImage)));
			whiteFigures.add(new Figure(FigureType.Knight, PlayerColor.White, new Point(6, 7), new JLabel(whiteKnightImage)));
		
			blackFigures.add(new Figure(FigureType.Bishop, PlayerColor.Black, new Point(2, 0), new JLabel(blackBishopImage)));
			blackFigures.add(new Figure(FigureType.Bishop, PlayerColor.Black, new Point(5, 0), new JLabel(blackBishopImage)));
			whiteFigures.add(new Figure(FigureType.Bishop, PlayerColor.White, new Point(2, 7), new JLabel(whiteBishopImage)));
			whiteFigures.add(new Figure(FigureType.Bishop, PlayerColor.White, new Point(5, 7), new JLabel(whiteBishopImage)));
		
			blackFigures.add(new Figure(FigureType.King, PlayerColor.Black, new Point(3, 0), new JLabel(blackKingImage)));
			blackFigures.add(new Figure(FigureType.Queen, PlayerColor.Black, new Point(4, 0), new JLabel(blackQueenImage)));
			whiteFigures.add(new Figure(FigureType.King, PlayerColor.White, new Point(3, 7), new JLabel(whiteKingImage)));
			whiteFigures.add(new Figure(FigureType.Queen, PlayerColor.White, new Point(4, 7), new JLabel(whiteQueenImage)));
			break;
		case White:
			for(int i = 0; i < NUMBER_OF_SQUARES; i++)
				whiteFigures.add(new Figure(FigureType.Pawn, PlayerColor.White, new Point(i, 1), new JLabel(whitePawnImage)));
			for(int i = 0; i < NUMBER_OF_SQUARES; i++)
				blackFigures.add(new Figure(FigureType.Pawn, PlayerColor.Black, new Point(i, 6), new JLabel(blackPawnImage)));
			
			whiteFigures.add(new Figure(FigureType.Rook, PlayerColor.White, new Point(0, 0), new JLabel(whiteRookImage)));
			whiteFigures.add(new Figure(FigureType.Rook, PlayerColor.White, new Point(7, 0), new JLabel(whiteRookImage)));
			blackFigures.add(new Figure(FigureType.Rook, PlayerColor.Black, new Point(0, 7), new JLabel(blackRookImage)));
			blackFigures.add(new Figure(FigureType.Rook, PlayerColor.Black, new Point(7, 7), new JLabel(blackRookImage)));
		
			whiteFigures.add(new Figure(FigureType.Knight, PlayerColor.White, new Point(1, 0), new JLabel(whiteKnightImage)));
			whiteFigures.add(new Figure(FigureType.Knight, PlayerColor.White, new Point(6, 0), new JLabel(whiteKnightImage)));
			blackFigures.add(new Figure(FigureType.Knight, PlayerColor.Black, new Point(1, 7), new JLabel(blackKnightImage)));
			blackFigures.add(new Figure(FigureType.Knight, PlayerColor.Black, new Point(6, 7), new JLabel(blackKnightImage)));
		
			whiteFigures.add(new Figure(FigureType.Bishop, PlayerColor.White, new Point(2, 0), new JLabel(whiteBishopImage)));
			whiteFigures.add(new Figure(FigureType.Bishop, PlayerColor.White, new Point(5, 0), new JLabel(whiteBishopImage)));
			blackFigures.add(new Figure(FigureType.Bishop, PlayerColor.Black, new Point(2, 7), new JLabel(blackBishopImage)));
			blackFigures.add(new Figure(FigureType.Bishop, PlayerColor.Black, new Point(5, 7), new JLabel(blackBishopImage)));
		
			whiteFigures.add(new Figure(FigureType.Queen, PlayerColor.White, new Point(3, 0), new JLabel(whiteQueenImage)));
			whiteFigures.add(new Figure(FigureType.King, PlayerColor.White, new Point(4, 0), new JLabel(whiteKingImage)));
			blackFigures.add(new Figure(FigureType.Queen, PlayerColor.Black, new Point(3, 7), new JLabel(blackQueenImage)));
			blackFigures.add(new Figure(FigureType.King, PlayerColor.Black, new Point(4, 7), new JLabel(blackKingImage)));
			break;
		}
	}

	public void createBoard() {
		board = new Figure[8][8];
		
		for(int i = 0; i < NUMBER_OF_SQUARES; i++)
			for(int j = 0; j < NUMBER_OF_SQUARES; j++) {
				board[i][j] = null;
			}
		
		for(Figure figure : whiteFigures) {
			board[figure.getPoint().x][figure.getPoint().y] = figure;
		}
		
		for(Figure figure : blackFigures) {
			board[figure.getPoint().x][figure.getPoint().y] = figure;
		}
	}

	private PlayerColor getOppositePlayerColor(PlayerColor playerColor) {
		switch (playerColor) {
		case Black:
			return PlayerColor.White;
		case White:
			return PlayerColor.Black;
		default:
			return null;
		}
	}
	
    private void loadImages() {
    	
    	URL url;
    	url = getClass().getResource("/images/white_pawn.png");
        whitePawnImage = new ImageIcon(url);
        url = getClass().getResource("/images/white_knight.png");
        whiteKnightImage = new ImageIcon(url);
        url = getClass().getResource("/images/white_bishop.png");
        whiteBishopImage = new ImageIcon(url);
        url = getClass().getResource("/images/white_rook.png");
        whiteRookImage = new ImageIcon(url);
        url = getClass().getResource("/images/white_queen.png");
        whiteQueenImage = new ImageIcon(url);
        url = getClass().getResource("/images/white_king.png");
        whiteKingImage = new ImageIcon(url);
        url = getClass().getResource("/images/black_pawn.png");
        blackPawnImage = new ImageIcon(url);
        url = getClass().getResource("/images/black_knight.png");
        blackKnightImage = new ImageIcon(url);
        url = getClass().getResource("/images/black_bishop.png");
        blackBishopImage = new ImageIcon(url);
        url = getClass().getResource("/images/black_rook.png");
        blackRookImage = new ImageIcon(url);
        url = getClass().getResource("/images/black_queen.png");
        blackQueenImage = new ImageIcon(url);
        url = getClass().getResource("/images/black_king.png");
        blackKingImage = new ImageIcon(url);
    } 
	
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public long getTimeForMove() {
		return timeForMove;
	}

	public void setTimeForMove(long timeForMove) {
		this.timeForMove = timeForMove;
	}

	public PlayerColor getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(PlayerColor playerColor) {
		this.playerColor = playerColor;
	}
	
	public Figure[][] getBoard() {
		return board;
	}

	public ArrayList<Figure> getBlackFigures() {
		return blackFigures;
	}

	public ArrayList<Figure> getWhiteFigures() {
		return whiteFigures;
	}	
}
