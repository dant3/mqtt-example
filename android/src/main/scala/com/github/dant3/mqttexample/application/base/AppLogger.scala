package com.github.dant3.mqttexample.application.base

import android.app.Application
import com.github.dant3.mqttexample.utils.Logger

trait AppLogger extends Application with Logger { self:Application ⇒
  def logcatTag:String

  override def onCreate(): Unit = {
    super.onCreate()
    Logger.setup(logcatTag)
  }
}
