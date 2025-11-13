package utils

import io.gatling.core.Predef._
import scala.util.Random

object DataFeeders {  // ← Cambia aquí de DataFeeders2 a DataFeeders

  // ===== CSV Feeders =====

  val walmartCredentialsFeeder = csv("data/walmart-credentials.csv").circular
  val usersFeeder = csv("data/users.csv").circular
  val usersFeederRandom = csv("data/users.csv").random
  val productsFeeder = csv("data/products.csv").circular
  val productsFeederRandom = csv("data/products.csv").random

  // ===== Custom Feeders =====

  val randomEmailFeeder = Iterator.continually(Map(
    "randomEmail" -> s"user${Random.nextInt(10000)}@test.com"
  ))

  val randomIdFeeder = Iterator.continually(Map(
    "randomId" -> Random.nextInt(100).toString
  ))

  val randomQuantityFeeder = Iterator.continually(Map(
    "quantity" -> (Random.nextInt(10) + 1).toString
  ))

  val timestampFeeder = Iterator.continually(Map(
    "timestamp" -> System.currentTimeMillis().toString
  ))

  val userWithRandomEmailFeeder = Iterator.continually(Map(
    "username" -> s"user${Random.nextInt(1000)}",
    "email" -> s"user${Random.nextInt(10000)}@test.com",
    "firstName" -> s"FirstName${Random.nextInt(100)}",
    "lastName" -> s"LastName${Random.nextInt(100)}"
  ))

  def printFeederData(feederName: String) = exec { session =>
    println(s"[FEEDER] $feederName data loaded for user ${session.userId}")
    session.attributes.foreach { case (key, value) =>
      if (!key.startsWith("gatling.")) {
        println(s"  $key -> $value")
      }
    }
    session
  }
}