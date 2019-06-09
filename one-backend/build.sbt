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
      "com.outr" %% "hasher" % "1.2.1",
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test
    ),
    mainClass in reStart := Some("com.example.QuickstartServer"),
    reColors := Seq("blue", "green", "magenta")

  )
