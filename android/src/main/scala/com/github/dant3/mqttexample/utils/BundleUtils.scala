package com.github.dant3.mqttexample.utils

import _root_.android.os.Bundle

private[utils] trait BundleUtils {
  def bundle(pairs: (String, Any)*): Bundle = {
    fillBundle(new Bundle())(pairs:_*)
  }

  def fillBundle(bundle: Bundle)(pairs: (String, Any)*): Bundle = {
    pairs.foreach(putPair(dest = bundle))
    bundle
  }

  private def putPair(dest:Bundle)(keyValue:(String, Any)) = put(dest, keyValue._1, keyValue._2)
  private def put(dest:Bundle, key: String, value:Any):Unit = value match {
    case string:String => dest.putString(key, string)
    case int:Int => dest.putInt(key, int)
    case long:Long ⇒ dest.putLong(key, long)
    case boolean:Boolean => dest.putBoolean(key, boolean)
  }

  implicit class BundleOperators(`this`: Bundle) {
    def + (that: Bundle) = {
      val c = new Bundle
      c.putAll(`this`)
      c.putAll(that)
      c
    }

    def += (that: Bundle) = {
      `this`.putAll(that)
      `this`
    }
  }
}

object BundleUtils extends BundleUtils