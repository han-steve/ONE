package com.ONE

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{get}

trait Routes extends JsonSupport with CORSHandler {


  lazy val routes: Route =
    corsHandler(
      UserRoute.usersRoutes ~
      TransactionRoute.transactionRoutes ~
      BankRoute.bankRoutes ~
      ContactRoute.contactRoutes ~
      get {
        path("one") {
          getFromResource("one/index.html")
        } ~
        pathPrefix("one") {
          getFromResourceDirectory("one")
        }
    })

}
