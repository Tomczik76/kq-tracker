package kqtracker.parser
import org.specs2.scalacheck._
import org.specs2._
import kqtracker.model._
import enumeratum.scalacheck._
import enumeratum.values.scalacheck._
import fastparse._, NoWhitespace._
import org.scalacheck._
import org.scalacheck.Arbitrary._
import java.math.MathContext
class EventParserSpec
    extends org.specs2.mutable.Specification
    with ScalaCheck {

  val positionArb = Arbitrary(Gen.choose(0, 2000))

  val durationArb = Arbitrary(
    for {
      n <- Gen.choose(1, 600)
      d <- Gen.choose(0, 99909)
    } yield (n + d * 0.00001)
  )

  "Player Kill" >> prop {
    (
        x: Int,
        y: Int,
        killer: Player,
        victim: Player,
        victimType: PlayerType
    ) =>
      val event =
        s"![k[playerKill],v[$x,$y,${killer.entryName},${victim.entryName},${victimType.entryName}]]!"
      EventParser.parseEvent(event).get.value must_== PlayerKill(
        x, y, killer, victim, victimType
      )
  }.setArbitrary1(positionArb).setArbitrary2(positionArb)

  "Game Start" >> prop {
    (
        map: GameMap,
        isAttractModeEnabled: Boolean
    ) =>
      val event =
        s"![k[gamestart],v[${map.entryName},False,0,${isAttractModeEnabled.toString.capitalize}]]!"
      EventParser.parseEvent(event).get.value must_== GameStart(
        map,
        isAttractModeEnabled
      )
  }

  "Game End" >> prop {
    (
        map: GameMap,
        duration: Double,
        isAttractModeEnabled: Boolean
    ) =>
      val event =
        f"![k[gameend],v[${map.entryName},False,$duration,${isAttractModeEnabled.toString.capitalize}]]!"
        println(event)
      EventParser.parseEvent(event).get.value must_== GameEnd(
        map,
        duration,
        isAttractModeEnabled
      )
  }.setArbitrary2(durationArb)
}
