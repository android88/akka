package com.xwtec.akka
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout

import scala.concurrent.Future
import java.util.concurrent.TimeUnit

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
class ProcessOrderActor extends Actor {
  implicit val timeout = Timeout(5 seconds)
  val orderActor = context.actorOf(Props[OrderActor])
  val addressActor = context.actorOf(Props[AddressActor])
  val orderAggregateActor = context.actorOf(Props[OrderAggregateActor])

  def receive = {
    /*
    val aggResult: Future[OrderHistory] =
      for {
        order <- (orderActor ? userId).mapTo[Order] // call pattern directly
        address <- (addressActor ? userId).mapTo[Address] // call by implicit conversion
      } yield OrderHistory(order, address)
    aggResult pipeTo orderAggregateActor
    */

    case userId: Integer =>
      val aggResult: Future[OrderHistory] =
        for {
          order <- (orderActor ? userId).mapTo[Order]
          address <- (addressActor ? userId).mapTo[Address]
        } yield OrderHistory(order,address)
      aggResult pipeTo orderAggregateActor
  }
}
