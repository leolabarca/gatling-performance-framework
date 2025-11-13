package utils

import scala.concurrent.duration._
import scala.util.Random

object ThinkTimeUtils {

  private val random = new Random()

  // Think time aleatorio entre 2 y 7 segundos (como en tu script)
  def randomThinkTime: FiniteDuration = {
    (random.nextInt(5001) + 2000).milliseconds
  }

  // Think time aleatorio customizable
  def randomThinkTime(minMs: Int, maxMs: Int): FiniteDuration = {
    (random.nextInt(maxMs - minMs + 1) + minMs).milliseconds
  }

  // Think times predefinidos comunes
  def shortThinkTime: FiniteDuration = (random.nextInt(2001) + 1000).milliseconds // 1-3s
  def mediumThinkTime: FiniteDuration = (random.nextInt(4001) + 3000).milliseconds // 3-7s
  def longThinkTime: FiniteDuration = (random.nextInt(6001) + 5000).milliseconds // 5-11s
}