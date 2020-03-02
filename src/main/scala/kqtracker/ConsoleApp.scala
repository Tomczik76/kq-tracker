package kqtracker

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import java.net.http.HttpClient
import org.http4s.client.jdkhttpclient._

object ConsoleApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
  
      IO(HttpClient.newHttpClient())
        .map { httpClient =>
          (JdkHttpClient[IO](httpClient), JdkWSClient[IO](httpClient))
        }
        
    IO.pure(ExitCode.Success)
  }
}
