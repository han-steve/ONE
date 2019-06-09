//package com.example
//
//import com.example.TransactionActor._
//
//import java.sql.{ DriverManager, ResultSet }
//
//import akka.actor.{ ActorRef, ActorSystem }
//import akka.event.Logging
//
//import scala.concurrent.duration._
//import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, StatusCodes }
//import akka.http.scaladsl.server.directives.MethodDirectives.get
//import akka.http.scaladsl.server.directives.MethodDirectives.post
//import akka.http.scaladsl.server.directives.RouteDirectives.complete
//
//import scala.concurrent.Future
//import akka.pattern.ask
//import akka.util.Timeout
//
////#user-routes-class
//trait TransactionsRoutes extends JsonSupport with CORSHandler {
//  //#user-routes-class
//
//  // we leave these abstract, since they will be provided by the App
//  implicit def system: ActorSystem
//
//  lazy val log = Logging(system, classOf[TransactionsRoutes])
//
//  // other dependencies that UserRoutes use
//  def transactionActor: ActorRef
//
//  // Required by the `ask` (?) method below
//  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration
//
//  //#all-routes
//  //#users-get-post
//  //#users-get-delete
//
//  var sql_connection: java.sql.Connection = null
//
//  Class.forName("org.postgresql.ds.PGSimpleDataSource") // Load the driver
//
//  sql_connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ONE", "timothytqin", "tq05112001") // Make the connection
//
//  object TransactionRouter {
//    val route =
//      //    Route =
//      //      corsHandler(
//      pathPrefix("transactions") {
//        //#users-get-delete
//        pathEnd {
//          get {
//            val transaction: Future[Transactions] =
//              (transactionActor ? GetTransactions).mapTo[Transactions]
//            complete(transaction)
//          } ~
//            post {
//              entity(as[Transaction]) { transaction =>
//                val transactionMade: Future[TransactionActionPerformed] =
//                  (transactionActor ? MakeTransaction(transaction)).mapTo[TransactionActionPerformed]
//                onSuccess(transactionMade) { performed =>
//                  val query = "insert into transactions values ('" + transaction.username + "', " + transaction.transaction_date +
//                    ", '" + transaction.category + "', '" + transaction.payee + "', " + transaction.amount + ", '" + transaction.memo + "', '" + transaction.account + "')";
//                  println("QUERY: " + query)
//                  sql_connection.createStatement().executeUpdate(query)
//                  log.info("Transaction complete [{}]: {}", transaction.username, performed.description)
//                  complete((StatusCodes.Created, performed))
//                }
//              }
//            }
//        }
//      }
//    //        })
//  }
//}
