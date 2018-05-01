package view;

import controller.Controller;
import model.Figure;

import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;

import model.PlayerColor;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.immutable.Vector;

import static model.Constants.SQUARE_SIZE;


public class BoardPanel extends JPanel {
	
	public static final long serialVersionUID = -7729510720848698723L; // kod seryjny klasy JPanel
	
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

    private Iterable<Tuple2<Object, Object>> possibleMoves;

    private Field[][] board;

    class getMovesRunnable implements Runnable {

        int x;
        int y;
        getMovesRunnable(int _x, int _y){
            x = _x;
            y = _y;
        }
        public void run(){
            possibleMoves = JavaConverters.asJavaIterable(controller.getMoves(5, 5));
            try {
                SwingUtilities.invokeAndWait(new Runnable(){
                    public void run(){
                        possibleMoves.forEach(field -> board[(int)field._1()][(int)field._2()].setBackground(possibleMoveFieldColor));
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public BoardPanel(Controller _c) {
        possibleMoves = new Iterable<Tuple2<Object, Object>>() {
            @Override
            public Iterator<Tuple2<Object, Object>> iterator() {
                return null;
            }
        };
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
                        Field newSelected = (Field) e.getSource();
                        if (selected != null) {
                            selected.resetColor();
                            return;
                        }
                        newSelected.setBackground(selectedFieldColor);
                        selected = board[newSelected.y][newSelected.x];
                        System.out.println(newSelected.x + "x y" + newSelected.y + "\n");

                        System.out.println("nacisnieto");
                        if (playersMove == true) {
                            new Thread(new getMovesRunnable(selected.x, selected.y)).start();

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
}
