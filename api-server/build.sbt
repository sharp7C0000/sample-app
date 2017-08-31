name := """api-server"""
organization := "com.sharp7c0000"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  ws,
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test,
  "com.pauldijou" %% "jwt-play" % "0.14.0"
)

routesGenerator := InjectedRoutesGenerator

// for vagrant
PlayKeys.fileWatchService := play.dev.filewatch.FileWatchService.sbt(1000)
