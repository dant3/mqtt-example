import java.util.concurrent.Executors

import org.fusesource.mqtt.client.{BlockingConnection, QoS, MQTT}
import rx.lang.scala.Observable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.{implicitConversions, postfixOps}

object Main {
  val mqttServer = ("test.mosquitto.org", 1883)
  val emitterService = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
  val timeSource = Observable.interval(1 seconds).map(_ ⇒ System.currentTimeMillis())

  def main(args:Array[String]) = {
    val mqtt = new MQTT()
    mqtt.setHost(mqttServer._1, mqttServer._2)
    val mqttConnection = mqtt.blockingConnection()
    mqttConnection.connect()
    Runtime.getRuntime.addShutdownHook(new Thread({ mqttConnection.disconnect() }))
    val publishTimestamp = publishTime(mqttConnection) _
    publishTimestamp(System.currentTimeMillis())
    timeSource.subscribe {
      time ⇒ publishTimestamp(time)
    }
    while (true) {
      Thread.sleep(100)
    }
  }

  private def publishTime(connection:BlockingConnection)(time:Long) = {
    System.out.println(s"Publishing timestamp: $time")
    connection.publish("time", time.toString.getBytes, QoS.AT_LEAST_ONCE, false)
  }

  implicit def toRunnable(block: ⇒ Any):Runnable = new Runnable {
    override def run(): Unit = block
  }
}
