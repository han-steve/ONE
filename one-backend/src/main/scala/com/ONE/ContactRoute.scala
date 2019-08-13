package com.ONE

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import com.ONE.ContactActor.{ContactActionPerformed, MakeContact}
import com.ONE.UserActor._

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ContactRoute extends JsonSupport {

  import AppConstants._
  val contactActor: ActorRef = AppConstants.system.actorOf(ContactActor.props, "contactActor")

  val contactRoutes = pathPrefix("contact") {
    pathEnd {
      post {
        entity(as[Contact]) { contact =>
          val contactMade: Future[ContactActionPerformed] =
            (contactActor ? MakeContact(contact)).mapTo[ContactActionPerformed]
          onSuccess(contactMade) { performed =>
            val query = "insert into contact values ('" + contact.username + "', '" +
              contact.contact_date + "', '" +
              contact.email + "', '" +
              contact.comment + "'); "
            println("[QUERY] " + query)
            val sql_statement = AppConstants.SqlConnection.createStatement()
            sql_statement.execute(query)
            //                val result = sql_statement.getResultSet()
            //                result.next()
            complete((StatusCodes.Created, "Contact saved."))
          }
        }
      }
    }
  }
}
