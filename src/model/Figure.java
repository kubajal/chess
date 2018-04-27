package model;

import java.awt.Point;

import javax.swing.JLabel;

import model.Constants.FigureType;
import model.Constants.PlayerColor;

public class Figure {
	
	private FigureType figureType;
	private PlayerColor playerColor;
	private Point point;
	private JLabel figureImage;
	
	public Figure() {
		super();
	}
	
	public Figure(FigureType figureType, PlayerColor playerColor, Point point, JLabel figureImage) {
		super();
		this.figureType = figureType;
		this.playerColor = playerColor;
		this.point = point;
		this.figureImage = figureImage;
	}

	public FigureType getFigureType() {
		return figureType;
	}

	public void setFigureType(FigureType figureType) {
		this.figureType = figureType;
	}

	public PlayerColor getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(PlayerColor playerColor) {
		this.playerColor = playerColor;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
	
	public JLabel getFigureImage() {
		return figureImage;
	}

	public void setFigureImage(JLabel figureImage) {
		this.figureImage = figureImage;
	}
}
