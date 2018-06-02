package view;

import javax.swing.JFrame;

import controller.Controller;
import model.Constants;
import model.Figure;
import model.PlayerColor;

public class MainWindow extends JFrame implements Constants {

	private Controller controller;
	private IntroPanel introPanel;
	private BoardPanel boardPanel;
	
    public MainWindow(Controller controller){
    	this.controller = controller;
    }
    public MainWindow() {}

    public void showIntroPanel() {
    	introPanel = new IntroPanel(this.controller);
    	getContentPane().removeAll();
    	getContentPane().add(introPanel);
        setTitle("Szachy");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(INTRO_WINDOW_WIDTH, INTRO_WINDOW_HEIGHT);
        setLocationRelativeTo(null); // ustawiamy okno powitalne na srodku ekranu
        setVisible(true);
        setResizable(false);
    }
    
    public void showBoardPanel() {
    	boardPanel = new BoardPanel(this.controller);
    	getContentPane().removeAll();
    	getContentPane().add(boardPanel);
    	setTitle("Szachy");
    	setSize(BOARD_WIDTH, BOARD_HEIGHT + 100);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setLocationRelativeTo(null); // ustawiamy okno gry na srodku ekranu
    	setResizable(false);
		if(controller.playerColor() == PlayerColor.Black())
			controller.makeComputerMove();
	}

    public BoardPanel getBoardPanel(){
    	return boardPanel;
	}

	public Figure getFigure(int x, int y){
    	return boardPanel.getFigure(x, y);
	}
}