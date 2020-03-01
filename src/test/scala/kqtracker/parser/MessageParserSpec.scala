package kqtracker.parser
import org.specs2.scalacheck._
import org.specs2._
import kqtracker.model._
import enumeratum.scalacheck._
import enumeratum.values.scalacheck._
import fastparse._, NoWhitespace._
class MessageParserSpec
    extends org.specs2.mutable.Specification
    with ScalaCheck {

  "GameStart" >> prop {
    (
        map: GameMap,
        goldOnLeft: Boolean,
        duration: Int,
        isAttractModeEnabled: Boolean
    ) =>
      val event =
        s"![k[gamestart],v[${map.entryName},${goldOnLeft.toString.capitalize},$duration,${isAttractModeEnabled.toString.capitalize}]]!"
      MessageParser.parseEvent(event).get.value must_== GameStart(
        map,
        goldOnLeft,
        duration,
        isAttractModeEnabled
      )
  }.set(workers = 1)

  "GameEnd" >> prop {
    (
        map: GameMap,
        unknown1: Boolean,
        duration: Double,
        unknown2: Boolean
    ) =>
      val event =
        s"![k[gameend],v[${map.entryName},${unknown1.toString.capitalize},$duration,${unknown2.toString.capitalize}]]!"
      MessageParser.parseEvent(event).get.value must_== GameEnd(
        map,
        unknown1,
        duration,
        unknown2
      )
  }
}
