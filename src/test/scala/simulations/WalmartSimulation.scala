package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scenarios.walmart.WalmartProductsScenario
import config.BaseConfig
import scala.concurrent.duration._

class WalmartSimulation extends Simulation {

  // ================================
  // HTTP Protocol
  // ================================
  val httpProtocol = http
    .baseUrl(BaseConfig.baseUrl)
    .headers(BaseConfig.commonHeaders)

  // ================================
  // PRE — Antes de ejecutar
  // ================================
  before {
    println("\n🏪 WALMART PERFORMANCE TEST — Distributed Mode")
    BaseConfig.printConfig()
  }

  // ================================
  // SETUP — Inyección distribuida
  // ================================
  setUp(
    WalmartProductsScenario.scn.inject(
      rampUsers(BaseConfig.usersForThisShard)
        .during(BaseConfig.rampUp.seconds)
    )
  )
    .protocols(httpProtocol)
    .maxDuration(BaseConfig.duration.seconds)
    .assertions(
      global.responseTime.percentile3.lt(BaseConfig.p95Threshold),  // P95 threshold
      global.responseTime.percentile4.lt(BaseConfig.p99Threshold),  // P99 threshold
      global.successfulRequests.percent.gt(BaseConfig.successRate)  // Success rate
    )

  // ================================
  // POST — Después de la prueba
  // ================================
  after {
    println("=" * 60)
    println("🏁 Walmart Simulation completeeeeeed!")
    println("📝 Revisa el HTML report (simulación + shard aplicado)")
    println("=" * 60)
  }

}
