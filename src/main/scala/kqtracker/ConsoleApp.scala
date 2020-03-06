package kqtracker

import cats._
import cats.implicits._
import cats.effect._
import java.net.http._

import fastparse.Parsed
import kqtracker.parser.EventParser
import org.http4s.implicits._
import org.http4s.client.jdkhttpclient._

import cats.effect.IO
import fs2.Stream
import skunk._
import skunk.implicits._
import skunk.codec.all._
import natchez.Trace.Implicits.noop
import java.util.UUID
import skunk.data.Completion

object ConsoleApp extends IOApp {
  val session: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      user = "ryan",
      password = None,
      database = "kqtracker"
    )


  override def run(args: List[String]): IO[ExitCode] = {
    session
      .use(session =>
        IO(HttpClient.newHttpClient())
          .map(JdkWSClient[IO](_))
          .flatMap(wsClient =>
            wsClient
              .connectHighLevel(WSRequest(uri"wss://localhost"))
              .use { conn =>
                val rawEvents = conn.receiveStream
                  .collect { case WSFrame.Text(str, _) => str }

                rawEvents
                  .evalMap(event => IO(UUID.randomUUID, event))
                  .evalMap {
                    case (id, event) =>
                    session
                        .prepare(sql"INSERT INTO raw_event VALUES($uuid, $varchar)".command)
                        .use(_.execute(id, event))
                  }
                  .interruptWhen(Stream.eval(IO.async[Boolean]{cb => 
                    println("Press Enter to Quit")
                    Console.in.readLine()
                    cb(Right(true))
                  }))
                  .compile
                  .drain
              }
          )
      )
      .as(ExitCode.Success)
  }
}
