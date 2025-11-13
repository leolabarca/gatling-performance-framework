package scenarios.demo

import config.BaseConfig
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.DebugUtils

object DemoSanityScenario {

  // Sanity check: Solo un GET simple
  val sanityCheck =
    DebugUtils.logRequest("SANITY - Get Post")
      .exec(
        http("Sanity Check - Get Post")
          .get("/posts/1")
          .headers(BaseConfig.commonHeaders)
          .check(status.is(200))
          .check(jsonPath("$.id").is("1"))
          .check(responseTimeInMillis.lte(2000))
      )
      .exec { session =>
        println("=" * 80)
        println("✅ SANITY CHECK PASSED - API is UP and responding")
        println("=" * 80)
        session
      }
      .exec(DebugUtils.logResponse)

  val scn = scenario("Demo API Sanity Check")
    .exec(sanityCheck)
}