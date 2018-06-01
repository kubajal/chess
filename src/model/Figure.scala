package model

import javax.swing.JLabel
import model.FigureType._
import model.PlayerColor._

case class Figure(var figureType : FigureType, color : PlayerColor, var x : Int, var y: Int, 
	var figureImage : JLabel, var moved : Boolean = false) {

  def getFigureImage: JLabel = figureImage
  def setFigureImage(figureImage: JLabel): Unit = this.figureImage = figureImage
  def getPoint: (Int, Int) = (x, y)
  def getX: Int = x
  def getY: Int = y
  def setPoint(_x: Int, _y: Int): Unit = {
    this.x = _x
    this.y = _y
  }
  def XY() : (Int, Int) = (x, y)
  def getType(): FigureType = figureType
  def getColor(): PlayerColor = color
	def hasMoved(): Boolean = moved
  def setMoved(moved : Boolean) : Unit = this.moved = moved
	def setFigureType(figureType : FigureType) : Unit = this.figureType = figureType
}
