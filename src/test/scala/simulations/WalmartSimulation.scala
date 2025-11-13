package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scenarios.walmart.WalmartProductsScenario  // ← Actualizar import
import config.BaseConfig
import scala.concurrent.duration._

class WalmartSimulation extends Simulation {

  // Imprimir configuración al inicio
  before {
    println("\n🏪 WALMART PERFORMANCE TEST")
    BaseConfig.printConfig()
  }

  // Configuración del protocolo HTTP
  val httpProtocol = http
    .baseUrl(BaseConfig.baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Gatling-Performance-Framework/1.0")
    .shareConnections

  // Setup de la simulación
  setUp(
    WalmartProductsScenario.scn.inject(
      rampUsers(BaseConfig.users).during(BaseConfig.rampUp.seconds)
    ).protocols(httpProtocol)
  )
    .maxDuration(BaseConfig.duration.seconds)
    .assertions(
      global.responseTime.max.lt(5000),
      global.successfulRequests.percent.gt(95)
    )

  // Hook después de la simulación
  after {
    println("=" * 50)
    println("🏪 Walmart Simulation completed!")
    println("Check the HTML report for detailed results")
    println("=" * 50)
  }
}