package de.htwg.se.roguelike.aview.gui

import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage

import de.htwg.se.roguelike.aview.tui.State
import de.htwg.se.roguelike.controller.{ControllerInterface, GameStatus}
import de.htwg.se.roguelike.controller.controllerBaseImpl.Controller
import de.htwg.se.roguelike.model._
import de.htwg.se.roguelike.model.levelComponent.{ArmorInterface, ItemInterface, PotionInterface, WeaponInterface}
import de.htwg.se.roguelike.model.levelComponent.levelBaseImpl._
import javax.swing.ImageIcon

import scala.swing.event.{ButtonClicked, SelectionChanged}
import scala.swing.{Button, Dimension, FlowPanel, Panel}

case class guiInventoryMain(controller: ControllerInterface, gui: SwingGui) extends StateGui {
  override def processInputLine(input: String): Unit = {
    input match {
      case "1" => controller.setGameStatus(GameStatus.INVENTORYPOTION)
      case "2" => controller.setGameStatus(GameStatus.INVENTORYWEAPON)
      case "3" => controller.setGameStatus(GameStatus.INVENTORYARMOR)

      case "H" => controller.unEquipHelmet()
      case "C" => controller.unEquipChest()
      case "P" => controller.unEquipPants()
      case "B" => controller.unEquipBoots()
      case "G" => controller.unEquipGloves()

      case "R" => controller.unEquipRightHand()
      case "L" => controller.unEquipLeftHand()


      case "x" => controller.setGameStatus(controller.inventoryGameStatus)
      case "q" =>
      case _ =>
        print("Wrong Input!!!")

    }
  }

  override def handle(): Unit = {
    val e = controller.gameStatus
    e match {
      case GameStatus.INVENTORY => gui.state = this
      case GameStatus.LEVEL => gui.state = new guiLevel(controller, gui)
      case GameStatus.FIGHT => gui.state = new guiFight(controller, gui)
      case GameStatus.INVENTORYPOTION => gui.state = new guiInventoryPotion(controller, gui)
      case GameStatus.INVENTORYARMOR => gui.state = new guiInventoryArmor(controller, gui)
      case GameStatus.INVENTORYWEAPON => gui.state = new guiInventoryWeapon(controller, gui)
      case _ =>
        print("Wrong GameStatus!!!")
    }
  }

  override def drawPanel(SCALE: Int): Panel = {

    val potionButtonImage = new SpriteSheet("resources/potionButtonIcon.png")
    val potionIcon = new ImageIcon(potionButtonImage.getImage().getScaledInstance(128 * SCALE, 20 * SCALE, java.awt.Image.SCALE_SMOOTH))

    val weaponButtonImage = new SpriteSheet("resources/weaponButtonIcon.png")
    val weaponIcon = new ImageIcon(weaponButtonImage.getImage().getScaledInstance(128 * SCALE, 20 * SCALE, java.awt.Image.SCALE_SMOOTH))

    val armorButtonImage = new SpriteSheet("resources/armorButtonIcon.png")
    val armorIcon = new ImageIcon(armorButtonImage.getImage().getScaledInstance(128 * SCALE, 20 * SCALE, java.awt.Image.SCALE_SMOOTH))

    val exitButtonImage = new SpriteSheet("resources/exitButtonIcon.png")
    val exitIcon = new ImageIcon(exitButtonImage.getImage().getScaledInstance(128 * SCALE, 20 * SCALE, java.awt.Image.SCALE_SMOOTH))

    def getImageIcon(item: ItemInterface): ImageIcon = {
      item match {
        case armor: ArmorInterface => armor match {
          case helm: Helmet => getTexture(helm.textureIndex, "resources/HelmTextures.png")
          case chest: Chest => getTexture(chest.textureIndex, "resources/ChestTextures.png")
          case pants: Pants => getTexture(pants.textureIndex, "resources/PantsTextures.png")
          case boots: Boots => getTexture(boots.textureIndex, "resources/BootsTextures.png")
          case gloves: Gloves => getTexture(gloves.textureIndex, "resources/GlovesTextures.png")
        }
        case weapon: WeaponInterface => getTexture(weapon.textureIndex, "resources/WeaponTextures.png")
        case potion: PotionInterface => getTexture(potion.textureIndex, "resources/PotionTextures.png")
      }
    }

    def getTexture(index: Int, path: String): ImageIcon = {
      val textures = new SpriteSheet(path)
      val x = index % 5
      val y = index / 5
      new ImageIcon(textures.getSprite(32 * x, 32 * y, 32).getScaledInstance(30 * SCALE, 30 * SCALE, java.awt.Image.SCALE_SMOOTH))
    }


    val panel = new FlowPanel() {
      preferredSize = new Dimension(256 * SCALE, 144 * SCALE)
      peer.setLayout(null)


      val potionButton = new Button()
      potionButton.peer.setIcon(potionIcon)
      listenTo(potionButton)
      potionButton.peer.setBounds(0 * SCALE, 64 * SCALE, 128 * SCALE, 20 * SCALE)
      contents += potionButton

      val weaponButton = new Button()
      weaponButton.peer.setIcon(weaponIcon)
      listenTo(weaponButton)
      weaponButton.peer.setBounds(0 * SCALE, 84 * SCALE, 128 * SCALE, 20 * SCALE)
      contents += weaponButton

      val armorButton = new Button()
      armorButton.peer.setIcon(armorIcon)
      listenTo(armorButton)
      armorButton.peer.setBounds(0 * SCALE, 104 * SCALE, 128 * SCALE, 20 * SCALE)
      contents += armorButton

      val exitButton = new Button()
      exitButton.peer.setIcon(exitIcon)
      listenTo(exitButton)
      exitButton.peer.setBounds(0 * SCALE, 124 * SCALE, 128 * SCALE, 20 * SCALE)
      contents += exitButton

      //---RightSideCurrentEquipped

      val helmetButton = new Button()
      helmetButton.peer.setIcon(getImageIcon(controller.player.helmet))
      helmetButton.peer.setBounds(177 * SCALE, 5 * SCALE, 30 * SCALE, 30 * SCALE)
      listenTo(helmetButton)
      contents += helmetButton

      val chestButton = new Button()
      chestButton.peer.setIcon(getImageIcon(controller.player.chest))
      chestButton.peer.setBounds(177 * SCALE, 40 * SCALE, 30 * SCALE, 30 * SCALE)
      listenTo(chestButton)
      contents += chestButton

      val pantsButton = new Button()
      pantsButton.peer.setIcon(getImageIcon(controller.player.pants))
      pantsButton.peer.setBounds(177 * SCALE, 75 * SCALE, 30 * SCALE, 30 * SCALE)
      listenTo(pantsButton)
      contents += pantsButton

      val bootsButton = new Button()
      bootsButton.peer.setIcon(getImageIcon(controller.player.boots))
      bootsButton.peer.setBounds(177 * SCALE, 110 * SCALE, 30 * SCALE, 30 * SCALE)
      listenTo(bootsButton)
      contents += bootsButton

      val glovesButton = new Button()
      glovesButton.peer.setIcon(getImageIcon(controller.player.gloves))
      glovesButton.peer.setBounds(137 * SCALE, 5 * SCALE, 30 * SCALE, 30 * SCALE)
      listenTo(glovesButton)
      contents += glovesButton

      val weaponRightHandButton = new Button()
      weaponRightHandButton.peer.setIcon(getImageIcon(controller.player.rightHand))
      weaponRightHandButton.peer.setBounds(217 * SCALE, 40 * SCALE, 30 * SCALE, 30 * SCALE)
      listenTo(weaponRightHandButton)
      contents += weaponRightHandButton

      val weaponLeftHandButton = new Button()
      weaponLeftHandButton.peer.setIcon(getImageIcon(controller.player.leftHand))
      weaponLeftHandButton.peer.setBounds(137 * SCALE, 40 * SCALE, 30 * SCALE, 30 * SCALE)
      listenTo(weaponLeftHandButton)
      contents += weaponLeftHandButton

      reactions += {
        case ButtonClicked(pb) if pb == potionButton => controller.setGameStatus(GameStatus.INVENTORYPOTION)
        case ButtonClicked(wb) if wb == weaponButton => controller.setGameStatus(GameStatus.INVENTORYWEAPON)
        case ButtonClicked(ab) if ab == armorButton => controller.setGameStatus(GameStatus.INVENTORYARMOR)
        case ButtonClicked(e) if e == exitButton => controller.setGameStatus(controller.inventoryGameStatus)
        case ButtonClicked(b) if b == helmetButton => controller.unEquipHelmet()
        case ButtonClicked(b) if b == chestButton => controller.unEquipChest()
        case ButtonClicked(b) if b == pantsButton => controller.unEquipPants()
        case ButtonClicked(b) if b == bootsButton => controller.unEquipBoots()
        case ButtonClicked(b) if b == glovesButton => controller.unEquipGloves()
        case ButtonClicked(b) if b == weaponRightHandButton => controller.unEquipRightHand()
        case ButtonClicked(b) if b == weaponLeftHandButton => controller.unEquipLeftHand()

        //case SelectionChanged(_) => controller.repaint()
      }

      //enemyItems.peer.setDragEnabled(true) maybe iwi bei inventory?!?!??!?!?
      //enemyItems.peer.getDragEnabled maybe iwi bei inventory?!?!??!?!?


      override def paintComponent(g: Graphics2D): Unit = {
        val inventoryBackground = new SpriteSheet("resources/inventoryBackground.png").getImage()

        g.drawImage(inventoryBackground, 0, 0, 256 * SCALE, 144 * SCALE, null)

        //-HealthBar
        g.setFont(new Font("TimesRoman", Font.BOLD, 7 * SCALE))
        g.setColor(Color.BLACK)
        g.fillRect(4 * SCALE, 22 * SCALE, 105 * SCALE, 15 * SCALE)
        g.setColor(Color.RED)
        var HealthHelp1 = 0
        var healthbarWidth = 0
        if (controller.player.maxHealth < 200) {
          HealthHelp1 = controller.player.maxHealth / 100
          val HealthHelp2 = controller.player.health / HealthHelp1
          healthbarWidth = HealthHelp2 * 105 / controller.player.maxHealth
        } else {
          HealthHelp1 = controller.player.maxHealth / 100
          healthbarWidth = controller.player.health * 105 / controller.player.maxHealth
        }
        g.fillRect(4 * SCALE, 22 * SCALE, healthbarWidth * SCALE, 15 * SCALE)
        g.drawRect(4 * SCALE, 22 * SCALE, 105 * SCALE, 15 * SCALE)
        g.setColor(Color.WHITE)
        g.drawString("Health: " + controller.player.health + "/" + controller.player.maxHealth, 30 * SCALE, 32 * SCALE)

        //-ManaBar
        g.setColor(Color.BLACK)
        g.fillRect(4 * SCALE, 40 * SCALE, 105 * SCALE, 15 * SCALE)
        g.setColor(Color.BLUE)
        var ManaHelp1 = 0
        var manabarWidth = 0
        if (controller.player.maxMana < 200) {
          ManaHelp1 = controller.player.maxMana / 100
          val ManaHelp2 = controller.player.mana / ManaHelp1
          manabarWidth = ManaHelp2 * 105 / controller.player.maxMana
        } else {
          ManaHelp1 = controller.player.maxMana / 100
          manabarWidth = controller.player.mana * 105 / controller.player.maxMana
        }
        g.fillRect(4 * SCALE, 40 * SCALE, manabarWidth * SCALE, 15 * SCALE)
        g.drawRect(4 * SCALE, 40 * SCALE, 105 * SCALE, 15 * SCALE)
        g.setColor(Color.WHITE)
        g.drawString("Mana: " + controller.player.mana + "/" + controller.player.maxMana, 30 * SCALE, 50 * SCALE)

        //-Level
        g.setFont(new Font("TimesRoman", Font.BOLD, 10 * SCALE))
        g.drawString("Level: " + controller.player.lvl, 5 * SCALE, 10 * SCALE)
        g.drawString("ATK: " + controller.player.attack, 70 * SCALE, 10 * SCALE)
        g.setFont(new Font("TimesRoman", Font.BOLD, 6 * SCALE))
        g.drawString("Gold: " + controller.player.gulden, 70 * SCALE, 20 * SCALE)
        g.drawString("EXP: " + controller.player.exp + "/" + controller.player.maxExp , 5 * SCALE, 20 * SCALE)

      }
    }
    panel
  }
}
