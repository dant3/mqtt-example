package com.github.dant3.mqttexample.application.base

import android.app.Application
import com.github.dant3.mqttexample.application.FatalExceptionHandler

trait AppExceptionHandler extends Application { self:Application ⇒
  protected def shutdownHooks:Seq[FatalExceptionHandler.ShutdownHook] = Seq.empty

  override def onCreate(): Unit = {
    super.onCreate()
    FatalExceptionHandler.setup(shutdownHooks)
  }
}
