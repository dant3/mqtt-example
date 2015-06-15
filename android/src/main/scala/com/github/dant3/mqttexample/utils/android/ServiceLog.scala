package com.github.dant3.mqttexample.utils.android

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import com.github.dant3.mqttexample.utils.Logger

trait ServiceLog extends Service with Logger {
  override def onCreate(): Unit = {
    log.debug("onCreate()")
    super.onCreate()
  }

  override def onStartCommand(intent:Intent, flags:Int, startId:Int): Int = {
    log.debug("onStart({},{})", intent, flags)
    super.onStartCommand(intent, flags, startId)
  }

  override def onDestroy(): Unit = {
    log.debug("onDestroy()")
    super.onDestroy()
  }

  override def onTaskRemoved(rootIntent: Intent): Unit = {
    log.debug("onTaskRemoved({})", rootIntent)
    super.onTaskRemoved(rootIntent)
  }

  override def onLowMemory(): Unit = {
    log.debug("onLowMemory()")
    super.onLowMemory()
  }

  override def onRebind(intent: Intent): Unit = {
    log.debug("onRebind({})", intent)
    super.onRebind(intent)
  }

  override def onUnbind(intent: Intent): Boolean = {
    log.debug("onUnbind({})", intent)
    super.onUnbind(intent)
  }

  override def onConfigurationChanged(newConfig: Configuration): Unit = {
    log.debug("onConfigurationChanged({})", newConfig)
    super.onConfigurationChanged(newConfig)
  }

  override def onTrimMemory(level: Int): Unit = {
    log.debug("onTrimMemory(level = {})", level)
    super.onTrimMemory(level)
  }
}

