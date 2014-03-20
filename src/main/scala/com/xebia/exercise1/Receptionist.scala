package com.xebia
package exercise1

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

import akka.util.Timeout
import akka.actor.{ActorRef, Props}

import spray.routing._
import spray.httpx.SprayJsonSupport._


class Receptionist extends HttpServiceActor
                      with ReverseRoute {
  implicit def executionContext = context.dispatcher

  def createChild(props: Props, name: String) = context.system.actorOf(props, name)

  def receive = runRoute(reverseRoute)
}

trait ReverseRoute extends HttpService {
  // we need this so we can use Futures and Timeout
  implicit def executionContext: ExecutionContext

  def createChild(props:Props, name:String): ActorRef

  val reverseActor = createChild(ReverseActor.props, ReverseActor.name)

  def reverseRoute: Route = path("reverse") {
    post {
      entity(as[ReverseRequest]) { request =>
        implicit val timeout = Timeout(20 seconds)

        import akka.pattern.ask

        //TODO replace the next line by asking the actor to Reverse
        //and converting (hint: mapping) the resulting Future[ReverseResult] to a Future[ReverseResponse]
        val futureResponse = Future.successful(ReverseResponse(request.value.reverse))

        complete(futureResponse)
      }
    }
  }
}
