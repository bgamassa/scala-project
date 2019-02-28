import Dependencies._

lazy val commonSettings = Seq(
  organization := "fr.epita",
  developers := List(
    Developer(
      id    = "noel_m",
      name  = "Martin NOEL",
      email = "martin.noel@epita.fr",
      url   = url("https://github.com/tinmarzoo")
    ),
    Developer(
      id    = "gamass_b",
      name  = "Binta GAMASSA",
      email = "binta.gamassa@epita.fr",
      url   = url("https://github.com/bgamassa")
    ),
    Developer(
      id    = "lewand_m",
      name  = "Maxime LEWANDOWSKI",
      email = "maxime.lewandowski@epita.fr",
      url   = url("https://github.com/lywel")
    )
  ),
  scalaVersion := "2.12.8",
  libraryDependencies ++= Seq( /* common dependencies */ ),
  scalaSource in Compile := baseDirectory.value / ".." / ".." / "src" / "main" / "scala",
  scalaSource in Test    := baseDirectory.value / ".." / ".." / "src" / "test" / "scala"
)

lazy val emulator = (project in file("emulator"))
  .settings(
    commonSettings,
    name := "emulator",
    version := "0.0.1",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.10",
    libraryDependencies += "com.nrinaudo" %% "kantan.csv-generic" % "0.5.0"
  )

lazy val aggregator = (project in file("aggregator"))
  .settings(
    commonSettings,
    name := "aggregator",
    version := "0.0.1",
    libraryDependencies += scalaTest % Test,
  )
// ThisBuild / pomIncludeRepository := { _ => false }
// ThisBuild / publishTo := {
//   val nexus = "https://oss.sonatype.org/"
//   if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
//   else Some("releases" at nexus + "service/local/staging/deploy/maven2")
// }
// ThisBuild / publishMavenStyle := true
