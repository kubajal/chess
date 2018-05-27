package view;

import controller.Controller;
import model.Figure;

import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import model.PlayerColor;
import scala.Tuple2;
import scala.collection.JavaConverters;
import static model.Constants.*;

public class BoardPanel extends JPanel {

    private Color brightFieldColor = new Color(196, 200, 190);
    private Color darkFieldColor = new Color(125, 40, 15);
    private Color possibleMoveFieldColor = new Color(144, 150, 98);
    private Color selectedFieldColor = new Color(14, 150, 0);
    private Color yellow = new Color(150, 135, 21);

    private static Controller controller;

    private Figure draggedFigure = null;

    class moveRunnable implements Runnable {
        public Controller c;
        moveRunnable(Controller _c){
            c = _c;
        }

        @Override
        public void run() {
            c.getMainWindow().getBoardPanel().getAIButtonm().setEnabled(false);
            c.makeComputerMove();
            c.getMainWindow().getBoardPanel().getAIButtonm().setEnabled(true);
        }
    }

    public class AIThread extends Thread{

        public AIRunnable airunnable;
        Controller controller;

        AIThread(AIRunnable _airunnable){
            super(_airunnable);
            airunnable = _airunnable;
        }

        void setFlag(Boolean _f){
            airunnable.setRunFlag(_f);
        }
    }

    public class AIRunnable implements Runnable {

        public Boolean flag = true;
        public Controller c;

        AIRunnable(Controller _c){
            c = _c;
        }

        public void setRunFlag(Boolean _f){
            flag = _f;
        }

        @Override
        public void run() {
            while(true) {
                if(flag == true)
                    c.computerVsComputer();
                if(flag == true)
                    c.getMainWindow().getBoardPanel().repaintFigures();
                if(flag == false)
                    break;
            }
            c.getMainWindow().getBoardPanel().getAIButtonm().setEnabled(true);
            c.getMainWindow().getBoardPanel().getAIButtonm().setBackground(null);
            c.getMainWindow().getBoardPanel().enableFigures();

        }
    }

    AIRunnable computerVsComputerRunnable = null;
    moveRunnable computerMoveRunnable = null;
    Thread aithread = null;

    class Field extends JButton {

        public Figure figure;
        public Integer x;
        public Integer y;
        void setFigure(Figure f){
            figure = f;
            removeAll();
            add(f.getFigureImage());
        }
        Field(int _x, int _y){
            x = _x;
            y = _y;
        }

        public void resetColor(){

            if ((x + y) % 2 == 0)
                setBackground(brightFieldColor);
            else
                setBackground(darkFieldColor);
        }
    }

    private Boolean playersMove = false;
    private Field selected = null;

    private Collection<Tuple2<Object, Object>> possibleMoves;

    private Field[][] board;

    private JButton AIbutton;

    public  JButton getAIButtonm() {
        return AIbutton;
    }

    public BoardPanel(Controller _c) {
        controller = _c;
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        board = new Field[8][8];
        for(int j = 7; j >= 0; j--){
            for(int i = 0; i < 8; i++){
                board[i][j] = new Field(i, j);
                board[i][j].setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                board[i][j].setVisible(true);
                board[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(controller.isPlayersMove()) {

                            Field newSelected = (Field)e.getSource();

                            if(possibleMoves != null && possibleMoves.contains(new Tuple2(newSelected.x, newSelected.y))){
                                controller.makePlayerMove(selected.figure, new Tuple2(newSelected.x, newSelected.y));
								 repaintFigures();
								 enableFigures();
								 possibleMoves = null;
								 selected = null;
								 new Thread(new moveRunnable(controller)).start();
                                return;
                            }
                            if(selected != null) {
                                selected.resetColor();
                                possibleMoves.forEach(field -> {
                                    board[(int)field._1()][(int)field._2()].resetColor();
                                    board[(int)field._1()][(int)field._2()].setEnabled(false);
                                });
                            }
                            selected = (Field) e.getSource();
                            selected.setBackground(selectedFieldColor);
                            System.out.println("x " + selected.x + ", y " + selected.y + "\n");

                            possibleMoves = JavaConverters.asJavaCollection(controller.findPossibleMoves(selected.figure));
                            possibleMoves.forEach(field -> {
                                board[(int)field._1()][(int)field._2()].setBackground(possibleMoveFieldColor);
                                board[(int)field._1()][(int)field._2()].setEnabled(true);
                                }
                            );
                        }
                    }
                });
                board[i][j].resetColor();
                board[i][j].setEnabled(false);
                this.add(board[i][j]);
            }
        }
        repaintFigures();
        enableFigures();
        AIbutton = new JButton();
        aithread = new AIThread(new AIRunnable(controller));
        AIbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton b = ((JButton)e.getSource());
                if(computerVsComputerRunnable == null){
                    repaintFigures();
                    b.removeAll();
                    AIbutton.setText("Zatrzymaj AI");
                    b.repaint();
                    disableFigures();
                    computerVsComputerRunnable = new AIRunnable(controller);
                    aithread = new Thread(computerVsComputerRunnable);
                    aithread.start();
                }
                else {
                    b.removeAll();
                    AIbutton.setText("Zacznij gre komputer vs komputer");
                    b.setBackground(yellow);
                    computerVsComputerRunnable.setRunFlag(false);
                    computerVsComputerRunnable = null;
                    AIbutton.setEnabled(false);
                }
            }
        });
        AIbutton.setText("Zacznij gre komputer vs komputer");
        AIbutton.setPreferredSize(new Dimension(300, 50));
        AIbutton.setVisible(true);
        AIbutton.setEnabled(true);
        add(AIbutton);
    }
    public Figure getFigure(int x, int y){
        return board[x][y].figure;
    }

    public void disableFigures() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j].setEnabled(false);
            }
        }
    }

    public void enableFigures() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                Figure [] blackFigures = controller.getBlackFigures();
                Figure [] whiteFigures = controller.getWhiteFigures();

                for(Figure f : whiteFigures){
                    if(f == null)
                        continue;
                    if(controller.playerColor() == PlayerColor.White())
                        board[f.x()][f.y()].setEnabled(true);
                    else
                        board[f.x()][f.y()].setEnabled(false);
                }

                for(Figure f : blackFigures){
                    if(f == null)
                        continue;
                    if(controller.playerColor() == PlayerColor.Black())
                        board[f.x()][f.y()].setEnabled(true);
                    else
                        board[f.x()][f.y()].setEnabled(false);
                }
            }
        }
    }

    public void repaintFigures(){

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j].removeAll();
                board[i][j].resetColor();
                board[i][j].figure = null;
                board[i][j].setEnabled(false);
                board[i][j].repaint();
            }
        }

        Figure [] blackFigures = controller.getBlackFigures();
        Figure [] whiteFigures = controller.getWhiteFigures();

        for(Figure f : whiteFigures){
            if(f != null)
                board[f.x()][f.y()].setFigure(f);
        }

        for(Figure f : blackFigures){
            if(f != null)
                board[f.x()][f.y()].setFigure(f);
        }

        this.repaint();
    }

	public void displayGameOverInfo(String gameOverInfoString) {
		JLabel gameOverLabel = new JLabel(gameOverInfoString);
		repaintFigures();
		this.add(gameOverLabel);
		gameOverLabel.repaint();
		this.repaint();
	}
}
