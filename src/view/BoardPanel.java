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
import scala.collection.immutable.Vector;

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
    private Field selected;

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
        for(int j = 0; j < 8; j++){
            for(int i = 0; i < 8; i++){
                board[i][j] = new Field(i, j);
                board[i][j].setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                board[i][j].setVisible(true);
                board[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(playersMove) {

                            Field newSelected = (Field)e.getSource();
                            if(possibleMoves != null && possibleMoves.contains(new scala.Tuple2(newSelected.x, newSelected.y))){
                                controller.move(new scala.Tuple2(selected.x, selected.y), new scala.Tuple2(newSelected.x, newSelected.y));
                                newSelected.figure = selected.figure;
                                newSelected.add(selected.figure.getFigureImage());
                                selected.remove(selected.figure.getFigureImage()); // IconImage
                                selected.figure = null;
                                selected.resetColor();
                                selected = null;
                                possibleMoves.forEach(field -> {
                                    board[(int)field._1()][(int)field._2()].resetColor();
                                    board[(int)field._1()][(int)field._2()].setEnabled(false);
                                });
                                JavaConverters.asJavaCollection(controller.getPlayersFigures()).forEach(figure -> {
                                    board[figure.x()][figure.y()].setEnabled(false);
                                });
                                playersMove = false;
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
                            System.out.println(selected.x + "x y" + selected.y + "\n");

                            possibleMoves = JavaConverters.asJavaCollection(controller.getMoves(selected.x, selected.y));
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

        Vector<Figure> v = controller.getPlayersFigures();
        Collection<Figure> c = JavaConverters.asJavaCollection(v);
        for(Figure f : c){
            board[f.x()][f.y()].setEnabled(true);
            board[f.x()][f.y()].setFigure(f);
            board[f.x()][f.y()].add(f.getFigureImage());
        }

        v = controller.getOpponentsFigures();
        c = JavaConverters.asJavaCollection(v);
        for(Figure f : c){
            board[f.x()][f.y()].setFigure(f);
            board[f.x()][f.y()].add(f.getFigureImage());
        }
        if(controller.playerColor() == PlayerColor.White())
            playersMove = true;

    }
    public Figure getFigure(int x, int y){
        return board[x][y].figure;
    }

    public void enablePlayersMove(){
        playersMove = true;
    }

    public void repaintFigures() {

        for(Figure f : JavaConverters.asJavaCollection(controller.getOpponentsFigures())){
            board[f.x()][f.y()].removeAll();
            board[f.x()][f.y()].add(f.getFigureImage());
        }
        for(Figure f : JavaConverters.asJavaCollection(controller.getPlayersFigures())){
            board[f.x()][f.y()].removeAll();
            board[f.x()][f.y()].add(f.getFigureImage());
            board[f.x()][f.y()].setEnabled(true);
        }
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j].resetColor();
            }
        }
    }
}