package com.github.dant3.mqttexample.utils.avatars

import java.security.MessageDigest

import android.net.Uri

object Gravatar {
  def apply(email:String, pxSize:Int = 0, default:Default = Default.`404`):String = uri(email, pxSize, default)
  def uri(email:String, pxSize:Int, default:Default) = {
    val params = if (pxSize > 0) {
      queryParams("size" -> pxSize.toString, "default" -> default.value)
    } else {
      queryParams("default" -> default.value)
    }
    s"http://www.gravatar.com/avatar/${md5(email)}?$params"
  }


  trait Default {
    def value:String
  }

  object Default {
    def uri(uri:String) = Default(Uri.encode(uri))
    val `404` = Default("404")
    val identicon = Default("identicon")
    val monsteroid = Default("monsteroid")
    val wavatar = Default("wavatar")
    val retro = Default("retro")

    private def apply(queryValue:String) = new Default {
      override def value: String = queryValue
    }
  }

  private def queryParams(params:(String, String)*):String = queryParams(Map(params:_*))
  private def queryParams(params:Map[String, String]):String = params.map({
    case (key, value) => key + "=" + value
  }).mkString("&")


  def md5(string:String) = {
    def hex(array:Array[Byte]) = {
      val sb = new StringBuffer()
      for (i <- 0 to array.length - 1) {
        sb.append(Integer.toHexString((array(i) & 0xFF) | 0x100).substring(1,3))
      }
      sb.toString
    }

    val md5Digest = MessageDigest.getInstance("MD5")
    hex(md5Digest.digest(string.getBytes("CP1252")))
  }
}
