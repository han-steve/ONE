package com.ONE

import com.ONE.BankActor.BankActionPerformed
import com.ONE.ContactActor.ContactActionPerformed
import com.ONE.UserActor.UserActionPerformed

//#json-support
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat5(User)
  implicit val userUpdateJsonFormat = jsonFormat5(UserUpdate)

  implicit val userActionPerformedJsonFormat = jsonFormat1(UserActionPerformed)

  implicit val transactionJsonFormat = jsonFormat8(Transaction)
  implicit val transactionsJsonFormat = jsonFormat1(Transactions)


  implicit val contactJsonFormat = jsonFormat4(Contact)

  implicit val contactActionPerformedJsonFormat = jsonFormat1(ContactActionPerformed)

  implicit val bankJsonFormat = jsonFormat5(Bank)
  implicit val banksJsonFormat = jsonFormat1(Banks)

  implicit val bankActionPerformedJsonFormat = jsonFormat1(BankActionPerformed)

  implicit val tokenJsonFormat = jsonFormat1(BankAccessToken)
}
