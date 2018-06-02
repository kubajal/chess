package view;

import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;

import controller.Controller;
import model.PlayerColor;

public class IntroPanel extends JPanel{

	private static final long serialVersionUID = -9025564695102872265L;

	private JButton acceptButton, whitePlayerColorButton, blackPlayerColorButton;
    
    private Label playerColorLabel1, playerColorLabel2, algorithmDepthLabel;
    private TextField playerColorField, timeField;
    private SpinnerModel depthSpinnerModel;
	private JSpinner depthSpinner;

    private Controller controller;
	
    private boolean playerHasWhiteFigures;
    
    IntroPanel(Controller controller) {
    	
    	this.controller = controller;
    	setLayout(new FlowLayout(FlowLayout.CENTER, 1000, 5));
    	
    	whitePlayerColorButton = new JButton("Biały");
    	blackPlayerColorButton = new JButton("Czarny");
    	acceptButton = new JButton("Ok");
		playerHasWhiteFigures = true;
 		depthSpinnerModel = new SpinnerNumberModel(2, 1, 5, 1); 
		depthSpinner = new JSpinner(depthSpinnerModel);
    	
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

				controller.setAlgorithmDepth((int) depthSpinner.getValue());
				
				if (playerHasWhiteFigures)
					controller.setPlayerColor(PlayerColor.White());
				else // playerHasWhiteFigures == false
					controller.setPlayerColor(PlayerColor.Black());
				controller.getMainWindow().showBoardPanel();
			}
		});
    	
    	playerColorLabel1 = new Label("Wybierz kolor figur, którymi będzie grał gracz.");
    	playerColorLabel2 = new Label("Komputer otrzyma figury przeciwnego koloru");
		algorithmDepthLabel = new Label("Wybierz głębokość drzewa dla algorytmu Minimax");
        
        playerColorField = new TextField("Biały", 6);
        playerColorField.setEditable(false);
        
        add(playerColorLabel1);
        add(playerColorLabel2);
        add(whitePlayerColorButton);
        add(blackPlayerColorButton);
        add(playerColorField);
		add(algorithmDepthLabel);
		add(depthSpinner);;
        add(acceptButton);
    }
}
