package com.ONE

import java.sql.ResultSet

import akka.actor.{ Actor, ActorLogging, Props }

final case class Transaction(user_id: Int, transaction_date: String, category_id: String, category: String, account: String, payee: String, amount: Double, memo: String, transaction_id: Option[Int] =  None)
final case class Transactions(transactions: Seq[Transaction])

object TransactionActor {
  final case class GetTransactions(user_id: String)
  final case class MakeTransaction(transaction: Transaction)
  final case class UpdateTransaction(transaction: Transaction)
  final case class DeleteTransaction(user_id: String)

  def props: Props = Props[TransactionActor]
}

class TransactionActor extends Actor with ActorLogging {
  import TransactionActor._

  def receive: Receive = {
    case GetTransactions(user_id) => {
      val query = "select * from transactions where user_id = '" + user_id + "' order by transaction_date desc;"
      println("[QUERY] " + query)
      val result = AppConstants.SqlConnection.createStatement().executeQuery(query)
      var dbTransactions = Set.empty[Transaction]
      while (result.next()) {
        dbTransactions += Transaction(result.getInt("user_id"), result.getString("transaction_date"), result.getString("category_id"), result.getString("category"),
          result.getString("account"), result.getString("payee"), result.getDouble("amount"), result.getString("memo"), Option(result.getInt("transaction_id")))
      }
      sender() ! Transactions(dbTransactions.toSeq)
    }
    case MakeTransaction(transaction) =>
      val query = "insert into transactions (\"user_id\", \"transaction_date\", \"category_id\", \"category\", \"account\", \"payee\", \"amount\", \"memo\") values (" +
        transaction.user_id + ", '" +
        transaction.transaction_date + "', " +
        transaction.category_id + ", '" +
        transaction.category + "', '" +
        transaction.account + "', '" +
        transaction.payee + "', " +
        transaction.amount + ", '" +
        transaction.memo + "') returning transaction_id;"
      println("[QUERY] " + query)
      val sql_statement = AppConstants.SqlConnection.createStatement()
      sql_statement.execute(query)
      val result = sql_statement.getResultSet()
      result.next()
      sender() ! "" + result.getInt("transaction_id")
    case UpdateTransaction(transaction) =>
      val query = "update transactions set " +
        "transaction_date = '" + transaction.transaction_date + "', " +
        "category_id = " + transaction.category_id + ", " +
        "category = '" + transaction.category + "', " +
        "account = '" + transaction.account + "', " +
        "payee = '" + transaction.payee + "', " +
        "amount = " + transaction.amount + ", " +
        "memo = '" + transaction.memo + "' " +
        "where transaction_id = " + transaction.transaction_id.get + ";"
      println("[QUERY] " + query)
      AppConstants.SqlConnection.createStatement().executeUpdate(query)
      sender() ! s"true"
    case DeleteTransaction(transaction_id) =>
      val query = "delete from transactions where transaction_id = " + transaction_id + ";"
      println("[QUERY] " + query)
      AppConstants.SqlConnection.createStatement().executeUpdate(query)
      sender() ! s"true"
  }
}