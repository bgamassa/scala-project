name := """aggregator"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test
libraryDependencies += jdbc
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies += "com.github.takezoe" %% "scala-jdbc" % "1.0.5"
libraryDependencies += filters
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.2"
