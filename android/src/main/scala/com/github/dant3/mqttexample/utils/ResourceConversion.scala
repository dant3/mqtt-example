package com.github.dant3.mqttexample.utils

import _root_.android.content.Context
import _root_.android.graphics.Movie
import _root_.android.graphics.drawable.Drawable

private[utils] class ResourceConversion(val id: Int)(implicit context: Context) {
  @inline def r2Text         : CharSequence        = context.getText(id)
  @inline def r2TextArray    : Array[CharSequence] = context.getResources.getTextArray(id)
  @inline def r2String       : String              = context.getResources.getString(id)
  @inline def r2StringArray  : Array[String]       = context.getResources.getStringArray(id)
  @inline def r2Drawable     : Drawable            = context.getResources.getDrawable(id)
  @inline def r2Movie        : Movie               = context.getResources.getMovie(id)
  @inline def r2Color        : Int                 = context.getResources.getColor(id)
  @inline def r2Dimension    : Float               = context.getResources.getDimension(id)
  @inline def r2Integer      : Int                 = context.getResources.getInteger(id)
  @inline def r2IntArray     : Array[Int]          = context.getResources.getIntArray(id)
  @inline def r2RawResource  : java.io.InputStream = context.getResources.openRawResource(id)
}

object ResourceConversion {
  // r2String is not provided because it is ambiguous with r2Text
  @inline implicit def r2Text(id: Int)(implicit context: Context): CharSequence = if (id == 0) null else context.getText(id)
  @inline implicit def r2TextArray(id: Int)(implicit context: Context): Array[CharSequence] =
    if (id == 0) null else context.getResources.getTextArray(id)
  @inline implicit def r2StringArray(id: Int)(implicit context: Context): Array[String] =
    if (id == 0) null else context.getResources.getStringArray(id)
  @inline implicit def r2Drawable(id: Int)(implicit context: Context): Drawable =
    if (id == 0) null else context.getResources.getDrawable(id)
  @inline implicit def r2Movie(id: Int)(implicit context: Context): Movie =
    if (id == 0) null else context.getResources.getMovie(id)

  @inline implicit def toConversions(id:Int)(implicit context: Context): ResourceConversion =
    new ResourceConversion(id)(context)
}