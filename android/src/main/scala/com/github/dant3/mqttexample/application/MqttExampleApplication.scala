package com.github.dant3.mqttexample.application

import android.app.Application
import com.github.dant3.mqttexample.BuildConfig
import com.github.dant3.mqttexample.application.base.{AppExceptionHandler, AppLogger, MultiDexApplication}

class MqttExampleApplication extends Application with MultiDexApplication with AppLogger with AppExceptionHandler  {
  override def logcatTag: String = "AMBER"

  override def onCreate() = {
    super.onCreate()
    log.debug("Application started. Version = {}, Debug = {}", BuildConfig.VERSION_NAME, BuildConfig.DEBUG)
  }
}