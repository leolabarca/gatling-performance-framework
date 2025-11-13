package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scenarios.demo.DemoSanityScenario
import config.BaseConfig
import scala.concurrent.duration._

class DemoSanitySimulation extends Simulation {

  before {
    println("\n🔍 DEMO API SANITY CHECK")
    println("=" * 50)
    println("Quick validation that API is UP")
    println(s"Base URL: ${BaseConfig.baseUrl}")
    println("=" * 50)
  }

  val httpProtocol = http
    .baseUrl(BaseConfig.baseUrl)
    .acceptHeader("application/json")
    .userAgentHeader("Gatling-Sanity-Check/1.0")

  setUp(
    DemoSanityScenario.scn.inject(
      atOnceUsers(1)
    ).protocols(httpProtocol)
  )
    .maxDuration(BaseConfig.duration.seconds)
    .assertions(
      global.responseTime.max.lt(2000),
      global.successfulRequests.percent.is(100)
    )

  after {
    println("=" * 50)
    println("🔍 Sanity Check completed!")
    println("=" * 50)
  }
}