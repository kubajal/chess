package model

import javax.swing.JLabel
import model.FigureType._
import model.PlayerColor._

case class Figure(figureType : FigureType, playerColor : PlayerColor, var x : Int, var y: Int, var figureImage : JLabel) {

  def getFigureImage: JLabel = figureImage
  def setFigureImage(figureImage: JLabel): Unit = this.figureImage = figureImage
  def getPoint: (Int, Int) = (x, y)
  def setPoint(_x: Int, _y: Int): Unit = {
    this.x = _x
    this.y = _y;
  }
}