package com.github.dant3.mqttexample.ui

import java.text.SimpleDateFormat
import java.util.Date

import android.content.Context
import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import android.view.{ViewGroup, Gravity}
import android.widget.TextView
import com.github.dant3.mqttexample.core.ObservableMqttConnection
import com.github.dant3.mqttexample.utils.android.concurrent.GuiThreadExecutor
import org.fusesource.hawtbuf.{Buffer, UTF8Buffer}
import org.fusesource.mqtt.client.{MQTT, QoS, Topic}
import rx.android.schedulers.AndroidSchedulers
import rx.lang.scala.JavaConversions._
import rx.lang.scala.Observable

import scala.util.{Failure, Success}

class TimeActivity extends ActionBarActivity {
  val mqttServer = ("test.mosquitto.org", 1883)
  val connection = mqttConnection(mqttServer)
  implicit val execCtx = GuiThreadExecutor.context
  val topicName: String = "time"
  private val timeFormat = new SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy")

  private var subscription:Option[Observable[Buffer]] = None
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    val textView = createTextView(this)
    textView.setIconAndText(android.R.drawable.presence_away, "Connecting")
    setContentView(textView)
    connection.connect.onComplete {
      case Success(_) ⇒
        textView.setIconAndText(android.R.drawable.presence_online, "Connected!")
        connection.subscribe(new Topic(topicName, QoS.AT_LEAST_ONCE)).onSuccess{
          case updates ⇒
            subscription = Some(updates)
            textView.setIconAndText(android.R.drawable.presence_video_online, "Subscribed!")
            updates.map(readTimestamp).
                    observeOn(AndroidSchedulers.mainThread()).
                    map(dateToString).
                    subscribe(textView.setText(_))
        }
      case Failure(_) ⇒
        textView.setIconAndText(android.R.drawable.presence_offline, "Connection failure")
    }
  }

  override def onDestroy(): Unit = {
    subscription match {
      case Some(updates) ⇒ connection.unsubscribe(UTF8Buffer.utf8(topicName)).onComplete(_ ⇒ connection.disconnect)
      case None ⇒ connection.disconnect
    }
    super.onDestroy()
  }


  private def mqttConnection(server:(String,Int)) = {
    val mqtt = new MQTT()
    mqtt.setHost(mqttServer._1, mqttServer._2)
    ObservableMqttConnection(mqtt)
  }

  private def createTextView(context:Context) = {
    val textView = new TextView(this)
    textView.setGravity(Gravity.CENTER)

    val lp = textView.getLayoutParams
    lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
    textView.setLayoutParams(lp)

    textView
  }

  implicit class RichTextView(textView: TextView) {
    def setIconAndText(icon:Int, text:String) = {
      textView.setText(text)
      textView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
      textView
    }
  }

  private def readTimestamp(buffer:Buffer) = new Date(UTF8Buffer.decode(buffer).toLong)
  private def dateToString(date:Date) = timeFormat.format(date)
}
