package config

import com.typesafe.config.ConfigFactory

object BaseConfig {

  private val config = ConfigFactory.load()

  // =====================
  // Environment selection
  // =====================
  // ENV is convenient to switch between blocks in application.conf (e.g., walmart, staging, prod)
  val environment: String = sys.env.getOrElse("ENV", "walmart")

  // Base URL
  // Priority: ENV VAR BASE_URL > application.conf environments.<env>.baseUrl > default
  val baseUrl: String = sys.env.get("BASE_URL")
    .orElse({
      try {
        Some(config.getString(s"environments.$environment.baseUrl"))
      } catch {
        case _: Throwable => None
      }
    })
    .getOrElse("https://example.org")

  // =====================
  // Load configuration
  // =====================
  // USERS (total across all shards), DURATION (seconds), RAMP_UP (seconds)
  val users: Int = sys.env.get("USERS")
    .map(_.toInt)
    .orElse({
      try Some(config.getInt("load.users")) catch { case _: Throwable => None }
    })
    .getOrElse(100)

  val duration: Int = sys.env.get("DURATION")
    .map(_.toInt)
    .orElse({
      try Some(config.getInt("load.duration")) catch { case _: Throwable => None }
    })
    .getOrElse(300)

  val rampUp: Int = sys.env.get("RAMP_UP")
    .map(_.toInt)
    .orElse({
      try Some(config.getInt("load.rampUp")) catch { case _: Throwable => None }
    })
    .getOrElse(60)

  // =====================
  // Thresholds (assertions)
  // =====================
  // p95, p99 in ms; successRate in percentage (0-100)
  val p95Threshold: Int = sys.env.get("P95_MS")
    .map(_.toInt)
    .orElse({
      try Some(config.getInt("thresholds.p95")) catch { case _: Throwable => None }
    })
    .getOrElse(1500)

  val p99Threshold: Int = sys.env.get("P99_MS")
    .map(_.toInt)
    .orElse({
      try Some(config.getInt("thresholds.p99")) catch { case _: Throwable => None }
    })
    .getOrElse(3000)

  val successRate: Double = sys.env.get("SUCCESS_RATE")
    .map(_.toDouble)
    .orElse({
      try Some(config.getDouble("thresholds.successRate")) catch { case _: Throwable => None }
    })
    .getOrElse(99.0)

  // =====================
  // Auth (optional)
  // =====================
  val username: String = sys.env.get("USERNAME")
    .orElse({
      try Some(config.getString(s"environments.$environment.username")) catch { case _: Throwable => None }
    })
    .getOrElse("default_user")

  val password: String = sys.env.get("PASSWORD")
    .orElse({
      try Some(config.getString(s"environments.$environment.password")) catch { case _: Throwable => None }
    })
    .getOrElse("default_password")

  // =====================
  // Common headers
  // =====================
  val commonHeaders: Map[String, String] = Map(
    "Accept" -> "application/json",
    "User-Agent" -> "Gatling-Performance-Framework/1.0"
  )

  // =====================
  // Sharding (Distributed)
  // =====================
  // SHARDS: total number of injectors; SHARD_INDEX: 0..(SHARDS-1)
  val shards: Int = sys.env.get("SHARDS").map(_.toInt).getOrElse(1)
  val shardIndex: Int = sys.env.get("SHARD_INDEX").map(_.toInt).getOrElse(0)

  // Users assigned to THIS shard (near-uniform split)
  val usersForThisShard: Int = {
    val base = users / shards
    val extra = if (shardIndex < (users % shards)) 1 else 0
    base + extra
  }

  // Run identifier (use same value on all shards to correlate a single run)
  val runId: String = sys.env.getOrElse("RUN_ID", java.util.UUID.randomUUID.toString)

  def printConfig(): Unit = {
    println("=" * 60)
    println(s"ENV: $environment")
    println(s"Base URL: $baseUrl")
    println(s"Users (total): $users")
    println(s"Shard: ${shardIndex + 1}/$shards")
    println(s"Users (this shard): $usersForThisShard")
    println(s"Duration: $duration s")
    println(s"Ramp-up: $rampUp s")
    println(s"Thresholds -> P95: ${p95Threshold}ms | P99: ${p99Threshold}ms | SuccessRate: $successRate%")
    if (username != "default_user") println(s"Username: $username")
    println(s"RUN_ID: $runId")
    println("=" * 60)
  }
}
