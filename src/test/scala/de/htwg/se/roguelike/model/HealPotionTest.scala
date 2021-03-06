package de.htwg.se.roguelike.model

import de.htwg.se.roguelike.model.levelComponent.PlayerInterface
import de.htwg.se.roguelike.model.levelComponent.levelBaseImpl.{Player, Potion}
import org.scalatest.{Matchers, WordSpec}

class HealPotionTest extends WordSpec with Matchers {

      val healPotion = Potion("SmallHeal")
      var player:PlayerInterface = Player(name = "TestPlayer",health = 90)
      var player2:PlayerInterface = Player(name = "TestPlayer",health = 10)

      "should heal and cap at 100 when used" in {
        player = healPotion.usePotion(player)
        player.health should be(100)
      }

      "should heal normal when used on low health" in {
        player2 = healPotion.usePotion(player2)
        player2.health should be(35)
      }

      "should be usable" in {
        healPotion.isUseable should be(true)
      }
}
