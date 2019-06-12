package com.example

import com.example.UserRegistryActor.UserActionPerformed
import com.example.TransactionActor.TransactionActionPerformed

//#json-support
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat4(User)
  implicit val userUpdateJsonFormat = jsonFormat8(UserUpdate)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val userActionPerformedJsonFormat = jsonFormat1(UserActionPerformed)

  implicit val transactionJsonFormat = jsonFormat8(Transaction)
  implicit val transactionUpdateJsonFormat = jsonFormat7(TransactionUpdate)
  implicit val transactionsJsonFormat = jsonFormat1(Transactions)

  implicit val transactionActionPerformedJsonFormat = jsonFormat1(TransactionActionPerformed)
}
