package com.github.dant3.mqttexample.utils.android.concurrent

import java.util.concurrent.ConcurrentHashMap

import com.github.dant3.mqttexample.utils.IDGenerator

import scala.collection.{JavaConversions, mutable}
import scala.concurrent.Future

class FutureCache[T] {
  private val generateID = new IDGenerator
  private val storage:mutable.Map[Long,Future[T]] = JavaConversions.mapAsScalaMap(new ConcurrentHashMap[Long, Future[T]]())

  def put[U <: T](process:Future[U]):Long = {
    val id = generateID()
    storage.put(id, process)
    implicit val context = SameThreadExecutor.context
    process.onComplete(_ ⇒ releaseProcess(id))
    id
  }
  def get(processID:Long):Option[Future[T]] = storage.get(processID)

  private def releaseProcess(processID:Long):Unit = storage.remove(processID)
}
