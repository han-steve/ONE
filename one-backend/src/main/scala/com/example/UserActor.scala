package com.example

import java.sql.ResultSet

import akka.actor.{ Actor, ActorLogging, Props }

final case class User(username: String, email: String, password: String, phoneNumber: String)
final case class UserUpdate(username_before: String, email_before: String, password_before: String, phoneNumber_before: String, username_after: String, email_after: String, password_after: String, phoneNumber_after: String)

object UserActor {
  final case class GetUser(username: String)
  final case class UserActionPerformed(description: String)
  final case class CreateUser(user: User)
  final case class UpdateUser(user: UserUpdate)
  final case class DeleteUser(name: String)

  def props: Props = Props[UserActor]

}

class UserActor extends Actor with ActorLogging {
  import UserActor._

  def receive: Receive = {
    case CreateUser(user) =>
      val query = "insert into users values ('" + user.username + "', '" + EncryptUtil.getMd5(user.password) + "', '" + user.email + "', '" + user.phoneNumber + "');"
      println("[QUERY] " + query)
      SqlUtil.SqlConnection.createStatement().execute(query)
      sender() ! UserActionPerformed(s"User ${user.username} with password ${user.password} created.")
    case UpdateUser(user) =>
      println("[UPDATED USER] " + user)
      sender() ! UserActionPerformed(s"User ${user.username_after} updated profile")
    case GetUser(username) =>
      val query = "select * from users where username = '" + username + "';"
      println("[QUERY] " + query)
      val result = SqlUtil.SqlConnection.createStatement().executeQuery(query)
      result.next()
//      var phone_Number = ""
//      if (user.getString("phoneNumber") != null)
//        phone_Number = user.getString("phoneNumber")
      println("[NEW USER] " + User(result.getString("username"), result.getString("email"), result.getString("password"), result.getString("phoneNumber")))
      sender() ! User(result.getString("username"), result.getString("email"), result.getString("password"), result.getString("phoneNumber"))
    case DeleteUser(name) =>
      sender() ! UserActionPerformed(s"User ${name} deleted.")
  }
}