package view;

import controller.Controller;
import model.Figure;

import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

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
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = new Field(j, i);
                board[i][j].setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                board[i][j].setVisible(true);
                board[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(playersMove) {

                            Field newSelected = (Field)e.getSource();
                            if(possibleMoves != null && possibleMoves.contains(new scala.Tuple2(newSelected.y, newSelected.x))){
                                newSelected.figure = selected.figure;
                                newSelected.add(selected.figure.getFigureImage());
                                newSelected.figure = selected.figure;
                                selected.remove(selected.figure.getFigureImage()); // IconImage
                                selected.figure = null;
                                selected.resetColor();
                                selected = null;
                                possibleMoves.forEach(field -> {
                                    board[(int)field._1()][(int)field._2()].resetColor();
                                    board[(int)field._1()][(int)field._2()].setEnabled(false);
                                });
                                JavaConverters.asJavaCollection(controller.getPlayersFigures()).forEach(figure -> {
                                    board[figure.y()][figure.x()].setEnabled(false);
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

                            possibleMoves = possibleMoves = JavaConverters.asJavaCollection(controller.getMoves(selected.y, selected.x));
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

        Vector<Figure> v = controller.getBlackFigures();
        Collection<Figure> c = JavaConverters.asJavaCollection(v);
        for(Figure f : c){
            System.out.println(f.x() + " " + f.y() + "\n");
            if(controller.playerColor() == PlayerColor.Black())
                board[f.y()][f.x()].setEnabled(true);
            board[f.y()][f.x()].setFigure(f);
            board[f.y()][f.x()].add(f.getFigureImage());
        }

        v = controller.getWhiteFigures();
        c = JavaConverters.asJavaCollection(v);
        for(Figure f : c){
            System.out.println(f.x() + " " + f.y() + "\n");
            if(controller.playerColor() == PlayerColor.White())
                board[f.y()][f.x()].setEnabled(true);
            board[f.y()][f.x()].setFigure(f);
            board[f.y()][f.x()].add(f.getFigureImage());
        }
        if(controller.playerColor() == PlayerColor.White())
            playersMove = true;

    }
}