package com.xebia
package exercise1

import akka.actor.{Actor, Props}

object ReverseActor {
    sealed trait Message
    case class Reverse(value: String) extends Message
    case class ReverseResult(value: String) extends Message

    val props = Props[ReverseActor]
    val name = "reveseActor"
}

class ReverseActor extends Actor {
  import ReverseActor._

  def receive = {
      case Reverse(value) => sender ! ReverseResult(value.reverse)
  }

}
