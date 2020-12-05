lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion = "2.6.0-M2"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7"
    )),
    name := "one-backend",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.slick" %% "slick" % "3.2.0",
      "org.postgresql" % "postgresql" % "42.2.5",
      "org.slf4j" % "slf4j-nop" % "1.7.26",
      "com.plaid" % "plaid-java" % "5.1.1",


"com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test
    ),
    mainClass in reStart := Some("com.example.QuickstartServer.scala"),
    reColors := Seq("blue", "green", "magenta")

  )

//lazy val commonSettings = Seq(
//  version := "0.1-SNAPSHOT",
//  organization := "com.example",
//  scalaVersion := "2.12.7",
//  test in assembly := {}
//)
//
//lazy val app = (project in file("app")).
//  settings(commonSettings: _*).
//  settings(
//    mainClass in assembly := Some("com.example.QuickstartServer"),
//    // more settings here ...
//  )
//
//lazy val utils = (project in file("utils")).
//  settings(commonSettings: _*).
//  settings(
//    assemblyJarName in assembly := "utils.jar",
//    // more settings here ...
//  )