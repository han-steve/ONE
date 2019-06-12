package com.example

import java.sql.ResultSet

import akka.actor.{ Actor, ActorLogging, Props }

final case class Transaction(id: Int, username: String, transaction_date: String, category: String, payee: String, amount: Double, memo: String, account: String)
final case class TransactionUpdate(id: Int, transaction_date: String, category: String, payee: String, amount: Double, memo: String, account: String)
final case class Transactions(transactions: Seq[Transaction])

object TransactionActor {
  final case class TransactionActionPerformed(description: String)
  final case class GetTransactions(transactions: ResultSet)
  final case class MakeTransaction(transaction: Transaction)
  final case class UpdateTransaction()
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
        dbTransactions += new Transaction(transactions.getInt("id"), transactions.getString("username"), transactions.getString("transaction_date"), transactions.getString("category"),
          transactions.getString("payee"), transactions.getDouble("amount"), transactions.getString("memo"), transactions.getString("account"))
      }
      sender() ! Transactions(dbTransactions.toSeq)
    }
    case MakeTransaction(transaction) =>
      //      transactions += transaction
      sender() ! TransactionActionPerformed(s"User ${transaction.username} made transaction of $$" + transaction.amount + " on " + transaction.transaction_date)
    case UpdateTransaction() =>
      sender() ! TransactionActionPerformed(s"User made transaction")
    //    case GetTransaction(name) =>
    //      sender() ! transactions.find(_.username == name)
    case DeleteTransaction(transaction) =>
      sender() ! TransactionActionPerformed(s"Transaction made by ${transaction.username} on ${transaction.transaction_date} to ${transaction.payee} deleted.")
  }
}