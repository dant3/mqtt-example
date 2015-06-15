package com.github.dant3.mqttexample.core

import com.github.dant3.mqttexample.utils.android.concurrent.AsyncExecutor
import com.github.dant3.mqttexample.utils.focused.FocusedMutableMap
import org.fusesource.hawtbuf.{Buffer, UTF8Buffer}
import org.fusesource.mqtt.client.{CallbackConnection, Listener, MQTT, Topic}
import rx.lang.scala.{Observable, RxVar, Subscriber}

import scala.concurrent.Future

class ObservableMqttConnection private (connection:CallbackConnection) {
  private implicit val execCtx = AsyncExecutor.context

  private val connected = RxVar(false)
  private val subscriptions = new FocusedMutableMap[UTF8Buffer, Subscriber[Buffer]]


  def connect:Future[Unit] = {
    val promise = new MqttPromise[Void]
    connection.connect(promise)
    promise.future.map(_ ⇒ ())
  }

  def disconnect:Future[Unit] = {
    val promise = new MqttPromise[Void]
    connection.disconnect(promise)
    promise.future.map(_ ⇒ ())
  }

  def subscribe(topics:Topic):Future[Observable[Buffer]] = subscribe(Array(topics))

  def subscribe(topics:Array[Topic]):Future[Observable[Buffer]] = {
    val promise = new MqttPromise[Array[Byte]]
    connection.subscribe(topics, promise)
    promise.future.map(_ ⇒ {
      val subscription = new ProxySubscriber[Buffer]
      subscriptions.modify { map ⇒
        for {
          topic ← topics
          topicName = topic.name()
        } map.put(topicName, subscription)
      }
      Observable.apply { subscriber:Subscriber[Buffer] ⇒
        subscription.childs.modify(_.add(subscriber))
      }
    })
  }

  def unsubscribe(topic:UTF8Buffer):Future[Unit] = unsubscribe(Array(topic))
  def unsubscribe(topics:Array[UTF8Buffer]):Future[Unit] = {
    val promise = new MqttPromise[Void]
    connection.unsubscribe(topics, promise)
    promise.future.map(_ ⇒ subscriptions.modify(map ⇒ for (topic ← topics) map.remove(topic)))
  }

  connection.listener(new Listener() {
    override def onPublish(topic: UTF8Buffer, body: Buffer, ack: Runnable): Unit = {
      for (subscription ← subscriptions.read.get(topic); if !subscription.isUnsubscribed) {
        subscription.onNext(body)
      }
      ack.run()
    }
    override def onConnected(): Unit = connected := true
    override def onFailure(value: Throwable): Unit = ???
    override def onDisconnected(): Unit = connected := false
  })
}

object ObservableMqttConnection {
  def apply(mqtt:MQTT) = new ObservableMqttConnection(mqtt.callbackConnection())
}