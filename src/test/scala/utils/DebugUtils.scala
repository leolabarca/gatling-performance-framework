package utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object DebugUtils {

  // Lee variable de entorno para activar/desactivar debug
  private val debugEnabled = sys.env.getOrElse("DEBUG", "true").toBoolean

  // Imprime información del request (solo si DEBUG=true)
  def logRequest(requestName: String) = exec { session =>
    if (debugEnabled) {
      println("=" * 80)
      println(s"[REQUEST] $requestName")
      println(s"  Timestamp: ${System.currentTimeMillis()}")
      println(s"  User ID: ${session.userId}")
      println(s"  Session variables: ${session.attributes.mkString(", ")}")
    }
    session
  }

  // Imprime información del response (solo si DEBUG=true)
  def logResponse = exec { session =>
    if (debugEnabled) {
      println(s"[RESPONSE]")
      println(s"  Session variables: ${session.attributes.mkString(", ")}")
      println("=" * 80)
    }
    session
  }

  // Log personalizado con mensaje
  def log(message: String) = exec { session =>
    if (debugEnabled) {
      println(s"[DEBUG] $message")
    }
    session
  }
}