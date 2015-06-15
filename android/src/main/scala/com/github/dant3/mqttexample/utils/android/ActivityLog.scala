package com.github.dant3.mqttexample.utils.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.github.dant3.mqttexample.utils.Logger

trait ActivityLog extends Activity with Logger {
  override def onCreate(savedInstanceState: Bundle): Unit = {
    log.debug("onCreate({})", savedInstanceState)
    super.onCreate(savedInstanceState)
  }

  override def onPause(): Unit = {
    log.debug("onPause()")
    super.onPause()
  }

  override def onNewIntent(intent: Intent): Unit = {
    log.debug("onNewIntent({})", intent)
    super.onNewIntent(intent)
  }

  override def onStart(): Unit = {
    log.debug("onStart()")
    super.onStart()
  }

  override def onBackPressed(): Unit = {
    log.debug("onBackPressed()")
    super.onBackPressed()
  }

  override def onSaveInstanceState(outState: Bundle): Unit = {
    log.debug("onSaveInstanceState({})", outState)
    super.onSaveInstanceState(outState)
  }

  override def onResume(): Unit = {
    log.debug("onResume()")
    super.onResume()
  }

  override def onRestart(): Unit = {
    log.debug("onRestart()")
    super.onRestart()
  }

  override def onPostResume(): Unit = {
    log.debug("onPostResume()")
    super.onPostResume()
  }

  override def onPostCreate(savedInstanceState: Bundle): Unit = {
    log.debug("onPostCreate({})", savedInstanceState)
    super.onPostCreate(savedInstanceState)
  }

  override def onStop(): Unit = {
    log.debug("onStop()")
    super.onStop()
  }

  override def onNavigateUp(): Boolean = {
    log.debug("onNavigateUp()")
    super.onNavigateUp()
  }

  override def onDestroy(): Unit = {
    log.debug("onDestroy()")
    super.onDestroy()
  }

  override def onNavigateUpFromChild(child: Activity): Boolean = {
    log.debug("onNavigateUpFromChild({})", child)
    super.onNavigateUpFromChild(child)
  }
}
