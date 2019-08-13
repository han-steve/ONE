package com.ONE

import java.sql.ResultSet
import java.util.Date

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import com.plaid.client.PlaidClient
import com.plaid.client.request.{AccountsGetRequest, ItemGetRequest, ItemPublicTokenExchangeRequest, TransactionsGetRequest}
import com.plaid.client.response.{AccountsGetResponse, ErrorResponse, ItemGetResponse, ItemPublicTokenExchangeResponse, TransactionsGetResponse}
import retrofit2.Call
import retrofit2.Callback
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

  val APP_PORT = 8080
  val PLAID_CLIENT_ID = "5d0fb7795a4c3e0012b150bf"
  val PLAID_SECRET = "7f264190cf743e9abd4a758b3d392a"
  val PLAID_PUBLIC_KEY = "fdf561c3029d9d9e59acfe5b34a67f"
  val PLAID_ENV = "development"

  // PLAID_PRODUCTS is a comma-separated list of products to use when initializing
  // Link. Note that this list must contain 'assets' in order for the app to be
  // able to create and retrieve asset reports.
  val PLAID_PRODUCTS = "transactions"

  // PLAID_PRODUCTS is a comma-separated list of countries for which users
  // will be able to select institutions from.
  val PLAID_COUNTRY_CODES = "US,CA,GB,FR,ES"

  val PLAID_TRANSACTION_COUNT = 500

  // We store the access_token in memory - in production, store it in a secure
  // persistent data store
  private var ACCESS_TOKEN: String = s"access-sandbox-baf6d8d7-e73f-4740-9e98-e41d96bf3e6e"
  private var PUBLIC_TOKEN: String = null
  private var ITEM_ID: String = s""

  private var accounts: Map[String, String] = Map()

  // Initialize the Plaid client
  // Find your API keys in the Dashboard (https://dashboard.plaid.com/account/keys)
  val plaidClient: PlaidClient = PlaidClient.newBuilder.clientIdAndSecret(PLAID_CLIENT_ID, PLAID_SECRET).publicKey(PLAID_PUBLIC_KEY).developmentBaseUrl // optional. only needed to call endpoints that require a public key
    .build // or equivalent, depending on which environment you're calling into

  def props: Props = Props[BankActor]
}

class BankActor extends Actor with ActorLogging {
  import BankActor._

  def receive: Receive = {
    case GetPlaidOptions() => {
      val json = s"""{ "PLAID_PUBLIC_KEY": "$PLAID_PUBLIC_KEY",
        "PLAID_ENV": "$PLAID_ENV",
        "PLAID_PRODUCTS": "$PLAID_PRODUCTS",
        "PLAID_COUNTRY_CODES": "$PLAID_COUNTRY_CODES" }"""
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
        val transactionResponse: Response[TransactionsGetResponse] = plaidClient.service().transactionsGet(new TransactionsGetRequest(access_token, new Date(0), new Date()).withCount(PLAID_TRANSACTION_COUNT).withOffset(totalTransactions)).execute()
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
          bankTransactions += s"""{"transaction_id": """" + e.getTransactionId + s"""", "transaction_date": """" + e.getDate + s"""", "category_id": """" + e.getCategoryId + s"""", "category": """" + category + s"""", "amount": """ + amount + s""", "account":"""" + accounts.get(e.getAccountId) + s"""", "payee": """" + e.getName + """"}"""
          totalTransactions += 1
        }
        if(transactionsList.size() != PLAID_TRANSACTION_COUNT)
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
      PUBLIC_TOKEN = token.public_token
      val itemTokenExchangeResponse: Response[ItemPublicTokenExchangeResponse] = plaidClient.service().itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(PUBLIC_TOKEN)).execute()
      ACCESS_TOKEN = itemTokenExchangeResponse.body().getAccessToken
      ITEM_ID = itemTokenExchangeResponse.body().getItemId
      val accountResponse: Response[AccountsGetResponse] = plaidClient.service().accountsGet(new AccountsGetRequest(ACCESS_TOKEN)).execute()
      accountResponse.body().getAccounts.forEach(e => {
        accounts += (e.getAccountId -> e.getName)
      })
      val response: HttpResponse = HttpResponse(entity = HttpEntity(ContentTypes.`application/json`,
        s"""{ "access_token": "$ACCESS_TOKEN",
        "item_id": "$ITEM_ID" }"""))
      sender() ! response
    }
    case SetAccessToken(token: BankAccessToken) => {
      ACCESS_TOKEN = token.public_token
      val itemResponse: Response[ItemGetResponse] = plaidClient.service().itemGet(new ItemGetRequest(ACCESS_TOKEN)).execute()
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