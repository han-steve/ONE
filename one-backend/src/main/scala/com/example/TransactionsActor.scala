package com.example

import java.sql.ResultSet

import akka.actor.{ Actor, ActorLogging, Props }

final case class Transaction(username: String, transaction_date: String, category: String, payee: String, amount: Double, memo: String, account: String)
final case class TransactionUpdate(username: String, transaction_date_before: String, category_before: String, payee_before: String, amount_before: Double, memo_before: String, account_before: String,
  transaction_date_after: String, category_after: String, payee_after: String, amount_after: Double, memo_after: String, account_after: String)
final case class Transactions(transactions: Seq[Transaction])

object TransactionActor {
  final case class TransactionActionPerformed(description: String)
  final case class GetTransactions(transactions: ResultSet)
  final case class MakeTransaction(transaction: Transaction)
  final case class UpdateTransaction(transaction: TransactionUpdate)
  final case class GetTransaction(name: String)
  final case class DeleteTransaction(transaction: Transaction)

  def props: Props = Props[TransactionActor]
}

class TransactionActor extends Actor with ActorLogging {
  import TransactionActor._

  //  var transactions = Set.empty[Transaction]

  def receive: Receive = {
    case GetTransactions(transactions) => {
      var dbTransactions = Set.empty[Transaction]
      while (transactions.next()) {
        println(transactions.getString("id"))
        dbTransactions += new Transaction(transactions.getString("username"), transactions.getString("transaction_date"), transactions.getString("category"),
          transactions.getString("payee"), transactions.getDouble("amount"), transactions.getString("memo"), transactions.getString("account"))
      }
      sender() ! Transactions(dbTransactions.toSeq)
    }
    case MakeTransaction(transaction) =>
      //      transactions += transaction
      sender() ! TransactionActionPerformed(s"User ${transaction.username} made transaction of $$" + transaction.amount + " on " + transaction.transaction_date)
    case UpdateTransaction(transaction) =>
      sender() ! TransactionActionPerformed(s"User ${transaction.username} made transaction")
    //    case GetTransaction(name) =>
    //      sender() ! transactions.find(_.username == name)
    case DeleteTransaction(transaction) =>
      sender() ! TransactionActionPerformed(s"Transaction made by ${transaction.username} on ${transaction.transaction_date} to ${transaction.payee} deleted.")
  }
}