package com.github.dant3.mqttexample.utils.android.resources

import android.content.Context

import scala.language.implicitConversions

trait StringRes extends Res[String]

object StringRes {
  implicit def apply(stringID: Int):StringRes = new StringRes {
    override def apply(context: Context): String = context.getResources.getString(stringID)
  }

  implicit def apply(predefinedString:String):StringRes = new StringRes {
    override def apply(context: Context): String = predefinedString
  }
}

