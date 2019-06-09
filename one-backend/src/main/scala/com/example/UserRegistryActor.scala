package com.example

import java.sql.ResultSet

import akka.actor.{ Actor, ActorLogging, Props }

final case class User(username: String, password: String)
final case class Users(users: Seq[User])

object UserRegistryActor {
  final case class UserActionPerformed(description: String)
  final case class GetUsers(users: ResultSet)
  final case class CreateUser(user: User)
  final case class GetUser(name: String)
  final case class DeleteUser(name: String)

  def props: Props = Props[UserRegistryActor]
}

class UserRegistryActor extends Actor with ActorLogging {
  import UserRegistryActor._

  var users = Set.empty[User]

  def receive: Receive = {
    case GetUsers(users) => {
      var dbUsers = Set.empty[User]
      while (users.next()) {
        dbUsers += new User(users.getString("username"), users.getString("password"))
      }
      sender() ! Users(dbUsers.toSeq)
    }
    case CreateUser(user) =>
      println(user)
      users += user
      sender() ! UserActionPerformed(s"User ${user.username} with password ${user.password} created.")
    case GetUser(name) =>
      sender() ! users.find(_.username == name)
    case DeleteUser(name) =>
      users.find(_.username == name) foreach { user => users -= user }
      sender() ! UserActionPerformed(s"User ${name} deleted.")
  }
}