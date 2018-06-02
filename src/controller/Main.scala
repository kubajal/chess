package controller

import model.PlayerColor.PlayerColor
import view.MainWindow

/**
  * Entry point to the application.
  *
  * Creates Controller and
  */

object Main extends App {
  val controller = new Controller()
  val mainWindow = new MainWindow(controller)
  controller.setMainWindow(mainWindow)
  mainWindow.showIntroPanel()
}