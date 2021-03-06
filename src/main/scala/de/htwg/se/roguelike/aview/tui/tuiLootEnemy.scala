package de.htwg.se.roguelike.aview.tui

import de.htwg.se.roguelike.controller.{ControllerInterface, GameStatus}
import de.htwg.se.roguelike.controller.controllerBaseImpl.Controller

case class tuiLootEnemy(controller: ControllerInterface, tui: Tui) extends State {
  def processInputLine(input: String): Unit = {
    input match {
      case "q" =>
      case "x" => controller.setGameStatus(GameStatus.LEVEL)
      case _ =>
        input.toList.filter(c => c != ' ').filter(_.isDigit).map(c => c.toString.toInt) match {
          case index :: Nil => controller.lootingEnemy(index)
          case _ =>
        }
    }
    handle()
  }

  override def handle(): Unit = {
    val e = controller.gameStatus
    e match {
      case GameStatus.LEVEL => tui.state = new tuiLevel(controller, tui)
      case GameStatus.LOOTENEMY => tui.state = this
      case _ =>
        print("Wrong GameStatus!!!")
    }
  }
}
