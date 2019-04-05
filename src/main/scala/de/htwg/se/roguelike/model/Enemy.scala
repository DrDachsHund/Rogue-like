package de.htwg.se.roguelike.model

  case class Enemy(name: String) {
    def this() = this(name = "Skeleton")

    val health = 100
    val attack = 10
    val exp = 0


    override def toString: String =
      "Name: " + name +
        "\nhealth: " + health +
        "\nAttack: " + attack +
        "\nExperience: " + exp

  }