package de.htwg.se.roguelike.aview.gui

import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage

import de.htwg.se.roguelike.aview.tui.State
import de.htwg.se.roguelike.controller.{Controller, GameStatus}
import de.htwg.se.roguelike.model._
import javax.swing.{BoxLayout, ListSelectionModel}

import scala.swing.event.{ButtonClicked, SelectionChanged}
import scala.swing.{BorderPanel, BoxPanel, Button, Component, Dimension, FlowPanel, ListView, Orientation, Panel, ScrollBar, ScrollPane}

case class guiLootEnemy(controller: Controller, gui: SwingGui) extends StateGui {
  override def processInputLine(input: String): Unit = {
    input match {
      case "q" =>
      case "x" => controller.setGameStatus(GameStatus.LEVEL)
      case _ =>
        input.toList.filter(c => c != ' ').filter(_.isDigit).map(c => c.toString.toInt) match {
          case index :: Nil => controller.lootingEnemy(index)
          case _ =>
        }
    }
  }

  override def handle(): Unit = {
    val e = controller.gameStatus
    e match {
      case GameStatus.LEVEL => gui.state = new guiLevel(controller, gui)
      case GameStatus.LOOTENEMY => gui.state = this
      case _ =>
        print("Wrong GameStatus!!!")
    }
  }

  override def drawPanel(SCALE: Int): Panel = {

    var enemyItems = new ListView(controller.enemyLoot)

    val panel = new FlowPanel() {
      preferredSize = new Dimension(256 * SCALE, 144 * SCALE)
      peer.setLayout(null)

      val scrollBar = new ScrollPane(enemyItems)
      scrollBar.peer.setBounds(0, 0, 128 * SCALE, 144 * SCALE)

      enemyItems.peer.setBounds(0, 0, 128 * SCALE, 144 * SCALE)
      listenTo(enemyItems.selection)
      contents += scrollBar

      val lootButton = new Button("Loot")//vll noch texture
      listenTo(lootButton)
      lootButton.peer.setBounds(128 * SCALE, 64 * SCALE, 128 * SCALE, 20 * SCALE)
      contents += lootButton

      val lootAllButton = new Button("Loot All")
      listenTo(lootAllButton)
      lootAllButton.peer.setBounds(128 * SCALE, 94 * SCALE, 128 * SCALE, 20 * SCALE)
      contents += lootAllButton

      val exitButton = new Button("Exit")
      listenTo(exitButton)
      exitButton.peer.setBounds(128 * SCALE, 124 * SCALE, 128 * SCALE, 20 * SCALE)
      contents += exitButton


      reactions += {
        case ButtonClicked(lb) if lb == lootButton => controller.lootingEnemy(enemyItems.peer.getSelectedIndex + 1)
        case ButtonClicked(lab) if lab == lootAllButton => controller.lootAll()
        case ButtonClicked(e) if e == exitButton => controller.setGameStatus(GameStatus.LEVEL)
        case SelectionChanged(_) => controller.repaint()
      }

      override def paintComponent(g: Graphics2D): Unit = {

        val backgroundSpriteSheet = new SpriteSheet("resources/16bitSpritesBackground.png")
        val errorTexture = backgroundSpriteSheet.getSprite(32, 16, 16)

        g.setColor(Color.WHITE)
        g.fillRect(192 * SCALE, 0 * SCALE, 64 * SCALE, 64 * SCALE)
        /*
        g.setColor(Color.BLACK)
        g.setFont(new Font("TimesRoman", Font.BOLD, 5 * SCALE))
        enemyItems.peer.getSelectedValue match {
          case armor: Armor => g.drawString("DEF: " + armor.armor, 222 * SCALE, 0 * SCALE)
          case weapon: Weapon => g.drawString("ATK: " + weapon.dmg, 202 * SCALE, 0 * SCALE)
          case potion: Potion => potion match {
            case hp:HealPotion => g.drawString("HP: " + hp.power, 202 * SCALE, 0 * SCALE)
            case mp:ManaPotion => g.drawString("MP: " + mp.power, 202 * SCALE, 0 * SCALE)
          }
          case _ => g.fillRect(202 * SCALE, 0 * SCALE,100,100)
        }
        //geht nict ka zeichnet den string nicht
        */
        enemyItems.peer.getSelectedValue match {
          case armor: Armor => armor match {
            case helm: Helmet => g.drawImage(getTexture(helm.textureIndex, "resources/HelmTextures.png"), 128 * SCALE, 0 * SCALE, 64 * SCALE, 64 * SCALE, null)
            case chest: Chest => g.drawImage(getTexture(chest.textureIndex, "resources/ChestTextures.png"), 128 * SCALE, 0 * SCALE, 64 * SCALE, 64 * SCALE, null)
            case pants: Pants => g.drawImage(getTexture(pants.textureIndex, "resources/PantsTextures.png"), 128 * SCALE, 0 * SCALE, 64 * SCALE, 64 * SCALE, null)
            case boots: Boots => g.drawImage(getTexture(boots.textureIndex, "resources/BootsTextures.png"), 128 * SCALE, 0 * SCALE, 64 * SCALE, 64 * SCALE, null)
            case gloves: Gloves => g.drawImage(getTexture(gloves.textureIndex, "resources/GlovesTextures.png"), 128 * SCALE, 0 * SCALE, 64 * SCALE, 64 * SCALE, null)
          }
          case weapon: Weapon => g.drawImage(getTexture(weapon.textureIndex, "resources/WeaponTextures.png"), 128 * SCALE, 0 * SCALE, 64 * SCALE, 64 * SCALE, null)
          case potion: Potion => g.drawImage(getTexture(potion.textureIndex, "resources/PotionTextures.png"), 128 * SCALE, 0 * SCALE, 64 * SCALE, 64 * SCALE, null)
          case _ => {
            g.drawImage(errorTexture, 128 * SCALE, 0 * SCALE, 64 * SCALE, 64 * SCALE, null)
            println("NichtsAusgewählt")
          }
        }

      }

      def getTexture(index: Int, path: String): BufferedImage = {
        val weaponTextures = new SpriteSheet(path)
        val x = index % 5
        val y = index / 5
        weaponTextures.getSprite(32 * x, 32 * y, 32)
      }
    }
    panel
  }
}
