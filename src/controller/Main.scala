package controller

import java.awt.FlowLayout

import javax.swing._
import model.PlayerColor
import view.BoardPanel

/**
  * Entry point to the application.
  *
  * Creates Controller and shows the main windows of the app.
  */

object Main extends App {
  val chooseColor = JOptionPane.showConfirmDialog(null,
    "Do you want to play white figures?", "Figures color", JOptionPane.YES_NO_OPTION)
  var controller : Controller = null
  if (chooseColor == JOptionPane.YES_OPTION)
    controller = new Controller(PlayerColor.White)
  else
    controller = new Controller(PlayerColor.Black)
  val boardPanel = new BoardPanel(controller)
  controller.setBoardPanel(boardPanel)
  boardPanel.setVisible(true)
  if(chooseColor == JOptionPane.NO_OPTION)
    controller.makeComputerMove()
}