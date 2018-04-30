package view;

import controller.Controller;
import model.Figure;

import java.awt.*;
import javax.swing.*;
import java.awt.FlowLayout;

import model.PlayerColor;
import scala.collection.immutable.Vector;
import scala.collection.JavaConverters;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import scala.collection.immutable.Vector;


import static model.Constants.*;
import static model.PlayerColor.*;


public class BoardPanel extends JPanel {
	
	public static final long serialVersionUID = -7729510720848698723L; // kod seryjny klasy JPanel
	
    private Color brightFieldColor = new Color(196, 200, 190);
    private Color darkFieldColor = new Color(125, 40, 15);
    private Color possibleMoveFieldColor = new Color(144, 150, 98);
    private Color selectedFieldColor = new Color(14, 150, 0);
    private Font smallFont = new Font("Helvetica", Font.BOLD, 16);
    private Font bigFont = new Font("Helvetica", Font.BOLD, 20);

    private Controller controller;

    private Figure draggedFigure = null;

    class FigureJButton extends JButton {

        public Figure figure;
        public Integer x;
        public Integer y;
        void setFigure(Figure f){
            figure = f;
        }
        FigureJButton(int _x, int _y){
            x = _x;
            y = _y;
        }
    }

    private Boolean playersMove = false;
    private FigureJButton selected;

    private FigureJButton[][] board;

    public BoardPanel(Controller controller) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        board = new FigureJButton[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = new FigureJButton(j, i);
                board[i][j].setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                board[i][j].setVisible(true);
                board[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(playersMove == true){
                            FigureJButton newSelected = (FigureJButton)e.getSource();
                            if(selected != null && selected.x.equals(newSelected.x) && selected.y.equals(newSelected.y) ){
                                if((selected.x + selected.y)%2 == 0)
                                    selected.setBackground(brightFieldColor);
                                else
                                    selected.setBackground(darkFieldColor);
                                return;
                            }
                            newSelected.setBackground(selectedFieldColor);
                            if(selected != null){
                                if((selected.x + selected.y)%2 == 0)
                                    selected.setBackground(brightFieldColor);
                                else
                                    selected.setBackground(darkFieldColor);
                            }
                            controller.getMoves(newSelected.figure);
                            System.out.println(newSelected.x + "x y" + newSelected.y + "\n");
                            selected = board[newSelected.y][newSelected.x];
                        }
                        System.out.println("nacisnieto");
                    }
                });
                if((i+j)%2 == 0)
                    board[i][j].setBackground(brightFieldColor);
                else
                    board[i][j].setBackground(darkFieldColor);
                board[i][j].setEnabled(false);
                this.add(board[i][j]);
            }
        }

        Vector<Figure> v = controller.getBlackFigures();
        Collection<Figure> c = JavaConverters.asJavaCollection(v);
        for(Figure f : c){
            System.out.println(f.x() + " " + f.y() + "\n");
            if(controller.playerColor() == PlayerColor.Black())
                board[f.y()][f.x()].setEnabled(true);
            board[f.y()][f.x()].add(f.getFigureImage());
            board[f.y()][f.x()].setFigure(f);
        }

        v = controller.getWhiteFigures();
        c = JavaConverters.asJavaCollection(v);
        for(Figure f : c){
            System.out.println(f.x() + " " + f.y() + "\n");
            if(controller.playerColor() == PlayerColor.White())
                board[f.y()][f.x()].setEnabled(true);
            board[f.y()][f.x()].add(f.getFigureImage());
            board[f.y()][f.x()].setFigure(f);
        }
        if(controller.playerColor() == PlayerColor.White())
            playersMove = true;

    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        updateView(g2d);

        /*if(algorithm.noMoveForBlack && algorithm.noMoveForWhite) {
        	ingame = false;
        	gameover = true;
        }*/

        /*if (ingame) {
            playGame(g2d);
        }
        else if(gameover){
        	drawGameOverInfo(g2d);
        	//showGameOverScreen(g2d);
        }*/
    }    
    
    private void playGame(Graphics2D g2d) {
    	//algorithm.evaluateCurrentSituationOnBoard(g2d);
    	//updateView(g2d);
    } 
    
    private void updateView(Graphics2D g2d) {
        //drawBoard(g2d);
    	//drawFigures(g2d);  	
    	//paintPossibleMovesSquares(g2d, algorithm.possibleMovesPoints);
    	drawInfo(g2d);
    }    

    private void showGameOverScreen(Graphics2D g2d) {
    }  
    
    private void drawGameOverInfo(Graphics2D g2d) {
    	String message = "Koniec gry";
    	
    	g2d.setColor(Color.orange);
        g2d.setFont(bigFont);
        
        /*if(algorithm.blackDisks.size() == algorithm.whiteDisks.size())
        	g2d.drawString(message + ", remis", 160, 100);
        else if(algorithm.blackDisks.size() > algorithm.whiteDisks.size())
        	g2d.drawString(message + ", wygrały czarne piony", 160, 100);
        else
        	g2d.drawString(message + ", wygrały białe piony", 160, 100);*/
        	
    }
    
    private void drawInfo(Graphics2D g2d) {
    	/*String message = "Czarne piony: " + algorithm.blackDisks.size() + " białe piony: " 
    + algorithm.whiteDisks.size() + " obecny gracz: " + algorithm.currentPlayerColor;
    	g2d.setColor(Color.orange);
        g2d.setFont(smallFont);
    	g2d.drawString(message, 130, 50);*/
    }
    
    private void drawBoard(Graphics2D g2d) {
    	
    	for(int i = 0; i < NUMBER_OF_SQUARES; i++)
    		for(int j = 0; j < NUMBER_OF_SQUARES; j++) {
    			if((i + j) % 2 == 0) {
    				g2d.setColor(brightFieldColor);
    		        g2d.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    			}
    			else { // (i + j) % 2 == 1
    				g2d.setColor(darkFieldColor);
    		        g2d.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    			}
    		}
    }
 
    public void paintPossibleMovesSquares(Graphics2D g2d, List<Point> possibleMovesPoints) {
    	
    	g2d.setColor(possibleMoveFieldColor);
    	Iterator<Point> iter = possibleMovesPoints.iterator();
    	Point point;
        
    	while (iter.hasNext()) {
			point = iter.next();
			g2d.fillRect((point.x-1) * SQUARE_SIZE + 1, 
				(NUMBER_OF_SQUARES - point.y) * SQUARE_SIZE + 1, 
				SQUARE_SIZE, SQUARE_SIZE-1); 	
    	}
    }
    
    private void drawFigures(Graphics g2d) {
    	
    }
    
    // function used in mouse clicked event, it returns point representing square pressed by mouse
    private Point boardSquarePoint(Point mousePoint) {       
        return new Point(mousePoint.x / SQUARE_SIZE, NUMBER_OF_SQUARES - mousePoint.y / SQUARE_SIZE - 1);
    }
}
