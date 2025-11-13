package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scenarios.walmart.WalmartSanityScenario
import config.BaseConfig
import scala.concurrent.duration._

class WalmartSanitySimulation extends Simulation {

  before {
    println("\n🔍 WALMART SANITY CHECK")
    println("=" * 50)
    println("Quick validation that system is UP")
    println(s"Environment: ${sys.env.getOrElse("ENV", "walmart-sanity")}")
    println(s"Base URL: ${BaseConfig.baseUrl}")
    println(s"Users: ${BaseConfig.users}")
    println("=" * 50)
  }

  val httpProtocol = http
    .baseUrl(BaseConfig.baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Gatling-Sanity-Check/1.0")

  // Ejecución inmediata de 1 usuario
  setUp(
    WalmartSanityScenario.scn.inject(
      atOnceUsers(1) // 1 usuario inmediato
    ).protocols(httpProtocol)
  )
    .maxDuration(BaseConfig.duration.seconds)
    .assertions(
      global.responseTime.max.lt(3000), // Máximo 3 segundos
      global.successfulRequests.percent.is(100) // Debe ser 100% exitoso
    )

  after {
    println("=" * 50)
    println("🔍 Sanity Check completed!")
    println("=" * 50)
  }
}