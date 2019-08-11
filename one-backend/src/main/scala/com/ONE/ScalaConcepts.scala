package com.ONE

import java.sql.{ DriverManager, ResultSet }

import org.postgresql.Driver
import slick.jdbc.JdbcBackend._
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext.Implicits.global

//#quick-start-server

//#main-class

//object QuickstartServer extends UserRoutes {

// Scala is expression oriented: Every control returns a value, except for while loop.
object ScalaConcepts extends PostgresProfile {

  def main(args: Array[String]): Unit = {

    var sql_connection: java.sql.Connection = null

    Class.forName("org.postgresql.ds.PGSimpleDataSource") // Load the driver

    sql_connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ONE", "timothytqin", "tq05112001") // Make the connection

    val users: ResultSet = sql_connection.createStatement().executeQuery("select * from users")

    while (users.next()) {
      val row = users.getString("username")

      println("=======!!!!")
      println(s"users: ${row}")
    }

    //    val db = Database.forConfig("onedb")

    //    sqlu()
    //sql""""""

    val squares = for (i <- 0 to 9) yield i * i

    squares.foreach(println)

    println("====")

    val a = if (9 == 8 + 1) {
      //
      "great"
    } else {

      "bad"
    }

    val fn: String => Int = str => Integer.valueOf(str)

    // curried function
    val curriedMultiParamList = multiParamList(_: String)(fn)

    Seq(
      curriedMultiParamList("999"),
      curriedMultiParamList("234")).foreach(println)

    println("====")

    Seq(
      multiParamList("999")(fn),
      multiParamList("234")(fn)).foreach(println)

    println("999".countingToes)

    // the magic of implicit

    //    println {
    //      multiParamList("999") { str: String => Integer.valueOf(str)
    //      }
    //    }

    implicit class MagicString(str: String) {
      def countingToes: String = str + " counting the toes"
    }

  }

  // multiple parameter lists, more natural syntax

  def multiParamList(str: String)(fn: String => Int): Int = {
    fn(str)
  }

  //  def multiParamList(str: String)(fn: String => Int): Int = {
  //    fn(str)
  //  }

  //  // set up ActorSystem and other dependencies here
  //  //#main-class
  //  //#server-bootstrapping
  //  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  //  implicit val materializer: ActorMaterializer = ActorMaterializer()
  //  implicit val executionContext: ExecutionContext = system.dispatcher
  //  //#server-bootstrapping
  //
  //  val userRegistryActor: ActorRef = system.actorOf(UserRegistryActor.props, "userRegistryActor")
  //
  //  //#main-class
  //  // from the UserRoutes trait
  //  lazy val routes: Route = userRoutes
  //  //#main-class
  //
  //  //#http-server
  //  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8080)
  //
  //  serverBinding.onComplete {
  //    case Success(bound) =>
  //      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
  //    case Failure(e) =>
  //      Console.err.println(s"Server could not start!")
  //      e.printStackTrace()
  //      system.terminate()
  //  }
  //
  //  Await.result(system.whenTerminated, Duration.Inf)
  //#http-server
  //#main-class
}

//#main-class
//#quick-start-server
