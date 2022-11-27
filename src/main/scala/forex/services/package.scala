package forex

package object services {
  type RatesService[F[_]] = rates.OneFrame[F]
  final val RatesServices = rates.Interpreters
  type CacheService[F[_]] = cache.Cache[F]
  final val CacheServices = cache.Interpreters
  type CronJobService[F[_]] = cronJob.CronJob[F]
  final val CronJobServices = cronJob.Interpreters
}
