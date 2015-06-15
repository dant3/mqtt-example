package com.github.dant3.mqttexample.utils.adapters

import android.view.{View, ViewGroup}

trait ViewBindings[ItemType >: Null] extends TypedAdapter[ItemType] {
  type ViewType <: View
  val viewBinder:ViewBinder[ItemType, ViewType]

  override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
    val item = getItem(position)
    val view:ViewType = convertView match {
      case reusableView:ViewType => reusableView
      case _ => viewBinder.createView(position, item, parent)
    }

    viewBinder.bindView(position, item, view)
    view
  }
}
