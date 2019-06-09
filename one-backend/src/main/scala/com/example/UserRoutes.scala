//package com.example
//
//import java.sql.{ DriverManager, ResultSet }
//
//import akka.actor.{ ActorRef, ActorSystem }
//import akka.event.Logging
//
//import scala.concurrent.duration._
//import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, StatusCodes }
//import akka.http.scaladsl.server.Route
//import akka.http.scaladsl.server.directives.MethodDirectives.delete
//import akka.http.scaladsl.server.directives.MethodDirectives.get
//import akka.http.scaladsl.server.directives.MethodDirectives.post
//import akka.http.scaladsl.server.directives.RouteDirectives.complete
//import akka.http.scaladsl.server.directives.PathDirectives.path
//
//import scala.concurrent.Future
//import com.example.UserRegistryActor._
//import akka.pattern.ask
//import akka.util.Timeout
//
////#user-routes-class
//trait UserRoutes extends JsonSupport with CORSHandler {
//  //#user-routes-class
//
//  // we leave these abstract, since they will be provided by the App
//  implicit def system: ActorSystem
//
//  lazy val log = Logging(system, classOf[UserRoutes])
//
//  // other dependencies that UserRoutes use
//  def userRegistryActor: ActorRef
//
//  // Required by the `ask` (?) method below
//  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration
//
//  var sql_connection: java.sql.Connection = null
//
//  Class.forName("org.postgresql.ds.PGSimpleDataSource") // Load the driver
//
//  sql_connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ONE", "timothytqin", "tq05112001") // Make the connection
//
//  lazy val userRoutes: Route =
//    corsHandler(
//      pathPrefix("users") {
//        pathEnd {
//          get {
//            val users: Future[User] =
//              (userRegistryActor ? GetUser).mapTo[User]
//            complete(users)
//          } ~
//            post {
//              entity(as[User]) { user =>
//                val userCreated: Future[UserActionPerformed] =
//                  (userRegistryActor ? CreateUser(user)).mapTo[UserActionPerformed]
//                onSuccess(userCreated) { performed =>
//
//                  val i: UserActionPerformed = performed
//                  val query = "insert into users values ('" + user.username + "', '" + user.password + "');"
//                  println("QUERY: " + query)
//                  sql_connection.createStatement().executeUpdate(query)
//                  log.info("Created user [{}]: {}", user.username, performed.description)
//                  complete((StatusCodes.Created, performed))
//                }
//              }
//            }
//        }
//      })
//}
