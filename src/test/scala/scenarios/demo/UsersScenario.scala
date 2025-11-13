package scenarios.demo

import utils.{DebugUtils, ThinkTimeUtils}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.BaseConfig

object UsersScenario {

  // GET: Listar usuarios con paginación
  val listUsers =
    DebugUtils.logRequest("GET Users List")
      .exec(
        http("GET Users List")
          .get("/users?page=1")
          .headers(BaseConfig.commonHeaders)
          .check(status.is(200))
          .check(jsonPath("$.data[*].id").findAll.saveAs("userIds"))
          .check(jsonPath("$.data[0].id").saveAs("firstUserId"))
          .check(jsonPath("$.total").saveAs("totalUsers"))
      )
      .exec { session =>
        println(s"[INFO] Total users: ${session("totalUsers").as[String]}")
        session
      }
      .exec(DebugUtils.logResponse)
      .pause(ThinkTimeUtils.shortThinkTime)

  // GET: Obtener usuario específico
  val getUserById =
    DebugUtils.logRequest("GET User by ID")
      .exec(
        http("GET User by ID")
          .get("/users/${firstUserId}")
          .headers(BaseConfig.commonHeaders)
          .check(status.is(200))
          .check(jsonPath("$.data.email").saveAs("userEmail"))
          .check(jsonPath("$.data.first_name").saveAs("firstName"))
      )
      .exec { session =>
        println(s"[INFO] User: ${session("firstName").as[String]} - ${session("userEmail").as[String]}")
        session
      }
      .exec(DebugUtils.logResponse)
      .pause(ThinkTimeUtils.shortThinkTime)

  // POST: Crear usuario
  val createUser =
    DebugUtils.logRequest("CREATE User")
      .exec(
        http("CREATE User")
          .post("/users")
          .headers(BaseConfig.commonHeaders)
          .body(StringBody(
            """{
              |  "name": "Gatling Tester",
              |  "job": "Performance Engineer"
              |}""".stripMargin
          ))
          .check(status.is(201))
          .check(jsonPath("$.id").saveAs("createdUserId"))
          .check(jsonPath("$.createdAt").exists)
      )
      .exec { session =>
        println(s"[SUCCESS] Created user with ID: ${session("createdUserId").as[String]}")
        session
      }
      .exec(DebugUtils.logResponse)
      .pause(ThinkTimeUtils.shortThinkTime)

  // PUT: Actualizar usuario
  val updateUser =
    DebugUtils.logRequest("UPDATE User")
      .exec(
        http("UPDATE User")
          .put("/users/${createdUserId}")
          .headers(BaseConfig.commonHeaders)
          .body(StringBody(
            """{
              |  "name": "Gatling Tester Updated",
              |  "job": "Senior Performance Engineer"
              |}""".stripMargin
          ))
          .check(status.is(200))
          .check(jsonPath("$.updatedAt").exists)
      )
      .exec(DebugUtils.log("User updated successfully"))
      .exec(DebugUtils.logResponse)
      .pause(ThinkTimeUtils.shortThinkTime)

  // DELETE: Eliminar usuario
  val deleteUser =
    DebugUtils.logRequest("DELETE User")
      .exec(
        http("DELETE User")
          .delete("/users/${createdUserId}")
          .headers(BaseConfig.commonHeaders)
          .check(status.is(204))
      )
      .exec(DebugUtils.log("User deleted successfully"))
      .exec(DebugUtils.logResponse)

  // Escenario completo CRUD
  val scn = scenario("Users CRUD Flow")
    .exec(listUsers)
    .exec(getUserById)
    .exec(createUser)
    .exec(updateUser)
    .exec(deleteUser)
}