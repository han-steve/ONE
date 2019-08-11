package com.ONE

import java.sql.DriverManager

import akka.actor.ActorSystem
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext

private[ONE] object AppConstants {

  Class.forName("org.postgresql.ds.PGSimpleDataSource") // Load the driver

  lazy val SqlConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ONE", "timothytqin", "tq05112001") // Make the connection

  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport =
    EntityStreamingSupport.json()
  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

}
