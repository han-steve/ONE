package com.example

import java.math.BigInteger
import java.security.{MessageDigest, NoSuchAlgorithmException}

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.example.BankActor._
import com.example.ContactActor._
import com.example.TransactionActor._
import com.example.UserActor._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait Routes extends JsonSupport with CORSHandler {

  lazy val log = Logging(system, classOf[Routes])

  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val userActor: ActorRef = system.actorOf(UserActor.props, "userRegistryActor")
  val transactionActor: ActorRef = system.actorOf(TransactionActor.props, "transactionActor")
  val contactActor: ActorRef = system.actorOf(ContactActor.props, "contactActor")
  val bankActor: ActorRef = system.actorOf(BankActor.props, "bankActor")
  // Required by the `ask` (?) method below

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport =
    EntityStreamingSupport.json()
  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  lazy val routes: Route =
    corsHandler(
      pathPrefix("users") {
        pathPrefix("signup") {
          post {
            println("[PATH] users/signup")
            entity(as[User]) { req =>
              val userCreated: Future[User] =
                (userActor ? CreateUser(req)).mapTo[User]
              onComplete(userCreated) {
                case Success(value) =>
                  println("[CREATED USER] " + value)
                  complete(s"User was successfully created.")
                case Failure(exception) =>
                  complete(s"This username is taken. Please enter a different username.")
              }
            }
          }
        } ~
          pathPrefix("login") {
            extractUnmatchedPath { remaining =>
              val str = s"$remaining"
              if (str.length() != 0) {
                val username = str.substring(1)
                println("[PATH] users/login/" + username)
                get {
                  val user: Future[User] =
                    (userActor ? GetUser(username)).mapTo[User]
                  complete(201, user)
                }
              } else
                complete(s"This user does not exist.")
            }
          } ~
          pathPrefix("update") {
            put {
              extractUnmatchedPath { remaining =>
                val str = s"$remaining"
                if (str.length() != 0) {
                  val username = str.substring(1)
                  println("[PATH] users/update/" + str)
                  entity(as[UserUpdate]) { user =>
                    val encrypted = EncryptUtil.getMd5(user.password_after)
                    val userUpdate: Future[UserActionPerformed] =
                      (userActor ? UpdateUser(user)).mapTo[UserActionPerformed]
                    onSuccess(userUpdate) { performed =>
                      val query = "update users set " +
                        "username = '" + user.username_after + "', " +
                        "email = '" + user.email_after + "', " +
                        "\"password\" = '" + encrypted + "', " +
                        "\"phoneNumber\" = '" + user.phoneNumber_after + "' " +
                        "where username = '" + username + "';"
                      println("[QUERY] " + query)
                      SqlUtil.SqlConnection.createStatement().executeUpdate(query)
                      val updateTransactionsQuery = "update transactions set username = '" + user.username_after + "' where username = '" + user.username_before + "';"
                      println("[QUERY] " + updateTransactionsQuery)
                      SqlUtil.SqlConnection.createStatement().executeUpdate(updateTransactionsQuery)
                      log.info("User update complete [{}]: {}", user.username_after, performed.description)
                      complete((StatusCodes.Created, performed))
                    }
                  }
                }
                else
                  complete(s"This username has already been taken.")
              }
            }
          } ~ extractUnmatchedPath { remaining =>
          val str = s"$remaining"
          if (str.length() != 0) {
            val username = str.substring(1)
            println("[PATH] users/" + username)
            post {
              entity(as[User]) { req =>
                val encrypted = EncryptUtil.getMd5(req.password)
                val user: Future[User] = (userActor ? GetUser(username)).mapTo[User]
                complete(user.map(u => encrypted.equals(u.password).toString))
              }
            }
          } else
            complete(s"This user does not exist.")
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
                  val query = "insert into transactions values ('" + transaction.username + "', '" +
                    transaction.transaction_date + "', '" +
                    transaction.category + "', '" +
                    transaction.payee + "', " +
                    transaction.amount + ", '" +
                    transaction.memo + "', '" +
                    transaction.account + "') returning id;"
                  println("[QUERY] " + query)
                  val sql_statement = SqlUtil.SqlConnection.createStatement()
                  sql_statement.execute(query)
                  val result = sql_statement.getResultSet()
                  result.next()
                  log.info("Transaction complete [{}]: {}", transaction.username, performed.description)
                  complete((StatusCodes.Created, "" + result.getInt("id")))
                }
              }
            } ~
            delete {
              entity(as[Transaction]) { transaction =>
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
                    "account = '" + transaction.account + "'"
                  println("[QUERY] " + query)
                  SqlUtil.SqlConnection.createStatement().executeUpdate(query)
                  log.info("Transaction complete [{}]: {}", transaction.username, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            } ~
            put {
              entity(as[TransactionUpdate]) { transaction =>
                val transactionMade: Future[TransactionActionPerformed] =
                  (transactionActor ? UpdateTransaction()).mapTo[TransactionActionPerformed]
                onSuccess(transactionMade) { performed =>
                  val query = "update transactions set " +
                    "transaction_date = '" + transaction.transaction_date + "', " +
                    "category = '" + transaction.category + "', " +
                    "payee = '" + transaction.payee + "', " +
                    "amount = " + transaction.amount + ", " +
                    "memo = '" + transaction.memo + "', " +
                    "account = '" + transaction.account + "' " +
                    "where id = " + transaction.id + ";"
                  println("[QUERY] " + query)
                  SqlUtil.SqlConnection.createStatement().executeUpdate(query)
                  //                  log.info("Transaction complete [{}]: {}", transaction.username, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            }
        } ~ extractUnmatchedPath { remaining =>
          val str = s"$remaining"
          if (str.length() != 0) {
            val username = str.substring(1)
            val query = "select * from transactions where username = '" + username + "' order by transaction_date desc;"
            println("[QUERY] " + query)
            val result = SqlUtil.SqlConnection.createStatement().executeQuery(query)
            val transaction: Future[Transactions] =
              (transactionActor ? GetTransactions(result)).mapTo[Transactions]
            complete(201, transaction)
          } else
            complete(s"Unmatched: '$remaining'")
        }
      } ~ pathPrefix("contact") {
        pathEnd {
          post {
            entity(as[Contact]) { contact =>
              val contactMade: Future[ContactActionPerformed] =
                (contactActor ? MakeContact(contact)).mapTo[ContactActionPerformed]
              onSuccess(contactMade) { performed =>
                val query = "insert into contact values ('" + contact.username + "', '" +
                  contact.contact_date + "', '" +
                  contact.email + "', '" +
                  contact.comment + "'); "
                println("[QUERY] " + query)
                val sql_statement = SqlUtil.SqlConnection.createStatement()
                sql_statement.execute(query)
                //                val result = sql_statement.getResultSet()
                //                result.next()
                log.info("Transaction complete [{}]: {}", contact.username, performed.description)
                complete((StatusCodes.Created, "Contact saved."))
              }
            }
          }
        }
      } ~ pathPrefix("bank") {
        get {
          pathEnd {
            val options: Future[HttpResponse] = (bankActor ? GetPlaidOptions()).mapTo[HttpResponse]
            complete(options)
          } ~ pathPrefix("transactions") {
            extractUnmatchedPath { remaining =>
              var access_token = s"$remaining".substring(1)
              var response: Future[HttpResponse] =
                (bankActor ? GetBankTransactions(access_token)).mapTo[HttpResponse]
              complete(response)
            }
          } ~ extractUnmatchedPath { remaining =>
            val username = s"$remaining".substring(1)
            val query = "select * from bank where username = '" + username + "';"
            println("[QUERY] " + query)
            val result = SqlUtil.SqlConnection.createStatement().executeQuery(query)
            val connections: Future[Banks] =
              (bankActor ? GetConnectedBanks(result)).mapTo[Banks]
            complete(201, connections)
          }
        } ~ post {
          pathPrefix("get_access_token") {
            entity(as[BankAccessToken]) { token =>
              val response: Future[HttpResponse] =
                (bankActor ? GetAccessToken(token)).mapTo[HttpResponse]
              complete(response)
            }
          } ~ pathPrefix("set_access_token") {
            entity(as[BankAccessToken]) { token =>
              val response: Future[HttpResponse] =
                (bankActor ? GetAccessToken(token)).mapTo[HttpResponse]
              complete(response)
            }
          } ~ pathPrefix("new") {
            entity(as[Bank]) { connection =>
              val connectionMade: Future[BankActionPerformed] =
                (bankActor ? AddBankConnection(connection)).mapTo[BankActionPerformed]
              onSuccess(connectionMade) { performed =>
                val query = "insert into bank values ('" + connection.username + "', '" +
                  connection.access_token + "', '" + connection.item_id + "', '" + connection.primary_key + "'); ";
                println("[QUERY] " + query)
                val sql_statement = SqlUtil.SqlConnection.createStatement()
                sql_statement.execute(query)
                complete((StatusCodes.Created, "Connection added."))
              }

            }
          }
        }
      } ~ get {
        path("one") {
          getFromResource("one/index.html")
        } ~ pathPrefix("one") {
          getFromResourceDirectory("one")
        }
      })

}
