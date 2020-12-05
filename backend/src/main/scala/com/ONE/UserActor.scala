package com.ONE

import java.sql.ResultSet

import akka.actor.{ Actor, ActorLogging, Props }

final case class User(user_id: Int, username: String, email: String, password: String, phoneNumber: String)
final case class UserUpdate(user_id: Int, username: String, email: String, password: String, phoneNumber: String)

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


  // Migrate the table name out into constants file
  def receive: Receive = {
    case CreateUser(user) =>
      val query = "insert into users (\"username\", \"password\", \"email\", \"phoneNumber\") values ('" + user.username + "', '" + EncryptUtil.getMd5(user.password) + "', '" + user.email + "', '" + user.phoneNumber + "');"
      println("[QUERY] " + query)
      val isSuccessful = AppConstants.SqlConnection.createStatement().execute(query)
      sender() ! (if(isSuccessful)  s"true" else s"false")
    case UpdateUser(user) =>
      val query = "update users set " +
        "username = '" + user.username + "', " +
        "email = '" + user.email + "', " +
        "\"password\" = '" + EncryptUtil.getMd5(user.password) + "', " +
        "\"phoneNumber\" = '" + user.phoneNumber + "' " +
        "where user_id = " + user.user_id + ";"
      println("[QUERY] " + query)
      AppConstants.SqlConnection.createStatement().executeUpdate(query)
      println("[UPDATED USER] " + user.username)
      sender() ! s"true"
    case GetUser(username) =>
      val query = "select * from users where username = '" + username + "';"
      println("[QUERY] " + query)
      val result = AppConstants.SqlConnection.createStatement().executeQuery(query)
      result.next()
      val user = User(result.getInt("user_id"), result.getString("username"), result.getString("email"), result.getString("password"), result.getString("phoneNumber"))
      println("[USER] " + user)
      sender() ! user
    case DeleteUser(name) =>
      sender() ! UserActionPerformed(s"User ${name} deleted.")
  }
}