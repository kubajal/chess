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
    }

    private Boolean playersMove = false;
    private Field selected;

    private Field[][] board;

    public BoardPanel(Controller controller) {
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
                        if(playersMove == true){
                            Field newSelected = (Field)e.getSource();
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
}
