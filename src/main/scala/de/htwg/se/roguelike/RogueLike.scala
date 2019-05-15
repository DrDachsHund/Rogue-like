package de.htwg.se.roguelike

import de.htwg.se.roguelike.aview._
import de.htwg.se.roguelike.controller.Controller
import de.htwg.se.roguelike.model.{Enemy, Level, Player}

import scala.io.StdIn.readLine

object RogueLike {

  val controller = new Controller(player = new Player(name = "Player",posX = 5, posY = 5)
    ,enemies = Vector(new Enemy(name = "TestE1",posX = 0, posY = 0),
    new Enemy(name = "TestE2",posX = 1, posY = 0),
    new Enemy(name = "TestE3",posX = 0, posY = 1)),
    level = new Level(10))
  var tui:Tui = new Tui(controller)

  controller.notifyObservers


  def main(args: Array[String]): Unit = {
    var input: String = ""
    if (args.length != 0) {
      input = args(0)
    }
    if (!input.isEmpty) tui.strategy.tui(input)
    else do {
      input = readLine()
      tui.strategy.tui(input)
    } while (input != "q")
  }
}
