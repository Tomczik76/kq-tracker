package kqtracker.parser

import fastparse._, NoWhitespace._
import kqtracker.model._
object EventParser {

  def valueChars[_: P] = P(CharsWhile((c: Char) => c != '[' && c != ']'))

  def event[_: P, A](key: String, value: => P[A]) =
    P(s"![k[" ~ key ~ "],v[" ~ value ~ "]]!")
  def falseValue[_: P] = P("False").map(_ => false)
  def trueValue[_: P] = P("True").map(_ => true)
  def bool[_: P] = P(trueValue | falseValue)

  def num[_: P] = P(CharsWhileIn("0-9"))
  def signedNum[_: P] = P("-".? ~ num)
  def int[_: P] = signedNum.!.map(_.toInt)
  def double[_: P] =
    P(signedNum ~ ".".? ~ num).!.map(_.toDouble)
  def startGameValue[_: P] = P(CharsWhileIn("A-Z").! ~ "," ~ CharsWhileIn("").!)

  def dayMap[_: P] = P("map_day").map(_ => GameMap.Day)
  def duskMap[_: P] = P("map_dusk").map(_ => GameMap.Dusk)
  def nightMap[_: P] = P("map_night").map(_ => GameMap.Night)
  def map[_: P]: P[GameMap] = P(dayMap | duskMap | nightMap)

  def player[_: P] = P("10" | CharIn("1-9")).!.map(Player.withName)

  def worker[_: P] = P("Worker").map(_ => PlayerType.Worker)
  def soldier[_: P] = P("Soldier").map(_ => PlayerType.Soldier)
  def queen[_: P] = P("Queen").map(_ => PlayerType.Queen)
  def playerType[_: P]: P[PlayerType] = (worker | soldier | queen)

  def playerKillValue[_: P] =
    P(int ~ "," ~ int ~ "," ~ player ~ "," ~ player ~ "," ~ playerType)
      .map(PlayerKill.tupled)
  def playerKill[_: P] = event("playerKill", playerKillValue)

  def gameStartValue[_: P] =
    P(map ~ "," ~ ("True" | "False") ~ ",0," ~ bool).map(GameStart.tupled)
  def gameStart[_: P] = event("gamestart", gameStartValue)

  def gameEndValue[_: P] =
    P(map ~ "," ~ ("True" | "False") ~ "," ~ double ~ "," ~ bool)
      .map(GameEnd.tupled)

  def gameEnd[_: P] = event("gameend", gameEndValue)

  def gameEvent[_: P]: P[GameEvent] = P(gameStart | gameEnd | playerKill)

  def parseEvent(event: String): Parsed[GameEvent] = parse(event, gameEvent(_))
}
