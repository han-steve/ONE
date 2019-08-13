package com.ONE

import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }
import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object QuickstartServer extends Routes {

  import AppConstants._

  lazy val route: Route = routes

  val host = "localhost"
  val port = 8080
  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, host, port)

  def main(args: Array[String]): Unit = {

    serverBinding.onComplete {
      case Success(bound) =>
        println(s"Server online at http://${host}:${port}/")
      case Failure(e) =>
        Console.err.println(s"Server could not start!")
        e.printStackTrace()
        AppConstants.system.terminate()
    }

    Await.result(AppConstants.system.whenTerminated, Duration.Inf)
  }

}