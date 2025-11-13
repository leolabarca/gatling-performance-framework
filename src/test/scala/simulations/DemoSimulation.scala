package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scenarios.demo.PostsScenario
import config.BaseConfig
import scala.concurrent.duration._

class DemoSimulation extends Simulation {

  before {
    println("\n📚 DEMO API PERFORMANCE TEST")
    BaseConfig.printConfig()
  }

  val httpProtocol = http
    .baseUrl(BaseConfig.baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Gatling-Performance-Framework/1.0")
    .shareConnections

  setUp(
    PostsScenario.scn.inject(
      rampUsers(BaseConfig.users).during(BaseConfig.rampUp.seconds)
    ).protocols(httpProtocol)
  )
    .maxDuration(BaseConfig.duration.seconds)
    .assertions(
      global.responseTime.max.lt(3000),
      global.responseTime.percentile3.lte(BaseConfig.p95Threshold),
      global.successfulRequests.percent.gt(90)
    )

  after {
    println("=" * 50)
    println("📚 Demo Simulation completed!")
    println("=" * 50)
  }
}