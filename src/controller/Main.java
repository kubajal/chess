package controller;

import java.awt.EventQueue;

import model.Constants.PlayerColor;
import view.MainWindow;

public class Main {
	
	public static void main(String[] args) {
		
        EventQueue.invokeLater(new Runnable() {
        	
        	@Override
            public void run() {
        		Controller controller = new Controller(new Algorithm(), 100, PlayerColor.White, true, false);
        		MainWindow mainWindow = new MainWindow(controller);
        		controller.setMainWindow(mainWindow);
                mainWindow.showIntroPanel();
            }
        });
    }
}
