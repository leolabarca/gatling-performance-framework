package scenarios.walmart

import utils.DebugUtils
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.BaseConfig

object WalmartSanityScenario {

  // Sanity check: Solo login (request más crítico)
  val sanityCheck =
    DebugUtils.logRequest("SANITY - Login")
      .exec(
        http("Sanity Check - Login")
          .post("/api/auth")
          .headers(BaseConfig.commonHeaders)
          .body(StringBody(
            s"""{
               |  "username": "${BaseConfig.username}",
               |  "password": "${BaseConfig.password}"
               |}""".stripMargin
          ))
          .check(status.is(200))
          .check(jsonPath("$.data.token").exists)
          .check(responseTimeInMillis.lte(3000)) // Max 3 segundos
      )
      .exec { session =>
        println("=" * 80)
        println("✅ SANITY CHECK PASSED - System is UP and responding")
        println("=" * 80)
        session
      }
      .exec(DebugUtils.logResponse)

  // Escenario sanity
  val scn = scenario("Walmart Sanity Check")
    .exec(sanityCheck)
}