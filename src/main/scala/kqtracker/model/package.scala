package kqtracker.model

import enumeratum._
import enumeratum.EnumEntry._

sealed abstract class GameMap(override val entryName: String) extends EnumEntry

object GameMap extends Enum[GameMap] {
  val values = findValues

  case object Day extends GameMap("map_day")
  case object Dusk extends GameMap("map_dusk")
  case object Night extends GameMap("map_night")
}

sealed abstract class Player(override val entryName: String) extends EnumEntry

object Player extends Enum[Player] {
  val values = findValues

  case object GoldQueen extends Player("1") 
  case object GoldStripes extends Player("2")
  case object GoldAbs extends Player("3")
  case object GoldSkulls extends Player("4")
  case object GoldChecks extends Player("5")
  case object BlueQueen extends Player("6")
  case object BlueStripes extends Player("7")
  case object BlueAbs extends Player("8")
  case object BlueSkulls extends Player("9")
  case object BlueChecks extends Player("10")
}

sealed trait  PlayerType extends EnumEntry with CapitalWords

object PlayerType extends Enum[PlayerType] {
  val values = findValues

  case object Worker extends PlayerType
  case object Soldier extends PlayerType
  case object Queen extends PlayerType
}

sealed trait GameEvent
case class GameStart(
    map: GameMap,
    isAttractMode: Boolean
) extends GameEvent

case class GameEnd(
    map: GameMap,
    duration: Double,
    isAttractMode: Boolean
) extends GameEvent

case class PlayerKill(
    x: Int,
    y: Int,
    killer: Player,
    victim: Player,
    victimType: PlayerType
) extends GameEvent
