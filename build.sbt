val Http4sVersion = "0.21.1"
val CirceVersion = "0.13.0"
val Specs2Version = "4.8.3"
val LogbackVersion = "1.2.3"
val enumeratumScalacheckVersion = "1.5.15"

lazy val root = (project in file("."))
  .settings(
    organization := "kqtracker",
    name := "kqtracker",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.10",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server"    % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client"    % Http4sVersion,
      "org.http4s"      %% "http4s-circe"           % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"             % Http4sVersion,
      "org.http4s"      %% "http4s-jdk-http-client" % "0.2.0",
      "io.circe"        %% "circe-generic"          % CirceVersion,
      "org.tpolecat"    %% "skunk-core"             % "0.0.7",
      "com.beachape"    %% "enumeratum"             % enumeratumScalacheckVersion,
      "com.beachape"    %% "enumeratum-scalacheck"  % enumeratumScalacheckVersion % "test",
      "org.specs2"      %% "specs2-core"            % Specs2Version  % "test",
      "org.specs2"      %% "specs2-scalacheck"      % "4.8.3"        % "test",
      "org.scalacheck"  %% "scalacheck"             % "1.14.1"       % "test",
      "ch.qos.logback"  %  "logback-classic"        % LogbackVersion,
      "com.lihaoyi"     %% "fastparse"              % "2.2.2"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
)
