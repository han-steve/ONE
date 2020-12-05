package com.ONE

import java.sql.ResultSet

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import com.ONE.TransactionActor.{DeleteTransaction, GetTransactions, MakeTransaction, UpdateTransaction}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object TransactionRoute extends JsonSupport {

  import AppConstants._
  val transactionActor: ActorRef = AppConstants.system.actorOf(TransactionActor.props, "transactionActor")

  val transactionRoutes =
    pathPrefix("transactions") {
      pathPrefix("create") {
        post {
          println("[PATH] transactions/create")
          entity(as[Transaction]) { transaction =>
            val transactionMade: Future[String] =
              (transactionActor ? MakeTransaction(transaction)).mapTo[String]
            complete(transactionMade)
          }
        }
      } ~
      pathPrefix(Segment) { user_id =>
        pathEnd {
          println("[PATH] transactions/" + user_id)
          get {
            val transaction: Future[Transactions] =
              (transactionActor ? GetTransactions(user_id)).mapTo[Transactions]
            complete(transaction)
          }
        } ~
        pathPrefix("update") {
          println("[PATH] transactions/" + user_id + "/update")
          post {
            entity(as[Transaction]) { transaction =>
              val transactionMade: Future[String] =
                (transactionActor ? UpdateTransaction(transaction)).mapTo[String]
              complete(transactionMade)
            }
          }
        } ~
        path("delete" / Segment) { transaction_id =>
          println("[PATH] transactions/" + user_id + "/delete/" + transaction_id)
          get {
            val transactionDeleted: Future[String] =
              (transactionActor ? DeleteTransaction(transaction_id)).mapTo[String]
            onComplete(transactionDeleted) {
              case Success(value) =>
                println("[TRANSACTION] Delete successful.")
                complete("true")
              case Failure(error) =>
                println("[TRANSACTION] Delete failed.")
                complete("false")
              }
          }
        }
      }
    }
}
