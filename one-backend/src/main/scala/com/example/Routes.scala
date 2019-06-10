package com.example

import java.nio.charset.StandardCharsets
import java.sql.{ DriverManager, ResultSet }
import java.security.MessageDigest
//import org.apache.commons.codec.digest.DigestUtils

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{ get, post }
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import com.example.TransactionActor._
import com.example.UserRegistryActor._

import scala.concurrent.Future
import scala.concurrent.duration._

trait Routes extends JsonSupport with CORSHandler {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[Routes])

  def userRegistryActor: ActorRef
  def transactionActor: ActorRef

  // Required by the `ask` (?) method below
  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  var sql_connection: java.sql.Connection = null

  Class.forName("org.postgresql.ds.PGSimpleDataSource") // Load the driver

  sql_connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ONE", "timothytqin", "tq05112001") // Make the connection

  lazy val routes: Route =
    corsHandler(
      pathPrefix("users") {
        pathEnd {
          get {
            val query = "select * from users;"
            println("[QUERY] " + query)
            val result = sql_connection.createStatement().executeQuery(query)
            val users: Future[Users] =
              (userRegistryActor ? GetUsers(result)).mapTo[Users]
            complete(201, users)
          } ~
            post {
              entity(as[User]) { user =>
                val userCreated: Future[UserActionPerformed] =
                  (userRegistryActor ? CreateUser(user)).mapTo[UserActionPerformed]
                onSuccess(userCreated) { performed =>
                  val query = "insert into users values ('" + user.username + "', '" + user.password + "', '" + user.email + "', '" + user.phoneNumber + "');"
                  println("[QUERY] " + query)
                  sql_connection.createStatement().executeUpdate(query)
                  //                  log.info("Created user [{}]: {}", user.username, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            } ~
            put {
              entity(as[UserUpdate]) { user =>
                val userUpdate: Future[UserActionPerformed] =
                  (userRegistryActor ? UpdateUser(user)).mapTo[UserActionPerformed]
                onSuccess(userUpdate) { performed =>
                  val query = "update users set " +
                    "username = '" + user.username_after + "', " +
                    "email = '" + user.email_after + "', " +
                    "\"password\" = '" + user.password_after + "', " +
                    "\"phoneNumber\" = '" + user.phoneNumber_after + "' " +
                    "where username = '" + user.username_before + "';"
                  println("[QUERY] " + query)
                  sql_connection.createStatement().executeUpdate(query)
                  sql_connection.createStatement().executeUpdate("update transactions set username = '" + user.username_after + "' where username = '" + user.username_before + "';")
                  log.info("User update complete [{}]: {}", user.username_after, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            }
        } ~ extractUnmatchedPath { remaining =>
          val str = s"$remaining";
          if (str.length() != 0) {
            val username = str.substring(1);
            val query = "select * from users where username = '" + username + "';"
            println("[QUERY] " + query)
            val result = sql_connection.createStatement().executeQuery(query)
            val user: Future[User] =
              (userRegistryActor ? GetUser(result)).mapTo[User]
            complete(201, user)
          } else
            complete(s"Unmatched: '$remaining'")
        }
      } ~ pathPrefix("transactions") {
        pathEnd {
          get {
            val transaction: Future[Transactions] =
              (transactionActor ? GetTransactions).mapTo[Transactions]
            complete(transaction)
          } ~
            post {
              entity(as[Transaction]) { transaction =>
                val transactionMade: Future[TransactionActionPerformed] =
                  (transactionActor ? MakeTransaction(transaction)).mapTo[TransactionActionPerformed]
                onSuccess(transactionMade) { performed =>
                  val query = "insert into transactions values ('" + transaction.username + "', " +
                    transaction.transaction_date + ", '" +
                    transaction.category + "', '" +
                    transaction.payee + "', " +
                    transaction.amount + ", '" +
                    transaction.memo + "', '" +
                    transaction.account + "')";
                  println("[QUERY] " + query)
                  sql_connection.createStatement().executeUpdate(query)
                  log.info("Transaction complete [{}]: {}", transaction.username, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            } ~
            delete {
              entity(as[Transaction]) { transaction =>
                println(transaction)
                val transactionMade: Future[TransactionActionPerformed] =
                  (transactionActor ? DeleteTransaction(transaction)).mapTo[TransactionActionPerformed]
                onSuccess(transactionMade) { performed =>
                  val query = "delete from transactions where username = '" + transaction.username + "' and " +
                    //                    "id = " + transaction.id +
                    "transaction_date = '" + transaction.transaction_date + "' and " +
                    "category = '" + transaction.category + "' and " +
                    "payee = '" + transaction.payee + "' and " +
                    "amount = " + transaction.amount + " and " +
                    "memo = '" + transaction.memo + "' and " +
                    "account = '" + transaction.account + "'";
                  println("[QUERY] " + query)
                  sql_connection.createStatement().executeUpdate(query)
                  log.info("Transaction complete [{}]: {}", transaction.username, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            } ~
            put {
              entity(as[TransactionUpdate]) { transaction =>
                println(transaction)
                val transactionMade: Future[TransactionActionPerformed] =
                  (transactionActor ? UpdateTransaction(transaction)).mapTo[TransactionActionPerformed]
                onSuccess(transactionMade) { performed =>
                  val query = "update transactions set " +
                    "transaction_date = '" + transaction.transaction_date_after + "', " +
                    "category = '" + transaction.category_after + "', " +
                    "payee = '" + transaction.payee_after + "', " +
                    "amount = " + transaction.amount_after + ", " +
                    "memo = '" + transaction.memo_after + "', " +
                    "account = '" + transaction.account_after + "' " +
                    "where username = '" + transaction.username + "' and " +
                    "transaction_date = '" + transaction.transaction_date_before + "' and " +
                    "category = '" + transaction.category_before + "' and " +
                    "payee = '" + transaction.payee_before + "' and " +
                    "amount = " + transaction.amount_before + " and " +
                    "memo = '" + transaction.memo_before + "' and " +
                    "account = '" + transaction.account_before + "'"
                  println("[QUERY] " + query)
                  sql_connection.createStatement().executeUpdate(query)
                  log.info("Transaction complete [{}]: {}", transaction.username, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            }
        } ~ extractUnmatchedPath { remaining =>
          val str = s"$remaining";
          if (str.length() != 0) {
            val username = str.substring(1);
            val query = "select * from transactions where username = '" + username + "' order by transaction_date desc;"
            println("[QUERY] " + query)
            val result = sql_connection.createStatement().executeQuery(query)
            val transaction: Future[Transactions] =
              (transactionActor ? GetTransactions(result)).mapTo[Transactions]
            complete(201, transaction)
          } else
            complete(s"Unmatched: '$remaining'")
        }
      })
  //    private val sfSalt = "9e107d9d372bb6826bd81d3542a419d6"
  //    def encrypt(theText: String): String = {
  //      DigestUtils.md5Hex(theText + sfSalt)
  //    }
}
