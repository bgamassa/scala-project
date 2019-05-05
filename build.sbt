val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
val playJson  = "com.typesafe.play" %% "play-json" % "2.6.9"

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
)

lazy val emulator = (project in file("emulator"))
  .settings(commonSettings)
  .settings(
    name := "emulator",
    version := "0.0.1",
    libraryDependencies ++= Seq(playJson),
    libraryDependencies += scalaTest % Test
  )
