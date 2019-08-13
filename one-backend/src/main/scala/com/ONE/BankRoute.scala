package com.ONE

import akka.actor.ActorRef
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import com.ONE.BankActor.{AddBankConnection, BankActionPerformed, GetAccessToken, GetBankTransactions, GetConnectedBanks, GetPlaidOptions}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object BankRoute extends JsonSupport {

  import AppConstants._
  val bankActor: ActorRef = AppConstants.system.actorOf(BankActor.props, "bankActor")

  val bankRoutes = pathPrefix("bank") {
    pathPrefix(s"token") {
      post {
        pathPrefix("get_access_token") {
          entity(as[BankAccessToken]) { token =>
            println("[PATH] bank/token/get_access_token")
            val response: Future[HttpResponse] =
              (bankActor ? GetAccessToken(token)).mapTo[HttpResponse]
            complete(response)
          }
        } ~ pathPrefix("set_access_token") {
          entity(as[BankAccessToken]) { token =>
            println("[PATH] bank/token/set_access_token")
            val response: Future[HttpResponse] =
              (bankActor ? GetAccessToken(token)).mapTo[HttpResponse]
            complete(response)
          }
        }
      }
    } ~
    pathPrefix("new") {
      post {
        entity(as[Bank]) { connection =>
          println("[PATH] bank/new")
          val connectionMade: Future[String] =
            (bankActor ? AddBankConnection(connection)).mapTo[String]
          onSuccess(connectionMade) { performed =>
            complete(s"true")
          }
        }
      }
    } ~
    pathPrefix(IntNumber) { user_id =>
      get{
        pathPrefix("transactions") {
          println("[PATH] bank/" + user_id + "/transactions")
          path(Segment) { access_token =>
            println("[PATH] bank/" + user_id + "/transactions/" + access_token)
            var response: Future[HttpResponse] =
              (bankActor ? GetBankTransactions(access_token)).mapTo[HttpResponse]
            complete(response)
          }
        } ~
        pathEnd{
          println("[PATH] bank/" + user_id)
          val connections: Future[Banks] =
            (bankActor ? GetConnectedBanks(user_id)).mapTo[Banks]
          complete(201, connections)
        }
      }
    } ~
    pathEnd {
      get {
        println("[PATH] bank")
        val options: Future[HttpResponse] = (bankActor ? GetPlaidOptions()).mapTo[HttpResponse]
        complete(options)
      }
    }
  }
}
