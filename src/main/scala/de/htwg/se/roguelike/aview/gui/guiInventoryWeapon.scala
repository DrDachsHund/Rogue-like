package de.htwg.se.roguelike.aview.gui

import de.htwg.se.roguelike.aview.State
import de.htwg.se.roguelike.controller.{Controller, GameStatus}

import scala.swing.Panel

case class guiInventoryWeapon(controller: Controller, gui: SwingGui) extends StateGui {
  override def processInputLine(input: String): Unit = {
    input match {
      case "x" => controller.setGameStatus(GameStatus.INVENTORY)
      case "R" => controller.unEquipRightHand()
      case "L" => controller.unEquipLeftHand()
      case "q" =>
      case _ =>
        input.toList.filter(c => c != ' ').filter(_.isDigit).map(c => c.toString.toInt) match { //shaumamal
          case hand :: index :: Nil => controller.equipWeapon(hand, index)
          case _ =>
        }
    }
  }

  override def handle(): Unit = {
    val e = controller.gameStatus
    e match {
      case GameStatus.INVENTORYWEAPON => gui.state = this
      case GameStatus.INVENTORY => gui.state = new guiInventoryMain(controller, gui)
      case _ =>
        print("Wrong GameStatus!!!")
    }
  }

  override def drawPanel(Scale: Int): Panel = ???
}
