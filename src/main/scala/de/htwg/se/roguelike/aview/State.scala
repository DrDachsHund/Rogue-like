package de.htwg.se.roguelike.aview

trait State {
  def processInputLine(input: String)

  def handle()
}


/*TODO !!!!!!!!!!!!!!!:

WICHTIG BEIM EINLESEN VON INDEX GEHTS NICHT ÜBER 10 MASIVER FEHLER RIPRIP

Controller vll umbauen und nen enemy extra setzen in interact für bessere/einfachere behandlung und ausgaben!!!!

1: LOOT - 50 Wichtig-
  => Random Weapons/Armor/Potion/(BOSS ITEMS) durch einlesen von TXT !!!!!!!!!!!!                                 [✓]
      =>Weapon ersma nur auf Sword becheänken damit einfacher ist bzw                                             [✓}
          => mit match case random eine zurück geben schonmal vorbereiten aber nicht implementieren               [✓}
      =>Random Weapon gemacht nur noch kopieren in andere klassen und verändern stats anpassen!!                  [✓}
  => Inventory Enemies mit Methode füllen (Loot Drop/Gold) !!!!                                                   [✓} //LOOOT => Nach neuem fight nochmal schauen!!!!!!!!!!
      => Skalieren mit Enemy LVL                                                                                  [✓}
          => da jetzt nur lvl übergen wird viel einfacher da ich enemy.lvl einfach übergen kann unabhängiger      [✓}


2: Player -10 Wichtig-
  => LVL/EXP; LVLUP; Skalieren: MaxHP etc. SkillPunkte????  !!                                                     [✓}
  => Zukunft Klassen (VLLT nur Methode); Abilities?????  !                                                         [X}
  => Währung                                                                                                       [✓}
  => Kill Counter                                                                                                  [✓}
  => SCORE(KC + EXP + etc.) für Leaderboard                                                                        [✓}





3: Fight - 5 Wichtig-
  => Gegner Schlau !!!!
  => Mehr Tiefe (Attack,Block,Ability,Fliehen etc.) !!!!
  => Armor/Weapon Specials !!!


4: Level - 2 Wichtig-
  => Truhen, Random Loot !!!
  => Neue Level, Maybe Boss Level alle 5 (BOSS ITEMS), Portale/Höhlen etc. !
  => MAYBE Hindernisse BIG FRAGEZEICHEN ????????????????????????
  => Eingang Häuser,Shop
  => Monster Skalieren (Level Tiefe + Player Level)!!!!!!


5: Merchant - 0 Wichtig-
  => Shop
  => Random Items (Mehr/Bessere Items bei größerer Level Tiefe)

6: Enemy Resistance + Weapon Interaction (FIRE 2x DMG to ICE RES)


GAME OVER SCREEN

ARMOR/Weapon vergleichen ROT die Schlecht sind Grün gut (WoW)

*/