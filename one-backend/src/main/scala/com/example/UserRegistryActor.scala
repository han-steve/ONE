package com.example

import java.sql.ResultSet

import akka.actor.{ Actor, ActorLogging, Props }
import com.example.TransactionActor.{ TransactionActionPerformed, UpdateTransaction }

final case class User(username: String, email: String, password: String, phoneNumber: String)
final case class UserUpdate(username_before: String, email_before: String, password_before: String, phoneNumber_before: String, username_after: String, email_after: String, password_after: String, phoneNumber_after: String)
final case class Users(users: Seq[User])

object UserRegistryActor {
  final case class UserActionPerformed(description: String)
  final case class GetUsers(users: ResultSet)
  final case class CreateUser(user: User)
  final case class UpdateUser(user: UserUpdate)
  final case class GetUser(users: ResultSet)
  final case class DeleteUser(name: String)

  def props: Props = Props[UserRegistryActor]
}

class UserRegistryActor extends Actor with ActorLogging {
  import UserRegistryActor._

  def receive: Receive = {
    case GetUsers(users) => {
      var dbUsers = Set.empty[User]
      while (users.next()) {
        var phone_Number = ""
        if (users.getString("phoneNumber") != null)
          phone_Number = users.getString("phoneNumber")
        dbUsers += new User(users.getString("username"), users.getString("email"), users.getString("password"), phone_Number)
      }
      println(dbUsers)
      sender() ! Users(dbUsers.toSeq)
    }
    case CreateUser(user) =>
      println("[CREATED USER] " + user)
      sender() ! UserActionPerformed(s"User ${user.username} with password ${user.password} created.")
    case UpdateUser(user) =>
      println("[UPDATED USER] " + user)
      sender() ! UserActionPerformed(s"User ${user.username_after} updated profile")
    case GetUser(user) =>
      user.next();
      var phone_Number = ""
      if (user.getString("phoneNumber") != null)
        phone_Number = user.getString("phoneNumber")
      sender() ! new User(user.getString("username"), user.getString("email"), user.getString("password"), phone_Number)
    case DeleteUser(name) =>
      sender() ! UserActionPerformed(s"User ${name} deleted.")
  }
}