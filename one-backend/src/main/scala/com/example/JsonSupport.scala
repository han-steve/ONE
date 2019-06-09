package com.example

import com.example.UserRegistryActor.UserActionPerformed
import com.example.TransactionActor.TransactionActionPerformed

//#json-support
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val userActionPerformedJsonFormat = jsonFormat1(UserActionPerformed)

  implicit val transactionJsonFormat = jsonFormat7(Transaction)
  implicit val transactionUpdateJsonFormat = jsonFormat13(TransactionUpdate)
  implicit val transactionsJsonFormat = jsonFormat1(Transactions)

  implicit val transactionActionPerformedJsonFormat = jsonFormat1(TransactionActionPerformed)
}
