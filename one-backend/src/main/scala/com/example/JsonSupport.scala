package com.example

import com.example.BankActor.BankActionPerformed
import com.example.ContactActor.ContactActionPerformed
import com.example.UserActor.UserActionPerformed
import com.example.TransactionActor.TransactionActionPerformed

//#json-support
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat4(User)
  implicit val userUpdateJsonFormat = jsonFormat8(UserUpdate)

  implicit val userActionPerformedJsonFormat = jsonFormat1(UserActionPerformed)

  implicit val transactionJsonFormat = jsonFormat8(Transaction)
  implicit val transactionUpdateJsonFormat = jsonFormat7(TransactionUpdate)
  implicit val transactionsJsonFormat = jsonFormat1(Transactions)

  implicit val transactionActionPerformedJsonFormat = jsonFormat1(TransactionActionPerformed)

  implicit val contactJsonFormat = jsonFormat4(Contact)

  implicit val contactActionPerformedJsonFormat = jsonFormat1(ContactActionPerformed)

  implicit val bankJsonFormat = jsonFormat4(Bank)
  implicit val banksJsonFormat = jsonFormat1(Banks)

  implicit val bankActionPerformedJsonFormat = jsonFormat1(BankActionPerformed)

  implicit val tokenJsonFormat = jsonFormat1(BankAccessToken)
}
