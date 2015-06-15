package com.github.dant3.mqttexample.application.base

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

trait MultiDexApplication extends Application {
  override protected def attachBaseContext(base:Context) = {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }
}
