package controller

import model.PlayerColor.PlayerColor
import view.MainWindow

object Main extends App {
  val controller = new Controller()
  val mainWindow = new MainWindow(controller)
  controller.setMainWindow(mainWindow)
  mainWindow.showIntroPanel()
}