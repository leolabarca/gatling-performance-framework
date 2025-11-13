package config

import com.typesafe.config.ConfigFactory

object BaseConfig {

  private val config = ConfigFactory.load()

  // Obtener ambiente desde variable de entorno o usar walmart por defecto
  private val environment = sys.env.getOrElse("ENV", "walmart")

  // URLs base
  val baseUrl: String = config.getString(s"environments.$environment.baseUrl")

  // Configuración de carga
  val users: Int = sys.env.getOrElse("USERS",
    config.getString(s"environments.$environment.users")).toInt

  val duration: Int = sys.env.getOrElse("DURATION",
    config.getString(s"environments.$environment.duration")).toInt

  val rampUp: Int = sys.env.getOrElse("RAMP_UP",
    config.getString(s"environments.$environment.rampUp")).toInt

  // Credenciales (solo para ambientes que las tengan)
  val username: String = try {
    config.getString(s"environments.$environment.username")
  } catch {
    case _: Exception => "default_user"
  }

  val password: String = try {
    config.getString(s"environments.$environment.password")
  } catch {
    case _: Exception => "default_pass"
  }

  // Thresholds
  val p95Threshold: Int = config.getInt("thresholds.responseTime.p95")
  val p99Threshold: Int = config.getInt("thresholds.responseTime.p99")
  val successRate: Double = config.getDouble("thresholds.successRate")

  // Headers comunes
  val commonHeaders = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "User-Agent" -> "Gatling-Performance-Framework/1.0"
  )

  def printConfig(): Unit = {
    println("=" * 50)
    println(s"Environment: $environment")
    println(s"Base URL: $baseUrl")
    println(s"Users: $users")
    println(s"Duration: $duration seconds")
    println(s"Ramp-up: $rampUp seconds")
    if (username != "default_user") println(s"Username: $username")
    println("=" * 50)
  }
}