package model

import java.awt.Point
import javax.swing.JLabel
import model.FigureType._
import model.PlayerColor._

case class Figure(figureType : FigureType, playerColor : PlayerColor, var point : Point, var figureImage : JLabel) {

  def getFigureImage: JLabel = figureImage
  def setFigureImage(figureImage: JLabel): Unit = this.figureImage = figureImage
  def getPoint: Point = point
  def setPoint(point: Point): Unit = this.point = point
}
