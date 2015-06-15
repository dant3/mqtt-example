package com.github.dant3.mqttexample

import android.app.Activity
import android.content.{Context, Intent}

trait Navigation { context:Context ⇒


  def startActivity[T <: Activity](implicit manifest:Manifest[T]):Unit = {
    context.startActivity(startActivityIntent[T](manifest))
  }

  def startActivityIntent[T <: Activity](implicit manifest:Manifest[T]):Intent = {
    new Intent(context, manifest.runtimeClass)
  }
}
