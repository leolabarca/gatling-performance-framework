package scenarios.walmart

import config.BaseConfig
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.DebugUtils

object WalmartAuthScenario {

  // Autenticación con credenciales del config
  val authenticate =
    DebugUtils.logRequest("LOGIN")
      .exec(
        http("LOGIN")
          .post("/api/auth")
          .headers(BaseConfig.commonHeaders)
          .body(StringBody(
            s"""{
               |  "username": "${BaseConfig.username}",
               |  "password": "${BaseConfig.password}"
               |}""".stripMargin
          ))
          .check(status.is(200))
          .check(jsonPath("$.data.token").saveAs("authToken"))
          .check(bodyString.saveAs("loginResponse"))
      )
      .exec { session =>
        println("=" * 80)
        println("🔐 LOGIN RESPONSE:")
        println(session("loginResponse").asOption[String].getOrElse("No response body"))
        println(s"🔑 Token extraído: ${session("authToken").asOption[String].getOrElse("No token found")}")
        println("=" * 80)
        session
      }
      .exec(DebugUtils.logResponse)

  // Escenario completo de autenticación
  val scn = scenario("Walmart Authentication")
    .exec(authenticate)
}