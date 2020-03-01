package kqtracker.model
import enumeratum._

sealed trait GameEvent
case class GameStart(
    map: GameMap,
    goldOnLeft: Boolean,
    duration: Int,
    isAttractModeEnabled: Boolean
) extends GameEvent

case class GameEnd(
    map: GameMap,
    unknown1: Boolean,
    duration: Double,
    unknown2: Boolean
) extends GameEvent

sealed abstract class GameMap(override val entryName: String) extends EnumEntry

object GameMap extends Enum[GameMap] {
  val values = findValues

  case object Day extends GameMap("map_day")
  case object Dusk extends GameMap("map_dusk")
  case object Night extends GameMap("map_night")
}
