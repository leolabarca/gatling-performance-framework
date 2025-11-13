package scenarios.walmart

import config.BaseConfig
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{DebugUtils, ThinkTimeUtils}

object WalmartProductsScenario {

  // Obtener productos (requiere token de autenticación)
  val getProducts =
    DebugUtils.logRequest("GET Products")
      .exec(
        http("GET Products")
          .get("/api/products")
          .header("Authorization", "Bearer ${authToken}")
          .headers(BaseConfig.commonHeaders)
          .check(status.is(200))
          .check(jsonPath("$.data.items").exists)
          .check(bodyString.saveAs("productsResponse"))
      )
      .exec { session =>
        println("=" * 80)
        println("📦 GET PRODUCTS RESPONSE:")
        println(session("productsResponse").asOption[String].getOrElse("No response body"))
        println("=" * 80)
        session
      }
      .exec(DebugUtils.logResponse)
      .pause(ThinkTimeUtils.randomThinkTime)

  // Escenario completo: Auth + Get Products
  val scn = scenario("Walmart Auth and Products")
    .exec(WalmartAuthScenario.authenticate) // Reutiliza la autenticación
    .exec(getProducts)
}