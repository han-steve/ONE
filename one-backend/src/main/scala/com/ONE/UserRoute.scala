package com.ONE

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import com.ONE.UserActor._


import scala.concurrent.Future
import scala.util.{Failure, Success}

object UserRoute extends JsonSupport {

  import AppConstants._
  val userActor: ActorRef = AppConstants.system.actorOf(UserActor.props, "userActor")

  val usersRoutes = pathPrefix("users") {
    pathPrefix("signup") {
      post {
        entity(as[User]) { req =>
          println("[PATH] users/signup")
          val userCreated: Future[String] =
            (userActor ? CreateUser(req)).mapTo[String]
          onComplete(userCreated) {
            case Success(value) =>
              println("[CREATED USER] Successful.")
              complete(s"true")
            case Failure(exception) =>
              println("[CREATE USER] Failed to create user.")
              complete(s"false")
          }
        }
      }
    } ~
      pathPrefix("login") {
        get {
          path(Segment / Segment) { (username, password) =>
            println("[PATH] users/login/" + username)
            val encrypted = EncryptUtil.getMd5(password)
            println("[USER] User " + username + " attempted to login with password: " + encrypted)
            val user: Future[User] = (userActor ? GetUser(username)).mapTo[User]
            complete(user.map(u => encrypted.equals(u.password).toString))
          }
        }
      } ~
      pathPrefix("update") {
        post {
          path(IntNumber) { user_id =>
            println("[PATH] users/update/" + user_id)
            entity(as[UserUpdate]) { user =>
              val userUpdate: Future[String] =
                (userActor ? UpdateUser(user)).mapTo[String]
              onComplete(userUpdate) {
                case Success(value) =>
                  println("[UPDATE USER] Successful.")
                  complete(s"true")
                case Failure(exception) =>
                  println("[UPDATE USER] Failed to update user.")
                  complete(s"false")
              }
            }
          }
        }
      } ~
    path(Segment) { username =>
      get {
        println("[PATH] users/" + username)
        val user: Future[User] =
          (userActor ? GetUser(username)).mapTo[User]
        complete(201, user)
      }
    }
  }
}
