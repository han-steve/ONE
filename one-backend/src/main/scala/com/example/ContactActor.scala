package com.example

import java.sql.ResultSet

import akka.actor.{ Actor, ActorLogging, Props }

final case class Contact(username: String, contact_date: String, email: String, comment: String)

object ContactActor {
  final case class ContactActionPerformed(description: String)
  final case class MakeContact(comment: Contact)

  def props: Props = Props[ContactActor]
}

class ContactActor extends Actor with ActorLogging {
  import ContactActor._

  def receive: Receive = {
    case MakeContact(contact) =>
      sender() ! ContactActionPerformed(s"User ${contact.username} made a comment: " + contact.comment + " on " + contact.contact_date)
  }
}