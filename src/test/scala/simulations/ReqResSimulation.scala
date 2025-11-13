package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scenarios.demo.UsersScenario
import config.BaseConfig
import scala.concurrent.duration._

class ReqResSimulation extends Simulation {

  before {
    println("\n👥 REQRES API CRUD TEST")
    BaseConfig.printConfig()
  }

  val httpProtocol = http
    .baseUrl(BaseConfig.baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Gatling-Performance-Framework/1.0")
    .shareConnections

  setUp(
    UsersScenario.scn.inject(
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
    println("👥 ReqRes Simulation completed!")
    println("=" * 50)
  }
}