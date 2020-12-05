package com.ONE

import java.util.Date

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import com.plaid.client.request.{AccountsGetRequest, ItemGetRequest, ItemPublicTokenExchangeRequest, TransactionsGetRequest}
import com.plaid.client.response.{AccountsGetResponse, ItemGetResponse, ItemPublicTokenExchangeResponse, TransactionsGetResponse}
import retrofit2.Response

final case class Bank(user_id: Int, connection_id: Option[Int] =  None, access_token: String, item_id: String, primary_key: String)
final case class Banks(banks: Seq[Bank])
final case class BankAccessToken(public_token: String)

object BankActor {
  final case class BankActionPerformed(description: String)
  final case class GetPlaidOptions()
  final case class GetBankTransactions(access_token: String)
  final case class GetAccessToken(access_token: BankAccessToken)
  final case class SetAccessToken(access_token: BankAccessToken)
  final case class AddBankConnection(connection: Bank)
  final case class GetConnectedBanks(user_id: Int)

  var accounts: Map[String, String] = Map()

  def props: Props = Props[BankActor]
}

class BankActor extends Actor with ActorLogging {
  import BankActor._

  def receive: Receive = {
    case GetPlaidOptions() => {
      val json = "{ \"PLAID_PUBLIC_KEY\": \"" + AppConstants.PLAID_PUBLIC_KEY + "\", " +
        "\"PLAID_ENV\" : \"" + AppConstants.PLAID_ENV + "\", " +
        "\"PLAID_PRODUCTS\" : \"" + AppConstants.PLAID_PRODUCTS + "\", " +
        "\"PLAID_COUNTRY_CODES\" : \"" + AppConstants.PLAID_COUNTRY_CODES + "\" }"
      val response: HttpResponse = HttpResponse(entity = HttpEntity(
        ContentTypes.`application/json`,
        json))
      sender() ! response
    }
    case GetBankTransactions(access_token: String) => {
      var bankTransactions = s"""{"transactions": ["""
      var receivedAllTransactions = false
      var totalTransactions = 0
      while(!receivedAllTransactions) {
        val transactionResponse: Response[TransactionsGetResponse] = AppConstants.plaidClient.service().transactionsGet(new TransactionsGetRequest(access_token, new Date(0), new Date()).withCount(AppConstants.PLAID_TRANSACTION_COUNT).withOffset(totalTransactions)).execute()
        val transactionsList = transactionResponse.body().getTransactions
        var i = 0
        for(i <- 0 to transactionsList.size() - 1) {
          val e = transactionsList.get(i)
          var category = ""
          var j = 0
          for(j <- 0 to e.getCategory.size() - 1) {
            category += e.getCategory.get(j)
            if(j != e.getCategory.size() - 1)
              category += ", "
          }
          var amount = -1 * e.getAmount
          if(totalTransactions != 0)
            bankTransactions += s""","""
          bankTransactions += s"""{"transaction_id": "${e.getTransactionId}", "transaction_date": "${e.getDate}", "category_id": "${e.getCategoryId}", "category": "${category}", "amount": ${amount}, "account_id": "${e.getAccountId}", "account": "${accounts.get(e.getAccountId)}", "payee": "${e.getName}"}"""
          totalTransactions += 1
        }
        if(transactionsList.size() != AppConstants.PLAID_TRANSACTION_COUNT)
          receivedAllTransactions = !receivedAllTransactions
      }
      bankTransactions += s"""]}""";
      println(bankTransactions)
      val response: HttpResponse = HttpResponse(entity = HttpEntity(
        ContentTypes.`application/json`,
        bankTransactions))
      sender() ! response
    }
    case GetAccessToken(token: BankAccessToken) => {
      AppConstants.PUBLIC_TOKEN = token.public_token
      val itemTokenExchangeResponse: Response[ItemPublicTokenExchangeResponse] = AppConstants.plaidClient.service().itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(AppConstants.PUBLIC_TOKEN)).execute()
      AppConstants.ACCESS_TOKEN = itemTokenExchangeResponse.body().getAccessToken
      AppConstants.ITEM_ID = itemTokenExchangeResponse.body().getItemId
      val accountResponse: Response[AccountsGetResponse] = AppConstants.plaidClient.service().accountsGet(new AccountsGetRequest(AppConstants.ACCESS_TOKEN)).execute()
      accountResponse.body().getAccounts.forEach(e => {
        accounts += (e.getAccountId -> e.getName)
      })
      val response: HttpResponse = HttpResponse(entity = HttpEntity(ContentTypes.`application/json`,
        "{ \"access_token\" : \"" + AppConstants.ACCESS_TOKEN + "\", " +
          "\"item_id\" : \"" + AppConstants.ITEM_ID + "\" }"))
      sender() ! response
    }
    case SetAccessToken(token: BankAccessToken) => {
      AppConstants.ACCESS_TOKEN = token.public_token
      val itemResponse: Response[ItemGetResponse] = AppConstants.plaidClient.service().itemGet(new ItemGetRequest(AppConstants.ACCESS_TOKEN)).execute()
      val itemId: String = itemResponse.body().getItem.getItemId
      val response: HttpResponse = HttpResponse(entity = HttpEntity(ContentTypes.`application/json`,
        s"""{ "item_id": "$itemId" }"""))
      sender() ! response
    }
    case AddBankConnection(connection: Bank) => {
      val query = "insert into banks (\"user_id\", \"access_token\", \"item_id\", \"primary_key\") values ('" + connection.user_id + "', '" +
        connection.access_token + "', '" + connection.item_id + "', '" + connection.primary_key + "'); ";
      println("[QUERY] " + query)
      val sql_statement = AppConstants.SqlConnection.createStatement()
      sql_statement.execute(query)
      sender() ! s"true"
    }
    case GetConnectedBanks(user_id: Int) => {
      var connectedBanks = Set.empty[Bank]
      val query = "select * from banks where user_id = '" + user_id + "';"
      println("[QUERY] " + query)
      val result = AppConstants.SqlConnection.createStatement().executeQuery(query)
      while(result.next()) {
        connectedBanks += new Bank(result.getInt("user_id"), Option(result.getInt("connection_id")), result.getString("access_token"), result.getString("item_id"), result.getString("primary_key"))
      }
      sender() ! Banks(connectedBanks.toSeq)
    }
  }
}