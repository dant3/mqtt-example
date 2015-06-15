package com.github.dant3.mqttexample.utils.android.concurrent

import com.github.dant3.mqttexample.utils.Logger

import scala.concurrent.ExecutionContextExecutor

object SameThreadExecutor extends ExecutionContextExecutor with ScalaExecutor with Logger {
  val context = this

  override def reportFailure(cause: Throwable): Unit = log.warn("Failure! ", cause)

  override def execute(command: Runnable): Unit = command.run()
}
