package com.xebia
package exercise1

import akka.actor.{ActorRefFactory, Props}

import spray.testkit.Specs2RouteTest
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._

import org.specs2.mutable.Specification


class ReceptionistSpec extends Specification
                          with Specs2RouteTest {

  val subject = new ReverseRoute {
    implicit def actorRefFactory: ActorRefFactory = system
    implicit def executionContext = system.dispatcher

    def createChild(props: Props, name: String) = system.actorOf(props, name)
  }

  "The Receptionist" should {
    "Respond with a JSON response that contains a reversed string value" in {

      Post("/reverse", ReverseRequest("some text to reverse")) ~> subject.reverseRoute ~> check {
        status === StatusCodes.OK
        val response = entityAs[ReverseResponse]
        response.value must beEqualTo("esrever ot txet emos")
      }

    }
  }
}
