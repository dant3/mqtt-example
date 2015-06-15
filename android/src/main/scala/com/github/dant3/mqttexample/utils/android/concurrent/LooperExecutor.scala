package com.github.dant3.mqttexample.utils.android.concurrent

import android.os.{Handler, Looper}
import com.github.dant3.mqttexample.utils.Logger

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class LooperExecutor(looper:Looper) extends ExecutionContextExecutor with ScalaExecutor with Logger {
  protected val handler = new Handler(looper)
  val thread = looper.getThread
  val context:ExecutionContext = this

  override def execute(command: Runnable): Unit = {
    if (Thread.currentThread() == thread) {
      command.run()
    } else {
      handler.post(command)
    }
  }
  override def reportFailure(cause: Throwable): Unit = log.warn("Failure: ", cause)
}