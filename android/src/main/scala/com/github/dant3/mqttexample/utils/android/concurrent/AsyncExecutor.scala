package com.github.dant3.mqttexample.utils.android.concurrent

import android.os.AsyncTask
import com.github.dant3.mqttexample.utils.Logger

import scala.concurrent.ExecutionContextExecutor

object AsyncExecutor extends ExecutionContextExecutor with ScalaExecutor with Logger {
  val context = this

  override def reportFailure(cause: Throwable): Unit = log.error("Failure! ", cause)

  override def execute(command: Runnable): Unit = {
    AsyncTask.THREAD_POOL_EXECUTOR.execute(command)
  }
}
