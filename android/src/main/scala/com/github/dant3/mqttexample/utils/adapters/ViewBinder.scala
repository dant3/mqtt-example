package com.github.dant3.mqttexample.utils.adapters

import android.view.{View, ViewGroup}

trait ViewBinder[ItemType >: Null, ViewType <: View] {
  def binder = this
  def createView(position:Int, item:ItemType, parent:ViewGroup):ViewType
  def bindView(position:Int, item:ItemType, view:ViewType):Unit
}
