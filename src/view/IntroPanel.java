package view;

import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;
import model.PlayerColor;

public class IntroPanel extends JPanel{

	private static final long serialVersionUID = -9025564695102872265L;

	private JButton acceptButton, whitePlayerColorButton, blackPlayerColorButton;
    
    private Label playerColorLabel1, playerColorLabel2;
    private TextField playerColorField, timeField;
    
    private Controller controller;
	
    boolean playerHasWhiteFigures;
    
    IntroPanel(Controller controller) {
    	
    	this.controller = controller;
    	setLayout(new FlowLayout(FlowLayout.CENTER, 1000, 5));
    	
    	whitePlayerColorButton = new JButton("Biały");
    	blackPlayerColorButton = new JButton("Czarny");
    	acceptButton = new JButton("Ok");
		playerHasWhiteFigures = true;
    	
    	whitePlayerColorButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				playerColorField.setText("Biały");
				playerHasWhiteFigures = true;
			}
		});
    	blackPlayerColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerColorField.setText("Czarny");
				playerHasWhiteFigures = false;
			}
		});
    	acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (playerHasWhiteFigures){
					controller.setPlayerColor(PlayerColor.White());
					controller.setCurrentPlayersColor(PlayerColor.White());
				}
				else { // playerHasWhiteFigures == false
					controller.setPlayerColor(PlayerColor.Black());
					controller.setCurrentPlayersColor(PlayerColor.Black());
					controller.makeComputerMove();
				}
				controller.getMainWindow().showBoardPanel();

			}
		});
    	
    	playerColorLabel1 = new Label("Wybierz kolor figur, którymi będzie grał gracz.");
    	playerColorLabel2 = new Label("Komputer otrzyma figury przeciwnego koloru");
        
        playerColorField = new TextField("Biały", 6);
        playerColorField.setEditable(false);
        
        add(playerColorLabel1);
        add(playerColorLabel2);
        add(whitePlayerColorButton);
        add(blackPlayerColorButton);
        add(playerColorField);
        add(acceptButton);
    }
}
