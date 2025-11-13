package scenarios.demo

import utils.{DebugUtils, ThinkTimeUtils}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.BaseConfig

object PostsScenario {

  // GET: Obtener todos los posts
  val getAllPosts =
    DebugUtils.logRequest("GET All Posts")
      .exec(
        http("GET All Posts")
          .get("/posts")
          .headers(BaseConfig.commonHeaders)
          .check(status.is(200))
          .check(jsonPath("$[*].id").findAll.saveAs("postIds"))
          .check(jsonPath("$[0].id").saveAs("firstPostId"))
      )
      .exec { session =>
        val postIds = session("postIds").as[Vector[String]]
        println(s"[INFO] Found ${postIds.size} posts")
        session
      }
      .exec(DebugUtils.logResponse)
      .pause(ThinkTimeUtils.shortThinkTime)

  // GET: Obtener un post específico
  val getPostById =
    DebugUtils.logRequest("GET Post by ID")
      .exec(
        http("GET Post by ID")
          .get("/posts/${firstPostId}")
          .headers(BaseConfig.commonHeaders)
          .check(status.is(200))
          .check(jsonPath("$.id").exists)
          .check(jsonPath("$.title").saveAs("postTitle"))
          .check(jsonPath("$.body").exists)
          .check(responseTimeInMillis.lte(BaseConfig.p95Threshold))
      )
      .exec { session =>
        println(s"[INFO] Post title: ${session("postTitle").as[String]}")
        session
      }
      .exec(DebugUtils.logResponse)
      .pause(ThinkTimeUtils.shortThinkTime)

  // GET: Obtener comentarios de un post
  val getPostComments =
    DebugUtils.logRequest("GET Post Comments")
      .exec(
        http("GET Post Comments")
          .get("/posts/${firstPostId}/comments")
          .headers(BaseConfig.commonHeaders)
          .check(status.is(200))
          .check(jsonPath("$[*].email").findAll.saveAs("commentEmails"))
      )
      .exec { session =>
        val emails = session("commentEmails").as[Vector[String]]
        println(s"[INFO] Found ${emails.size} comments")
        session
      }
      .exec(DebugUtils.logResponse)
      .pause(ThinkTimeUtils.mediumThinkTime)

  // POST: Crear un nuevo post
  val createPost =
    DebugUtils.logRequest("CREATE Post")
      .exec(
        http("CREATE Post")
          .post("/posts")
          .headers(BaseConfig.commonHeaders)
          .body(StringBody(
            """{
              |  "title": "Performance Test Post",
              |  "body": "This is a test post created by Gatling",
              |  "userId": 1
              |}""".stripMargin
          ))
          .check(status.is(201))
          .check(jsonPath("$.id").saveAs("newPostId"))
      )
      .exec { session =>
        println(s"[SUCCESS] Created post with ID: ${session("newPostId").as[String]}")
        session
      }
      .exec(DebugUtils.logResponse)

  // Escenario completo de navegación de posts
  val scn = scenario("Posts CRUD Flow")
    .exec(getAllPosts)
    .exec(getPostById)
    .exec(getPostComments)
    .exec(createPost)
}