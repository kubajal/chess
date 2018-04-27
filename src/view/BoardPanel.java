package view;

import model.Figure;
import model.Constants;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

import controller.Controller;


public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, Constants {
	
	public static final long serialVersionUID = -7729510720848698723L; // kod seryjny klasy JPanel
	
    private Color brightFieldColor = new Color(160, 200, 160);
    private Color darkFieldColor = new Color(20, 160, 20);
    private Color possibleMoveFieldColor = new Color(40, 150, 40);
    private Font smallFont = new Font("Helvetica", Font.BOLD, 16);
    private Font bigFont = new Font("Helvetica", Font.BOLD, 20);
	
    private Dimension dimension;
    
    private Controller controller;
        
    private Figure draggedFigure = null;
    
    public BoardPanel(Controller controller) {
        this.controller = controller;
        this.setLayout(null);
        placeImagesOnBoard();
        
        setFocusable(true);
        dimension = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
  
        addMouseListener(this);
        addMouseMotionListener(this);
        //controller.algorithm.findPossibleMoves(algorithm.board, algorithm.currentPlayerColor, algorithm.blackDisks, algorithm.whiteDisks);
    }

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
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
        drawBoard(g2d);
    	//drawFigures(g2d);  	
    	//paintPossibleMovesSquares(g2d, algorithm.possibleMovesPoints);
    	drawInfo(g2d);
    }    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
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
    
    private void placeImagesOnBoard() {
    	ArrayList<Figure> allFigures = new ArrayList<Figure>();
    	allFigures.addAll(controller.getBlackFigures());
    	allFigures.addAll(controller.getWhiteFigures());

    	for (Figure figure : allFigures) {
    		figure.getFigureImage().setBounds(figure.getPoint().x * SQUARE_SIZE, 
    				(NUMBER_OF_SQUARES - figure.getPoint().y - 1) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            this.add(figure.getFigureImage());
    	}
    	
	}
    
    // function used in mouse clicked event, it returns point representing square pressed by mouse
    private Point boardSquarePoint(Point mousePoint) {       
        return new Point(mousePoint.x / SQUARE_SIZE, NUMBER_OF_SQUARES - mousePoint.y / SQUARE_SIZE - 1);
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
    	System.out.println("mouse pressed");
    	System.out.println(this.getComponentAt(event.getPoint()));
    	if(this.getComponentAt(event.getPoint()) instanceof JLabel) {
    		System.out.println("JLabel pressed");
    		Point pressedBoardPoint = boardSquarePoint(event.getPoint());
    		draggedFigure = controller.getBoard()[pressedBoardPoint.x][pressedBoardPoint.y];
    	}
    	else if(this.getComponentAt(event.getPoint()) instanceof BoardPanel)
    		System.out.println("BoardPanel pressed");
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
    	System.out.println("mouse released");
    	if(draggedFigure != null) {
    		controller.getBoard()[draggedFigure.getPoint().x][draggedFigure.getPoint().y] = null;
    		Point boardPoint = boardSquarePoint(event.getPoint());
    		draggedFigure.setPoint(boardPoint);
    		controller.getBoard()[boardPoint.x][boardPoint.y] = draggedFigure;	
    	}
    }
    
    @Override
    public void mouseEntered(MouseEvent event) {
    }
    
    @Override
    public void mouseExited(MouseEvent event) {
    }
    
    @Override
    public void mouseClicked(MouseEvent event) {
    	Point squarePoint = boardSquarePoint(event.getPoint());
    	
    	//(if(algorithm.possibleMovesPoints.contains(squarePoint)) {
    	//	algorithm.movePlayerColor(squarePoint);
    	//}
    }

	@Override
	public void mouseDragged(MouseEvent event) {
		System.out.println("mouse dragged");
		if(draggedFigure != null)
			draggedFigure.getFigureImage().setBounds(event.getX() - SQUARE_SIZE / 2, event.getY() - SQUARE_SIZE / 2, SQUARE_SIZE, SQUARE_SIZE);
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		System.out.println("mouse moved");	
	}       
}
