package de.htwg.se.roguelike.aview.gui

import java.awt.Graphics2D
import java.awt.image.BufferedImage

import de.htwg.se.roguelike.aview.State
import de.htwg.se.roguelike.controller.{Controller, GameStatus}
import javax.imageio.ImageIO

import scala.swing.{Button, Dimension, Panel}

case class guiFight(controller: Controller, gui: SwingGui) extends StateGui {
  override def processInputLine(input: String): Unit = {
    input match {
      case "q" =>
      case "1" => controller.attack()
      case "2" => controller.block()
      case "3" => controller.special()
      case "r" => controller.run()
      case "i" =>
        controller.setGameStatus(GameStatus.INVENTORY)
      //tui.inventoryGameStatus = GameStatus.FIGHT
      case _ =>
        print("Wrong Input!!!")
    }
  }

  override def handle(): Unit = {
    val e = controller.gameStatus
    e match {
      case GameStatus.LEVEL => gui.state = new guiMain(controller, gui)
      case GameStatus.FIGHT => gui.state = this
      case GameStatus.FIGHTSTATUS => gui.state = this
      case GameStatus.INVENTORY => gui.state = new guiInventoryMain(controller, gui)
      case GameStatus.GAMEOVER => gui.state = new guiGameOver(controller, gui)
      case GameStatus.PLAYERLEVELUP => gui.state = new guiPlayerLevelUp(controller, gui)
      case GameStatus.LOOTENEMY => gui.state = new guiLootEnemy(controller, gui)
      case _ =>
        print("Wrong GameStatus!!!")
    }
  }

  override def drawPanel(SCALE: Int): Panel = {
    //val img = ImageIO.read(getClass.getResource("Test.png"))
    val panel = new Panel {

      //ersma so aber eig eigene texturen für fihgt
      val playerSpriteSheet = new SpriteSheet("Player.png")
      val fightSpriteSheet = new SpriteSheet("Fight.png")
      val fightBackgroundSpriteSheet = new SpriteSheet("FightBackground1.png")
      val enemiesSpriteSheet = new SpriteSheet("Enemy.png")

      val playerTexture = playerSpriteSheet.getSprite(16, 0)
      val enemyTextureBlue = enemiesSpriteSheet.horizontalFlip(enemiesSpriteSheet.getSprite(0, 32)) //zum flippen vll in eigene klasse?!?!?!?
      val fight = fightSpriteSheet.sheet
      val fightBackground = fightBackgroundSpriteSheet.sheet

      val canvas = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
      val g = canvas.createGraphics()
      preferredSize = new Dimension(256 * SCALE, 144 * SCALE + 20)

      override def paint(g: Graphics2D): Unit = {

        g.drawImage(fightBackground, 0, 0, 256 * SCALE, 144 * SCALE, null)
        g.drawImage(playerTexture, 10 * SCALE, 60 * SCALE, 32 * SCALE, 32 * SCALE, null)
        g.drawImage(enemyTextureBlue, 210 * SCALE, 60 * SCALE, 32 * SCALE, 32 * SCALE, null)
        g.drawImage(fight, 0, 0, 256 * SCALE, 144 * SCALE, null)

      }

      repaint()
    }
    panel
  }

}