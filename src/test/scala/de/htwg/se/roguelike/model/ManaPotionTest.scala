package de.htwg.se.roguelike.model

import de.htwg.se.roguelike.model.levelComponent.PlayerInterface
import de.htwg.se.roguelike.model.levelComponent.levelBaseImpl.{Player, Potion}
import org.scalatest.{Matchers, WordSpec}

class ManaPotionTest extends WordSpec with Matchers {

  "A Mana Potion" when {
    "When new" should {
      val manaPotion = Potion("FullMana")
      "have a use" in {
        var player:PlayerInterface = new Player(name = "Test",mana = 1)
        player = manaPotion.usePotion(player)
        player.mana should be(player.maxMana)
      }
    }
  }

}
