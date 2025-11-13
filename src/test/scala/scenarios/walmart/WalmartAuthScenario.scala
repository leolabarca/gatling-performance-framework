package scenarios.walmart

import utils.{DebugUtils, DataFeeders}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.BaseConfig

object WalmartAuthScenario {

  // Autenticación usando datos del CSV feeder
  val authenticate =
    feed(DataFeeders.walmartCredentialsFeeder) // ← Carga username/password del CSV
      .exec(DebugUtils.logRequest("LOGIN"))
      .exec { session =>
        println(s"[FEEDER] Using credentials from CSV:")
        println(s"  Username: ${session("username").as[String]}")
        println(s"  Password: ${session("password").as[String]}")
        session
      }
      .exec(
        http("LOGIN")
          .post("/api/auth")
          .headers(BaseConfig.commonHeaders)
          .body(StringBody(
            """{
              |  "username": "#{username}",
              |  "password": "#{password}"
              |}""".stripMargin
          ))
          .check(status.is(200))
          .check(jsonPath("$.data.token").saveAs("authToken"))
          .check(bodyString.saveAs("loginResponse"))
      )
      .exec { session =>
        println("=" * 80)
        println(s"🔐 LOGIN RESPONSE for user: ${session("username").as[String]}")
        println(session("loginResponse").asOption[String].getOrElse("No response body"))
        println(s"🔑 Token extraído: ${session("authToken").asOption[String].getOrElse("No token found")}")
        println("=" * 80)
        session
      }
      .exec(DebugUtils.logResponse)

  // Escenario completo de autenticación (data-driven)
  val scn = scenario("Walmart Authentication")
    .exec(authenticate)
}