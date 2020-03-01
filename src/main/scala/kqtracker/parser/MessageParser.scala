package kqtracker.parser

import fastparse._, NoWhitespace._
import kqtracker.model._
object MessageParser {

  def valueChars[_: P] = P(CharsWhile((c: Char) => c != '[' && c != ']'))

  def event[_: P, A](key: String, value: => P[A]) = P(s"![k[" ~ key ~ "],v[" ~ value ~ "]]!")
  def falseValue[_: P] = P("False").map(_ => false)
  def trueValue[_: P] = P("True").map(_ => true)
  def boolean[_:P] = P(trueValue | falseValue)
  def integer[_:P] = P(CharsWhileIn("\\-0-9").!).map(_.toInt)
  def double[_:P] = P(CharsWhileIn("\\-0-9\\.E").!).map(_.toDouble)
  def startGameValue[_:P] = P(CharsWhileIn("A-Z").! ~ "," ~ CharsWhileIn("").!)
  def dayMap[_:P] = P("map_day").map(_ => GameMap.Day)
  def duskMap[_:P] = P("map_dusk").map(_ => GameMap.Dusk)
  def nightMap[_:P] = P("map_night").map(_ => GameMap.Night)
  def map[_:P]: P[GameMap] = P(dayMap | duskMap | nightMap)

  def gameStartValue[_: P] = P(map ~ "," ~ boolean ~ "," ~ integer ~ "," ~ boolean).map(GameStart.tupled)
  def gameStart[_: P] = event("gamestart", gameStartValue)

  def gameEndValue[_: P] = P(map ~/ "," ~ boolean ~/ "," ~ double ~/ "," ~/ boolean).map(GameEnd.tupled)
  def gameEnd[_: P] = event("gameend", gameEndValue)
  
  def parser[_:P] = P(gameStart | gameEnd)

  def parseEvent(event:String) = parse(event, parser(_))
}
