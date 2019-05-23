package de.htwg.se.roguelike.controller

import de.htwg.se.roguelike.model._
import de.htwg.se.roguelike.util.{Observable, UndoManager}

class Controller(var level:Level, var player:Player, var enemies:Vector[Enemy] = Vector()) extends Observable {

  val fight = new Fight
  var gameStatus = GameStatus.LEVEL
  private val undoManager = new UndoManager

  def createRandomLevel: Unit = {
    val (level1,enemies1) = new LevelCreator(10).createRandom(player, 10)
    level = level1
    enemies = enemies1
    undoManager.doStep(new LevelCommand((level,player),enemies,this))
    notifyObservers
  }

  def createLevel: Unit = {
    level = new LevelCreator(10).createLevel(player, enemies)
    undoManager.doStep(new LevelCommand((level,player),enemies,this))
    notifyObservers
  }

  def interaction: Unit = {
    if (fight.interaction(player,enemies)) {
      gameStatus = GameStatus.FIGHT
      strategy = new StrategyFight
      //setGameStatus(GameStatus.FIGHT) //schreibt sonst 2 mal fight
    }
  }

  def moveUp: Unit = {
    undoManager.doStep(new LevelCommand(level.moveUp(player),enemies,this))
    notifyObservers
  }

  def moveDown: Unit = {
    undoManager.doStep(new LevelCommand(level.moveDown(player),enemies,this))
    notifyObservers
  }

  def moveLeft: Unit = {
    undoManager.doStep(new LevelCommand(level.moveLeft(player),enemies,this))
    notifyObservers
  }

  def moveRight: Unit = {
    undoManager.doStep(new LevelCommand(level.moveRight(player),enemies,this))
    notifyObservers
  }

  //Fight----
  def attack():Unit = {
      var enemy:Enemy = new Enemy()
      for (enemyTest <- enemies) {
        if (player.posX == enemyTest.posX && player.posY == enemyTest.posY)
          enemy = enemyTest
      }

      enemies = enemies.filterNot(_ == enemy)

      enemy = fight.playerAttack(player, enemy)
      player = fight.enemyAttack(player, enemy)

      if (!player.isAlive()) setGameStatus(GameStatus.GAMEOVER)
      else if (!enemy.isAlive()) {
        setGameStatus(GameStatus.LEVEL)
        level = level.removeElement(enemy.posY, enemy.posX, 5)
        //loot einfügen
      } else {
        enemies = enemies :+ enemy
        setGameStatus(GameStatus.FIGHTSTATUS)
        setGameStatus(GameStatus.FIGHT)
      }
  }
  //Fight----

  //Strategy Pattern toString---
  var strategy: Strategy = new StrategyLevel

  trait Strategy {
    def updateToString:String
  }
  class StrategyLevel extends Strategy {
    override def updateToString = level.toString
  }
  class StrategyFight extends Strategy {
    override def updateToString = fight.toString + "[i]Inventory\n"
  }
  class StrategyFightStatus extends Strategy {
    override def updateToString = fightStatus
  }
  class StrategyInventory extends Strategy {
    override def updateToString =
        player.helmet.name + ": " + player.helmet.armor + "\n" +
        player.chest.name + ": " + player.chest.armor +  "\n" +
        player.pants.name + ": " + player.pants.armor +  "\n" +
        player.boots.name + ": " + player.boots.armor +  "\n" +
        player.gloves.name + ": " + player.gloves.armor +  "\n" +
        "[1]Potions\n" +
        "[2]Weapons\n" +
        "[3]Armor\n" +
        "[x]Back\n"
  }
  class StrategyPotions extends Strategy {
    override def updateToString =
      "Player Health: <" + player.health + ">\n" +
      "Player Mana: <" + player.mana + ">\n" +
      player.inventory.potionsToString + "[x}Back\n"
  }
  class StrategyWeapons extends Strategy {
    override def updateToString = player.inventory.weaponsToString + "[x}Back\n"
  }
  class StrategyArmor extends Strategy {
    override def updateToString =
        player.helmet.name + ": " + player.helmet.armor + "\n" +
        player.chest.name + ": " + player.chest.armor +  "\n" +
        player.pants.name + ": " + player.pants.armor +  "\n" +
        player.boots.name + ": " + player.boots.armor +  "\n" +
        player.gloves.name + ": " + player.gloves.armor +  "\n" +
        player.inventory.armorToString + "[x}Back\n"
  }
  class StrategyGameOver extends Strategy {
    override def updateToString = "GAME OVER DUDE"
  }
  def fightStatus:String = {
    var sb = new StringBuilder
    sb ++= ("Player Health: <" + player.health + ">\n")
    sb ++= "Enemy Health: "
    for (enemyTest <- enemies) {
      if (player.posX == enemyTest.posX && player.posY == enemyTest.posY)
        sb ++= ("<" + enemyTest.health + ">")
    }
    sb ++= "\n"
    sb.toString
  }
  //Strategy Pattern toString---

  //UndoManager---
  def undo: Unit = {
    undoManager.undoStep
    notifyObservers
  }

  def redo: Unit = {
    undoManager.redoStep
    notifyObservers
  }
  //UndoManager---

  //Inventory---
  def usePotion(index:Int): Unit = {
    if (player.inventory.potions.size < 1) {
      println("Keine Potion Vorhanden!!!")
    } else if (index <= player.inventory.potions.size && index > 0) {
      val potion = player.inventory.getPotion(index)
      player = potion.usePotion(player)
      var usedPotion = player.inventory.potions.filter(_ == potion)
      usedPotion = usedPotion.drop(1)
      var newPotions = player.inventory.potions.filterNot(_ == potion)
      newPotions ++= usedPotion
      player = player.copy(inventory = player.inventory.copy(potions = newPotions))
    } else println("CONTROLLER INKOREKTER INDEX => " + index)
    notifyObservers
  }

  def equipArmor(index:Int):Unit = {
    if (player.inventory.armor.size < 1) {
      println("Keine Armor Vorhanden!!!")
    } else if (index <= player.inventory.armor.size && index > 0) {
      val playerArmor = player.inventory.getArmor(index)

      var usedArmor = player.inventory.armor.filter(_ == playerArmor)
      usedArmor = usedArmor.drop(1)

      var oldArmor:Armor = Armor("nothing")
      playerArmor.armorType match {
        case "Helmet" => oldArmor = equipHelmet(playerArmor)
        case "Chest" => oldArmor = equipChest(playerArmor)
        case "Pants" => oldArmor = equipPants(playerArmor)
        case "Boots" => oldArmor = equipBoots(playerArmor)
        case "Gloves" => oldArmor = equipGloves(playerArmor)
      }

      var newArmor = player.inventory.armor.filterNot(_ == playerArmor)
      newArmor ++= usedArmor
      if (oldArmor.armorType != "nothing") {
       newArmor ++= oldArmor :: Nil
      }


      player = player.copy(inventory = player.inventory.copy(armor = newArmor))
    } else println("CONTROLLER INKOREKTER INDEX => " + index)
    notifyObservers
  }

  private def equipHelmet(newHelmet:Armor): Armor = {
    val oldHelmet = player.helmet
    player = player.copy(helmet = newHelmet)
    oldHelmet
  }

  private def equipChest(newChest:Armor): Armor = {
    val oldHelmet = player.chest
    player = player.copy(chest = newChest)
    oldHelmet
  }

  private def equipPants(newPants:Armor): Armor = {
    val oldHelmet = player.pants
    player = player.copy(pants = newPants)
    oldHelmet
  }

  private def equipBoots(newBoots:Armor): Armor = {
    val oldHelmet = player.boots
    player = player.copy(boots = newBoots)
    oldHelmet
  }

  private def equipGloves(newGloves:Armor): Armor = {
    val oldHelmet = player.gloves
    player = player.copy(gloves = newGloves)
    oldHelmet
  }




  def unEquipHelmet():Unit = {
    if (player.helmet.armorType != "nothing") {
      var newArmor = player.inventory.armor
      newArmor ++= player.helmet :: Nil
      val newInventory = player.inventory.copy(armor = newArmor)
      player = player.copy(helmet = Armor("noHelmet"),inventory = newInventory)
    }
    notifyObservers
  }
  def unEquipChest():Unit = {
    if (player.chest.armorType != "nothing") {
      var newArmor = player.inventory.armor
      newArmor ++= player.chest :: Nil
      val newInventory = player.inventory.copy(armor = newArmor)
      player = player.copy(chest = Armor("noChest"),inventory = newInventory)
    }
    notifyObservers
  }
  def unEquipPants():Unit = {
    if (player.pants.armorType != "nothing") {
      var newArmor = player.inventory.armor
      newArmor ++= player.pants :: Nil
      val newInventory = player.inventory.copy(armor = newArmor)
      player = player.copy(pants = Armor("noPants"),inventory = newInventory)
    }
    notifyObservers
  }
  def unEquipBoots():Unit = {
    if (player.boots.armorType != "nothing") {
      var newArmor = player.inventory.armor
      newArmor ++= player.boots :: Nil
      val newInventory = player.inventory.copy(armor = newArmor)
      player = player.copy(boots = Armor("noBoots"),inventory = newInventory)
    }
    notifyObservers
  }
  def unEquipGloves():Unit = {
    if (player.gloves.armorType != "nothing") {
      var newArmor = player.inventory.armor
      newArmor ++= player.gloves :: Nil
      val newInventory = player.inventory.copy(armor = newArmor)
      player = player.copy(gloves = Armor("noGloves"),inventory = newInventory)
    }
    notifyObservers
  }


  //Inventory---

  def setGameStatus(gameStatus: GameStatus.Value): Unit = {
    this.gameStatus = gameStatus
    gameStatus match {
      case GameStatus.LEVEL => strategy = new StrategyLevel
      case GameStatus.FIGHT => strategy = new StrategyFight
      case GameStatus.FIGHTSTATUS => strategy = new StrategyFightStatus
      case GameStatus.GAMEOVER => strategy = new StrategyGameOver
      case GameStatus.INVENTORY => strategy = new StrategyInventory
      case GameStatus.INVENTORYPOTION => strategy = new StrategyPotions
      case GameStatus.INVENTORYWEAPON => strategy = new StrategyWeapons
      case GameStatus.INVENTORYARMOR => strategy = new StrategyArmor
    }
    notifyObservers
  }

}
