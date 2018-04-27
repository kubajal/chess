package model;

public interface Constants {
    
    public enum PlayerColor {
        Black, White
    }	
    
    public enum FigureType {
    	Pawn, Knight, Bishop, Rook, Queen, King
    }
	
	// Stale uzywane w programie
	public static final int INTRO_WINDOW_WIDTH = 320;
	public static final int INTRO_WINDOW_HEIGHT = 350;	
	public static final int BOARD_WIDTH = 566;
	public static final int BOARD_HEIGHT = 588;
	public static final int NUMBER_OF_SQUARES = 8;
	public static final int SQUARE_SIZE = 70;
	public static final int IMAGE_OFFSET = 6;
}
