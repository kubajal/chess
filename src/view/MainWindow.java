package view;

import javax.swing.JFrame;

import controller.Controller;
import model.Constants;

public class MainWindow extends JFrame implements Constants {
	
	private static final long serialVersionUID = -7729510720848698724L; // kod seryjny klasy JFrame
	
	private Controller controller;
	private IntroPanel introPanel;
	private BoardPanel boardPanel;
	
    public MainWindow(Controller controller)
    {
    	this.controller = controller;
    }
    
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
    	setSize(BOARD_WIDTH, BOARD_HEIGHT);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setLocationRelativeTo(null); // ustawiamy ekran gry na srodku ekranu
    	setResizable(false);
    }
}