package com.ONE

import java.sql.DriverManager

import akka.actor.ActorSystem
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.plaid.client.PlaidClient

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

private[ONE] object AppConstants {

  Class.forName("org.postgresql.ds.PGSimpleDataSource") // Load the driver

  lazy val SqlConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ONE", "timothytqin", "tq05112001") // Make the connection

  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport =
    EntityStreamingSupport.json()
  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

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
  var ACCESS_TOKEN: String = s"access-sandbox-baf6d8d7-e73f-4740-9e98-e41d96bf3e6e"
  var PUBLIC_TOKEN: String = null
  var ITEM_ID: String = s""


  // Initialize the Plaid client
  // Find your API keys in the Dashboard (https://dashboard.plaid.com/account/keys)
  val plaidClient: PlaidClient = PlaidClient.newBuilder.clientIdAndSecret(PLAID_CLIENT_ID, PLAID_SECRET).publicKey(PLAID_PUBLIC_KEY).developmentBaseUrl // optional. only needed to call endpoints that require a public key
    .build // or equivalent, depending on which environment you're calling into


}
