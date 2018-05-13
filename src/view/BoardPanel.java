package view;

import controller.Controller;
import model.Figure;

import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import model.PlayerColor;
import model.PlayerColor$;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.immutable.Vector;
import scala.Array;

import static model.Constants.*;

public class BoardPanel extends JPanel {

    private Color brightFieldColor = new Color(196, 200, 190);
    private Color darkFieldColor = new Color(125, 40, 15);
    private Color possibleMoveFieldColor = new Color(144, 150, 98);
    private Color selectedFieldColor = new Color(14, 150, 0);

    private static Controller controller;

    private Figure draggedFigure = null;

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

/*    class getMovesRunnable implements Runnable {

        int x;
        int y;
        getMovesRunnable(int _x, int _y){
            x = _x;
            y = _y;
        }
        public void run(){
            possibleMoves = JavaConverters.asJavaIterable(controller.getMoves(y, x));
            try {
                SwingUtilities.invokeAndWait(new Runnable(){
                    public void run(){
                        possibleMoves.forEach(field -> board[(int)field._1()][(int)field._2()].setBackground(possibleMoveFieldColor));
                    }
                });
                return;
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }*/

    public BoardPanel(Controller _c) {
        controller = _c;
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
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

                            if(possibleMoves != null && possibleMoves.contains(new scala.Tuple2(newSelected.x, newSelected.y))){
                                controller.makePlayerMove(selected.figure, new scala.Tuple2(newSelected.x, newSelected.y));
                                controller.makeComputerMove();
								repaintFigures();
								possibleMoves = null;
								selected = null;
                                return;
                            }
                            if (selected != null) {
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
        JButton pomoc = new JButton();
        pomoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaintFigures();
                System.out.println("odswiezam widok");
            }
        });
        pomoc.setPreferredSize(new Dimension(30, 30));
        pomoc.setVisible(true);
        pomoc.setEnabled(true);
        add(pomoc);
    }
    public Figure getFigure(int x, int y){
        return board[x][y].figure;
    }

    public void repaintFigures(){

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j].removeAll();
                board[i][j].resetColor();
                board[i][j].figure = null;
                board[i][j].setEnabled(false);
                //board[i][j].repaint();
            }
        }

        Figure [] blackFigures = controller.getBlackFigures();
        for(Figure f : blackFigures){
            if(f == null)
                continue;
            if(controller.playerColor() == PlayerColor.Black())
                board[f.x()][f.y()].setEnabled(true);
            else
                board[f.x()][f.y()].setEnabled(false);
            board[f.x()][f.y()].setFigure(f);
        }

        Figure [] whiteFigures = controller.getWhiteFigures();
        for(Figure f : whiteFigures){
            if(f == null)
                continue;
            if(controller.playerColor() == PlayerColor.White())
                board[f.x()][f.y()].setEnabled(true);
            else
                board[f.x()][f.y()].setEnabled(false);
            board[f.x()][f.y()].setFigure(f);
        }
        this.repaint();
    }
}
