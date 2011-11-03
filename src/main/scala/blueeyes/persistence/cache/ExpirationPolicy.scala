package blueeyes
package persistence.cache

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.NANOSECONDS

/** An expiration policy defines how an entry expires.
 */
case class ExpirationPolicy private (timeToIdleNanos: Option[Long], timeToLiveNanos: Option[Long]) {
  /** Determines if the policy is eternal (i.e. entry never expires). */
  def eternal: Boolean = timeToIdleNanos == None && timeToLiveNanos == None

  /** Retrieves the time to idle, converted to the specified time unit. */
  def timeToIdle(unit: TimeUnit): Option[Long] = timeToIdleNanos.map(unit.convert(_, NANOSECONDS))

  /** Retrieves the specified time to live, converted to the specified time
   * unit.
   */
  def timeToLive(unit: TimeUnit): Option[Long] = timeToLiveNanos.map(unit.convert(_, NANOSECONDS))

  def isExpired[V](expirable: ExpirableValue[V], currentTime: Long = System.nanoTime()): Boolean = {
    def isPastTime(policyTime: Option[Long], baseTime: Long, currentTime: Long) = policyTime match {
      case Some(policyTime) => currentTime > (policyTime + baseTime)

      case None => false
    }

    !eternal &&
    (isPastTime(timeToIdle(NANOSECONDS), expirable.accessTime(NANOSECONDS),   currentTime) ||
     isPastTime(timeToLive(NANOSECONDS), expirable.creationTime(NANOSECONDS), currentTime))
  }
}

object ExpirationPolicy {
  /** Creates a new policy from the specified time to idle and time to live
   * settings, using the specified time unit.
   */
  def apply(timeToIdle: Option[Long], timeToLive: Option[Long], timeUnit: TimeUnit): ExpirationPolicy = {
    new ExpirationPolicy(timeToIdle.map(timeUnit.toNanos _), timeToLive.map(timeUnit.toNanos _))
  }
}
