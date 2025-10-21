
val toolkitVersion = "0.1.6"
val circeVersion = "0.14.15"
val scalaTestVersion = "3.2.19"

ThisBuild / scalaVersion := "3.7.3"
libraryDependencies ++= Seq(
  "org.typelevel" %% "toolkit" % toolkitVersion,
  "org.typelevel" %% "toolkit-test" % toolkitVersion % Test,

  // json
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  // config
  "com.typesafe" % "config" % "1.4.5",

  // testing
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "org.scalacheck" %% "scalacheck" % "1.19.0" % Test,
  "org.scalameta" %% "munit-scalacheck" % "1.2.0" % Test,
)
