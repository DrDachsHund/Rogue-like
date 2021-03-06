package de.htwg.se.roguelike.model.levelComponent.levelBaseImpl

import de.htwg.se.roguelike.model.levelComponent.{PlayerInterface, PotionInterface}

case class ManaPotion(name: String,
                      value: Int,
                      usable: Boolean,
                      power: Int,
                      rarity: String,
                      textureIndex:Int = 0) extends PotionInterface {

  override def usePotion(player: PlayerInterface): PlayerInterface = {
    super.usePotion(player.nextPlayer(mana = player.mana + (player.maxMana / 100 * power)))
  }
}
