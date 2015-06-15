package rx.lang.scala

import java.util.concurrent.atomic.AtomicReference

import com.github.dant3.mqttexample.utils.focused.FocusedMutableSet

final class RxVar[T] private(private val currentValue:AtomicReference[T]) extends RxVal[T] {
  import ImplicitFunctionConversions._

  private def this(startValue:T) = this(new AtomicReference[T](startValue))

  override def get = currentValue.get()

  def update(newValue:T):Unit = {
    val oldValue = currentValue.getAndSet(newValue)
    notifySubscribers(oldValue, newValue)
  }
  def := (newValue:T):Unit = update(newValue)


  private val subscribers = new FocusedMutableSet[Subscriber[T]]
  override private[scala] val asJavaObservable = rx.Observable.create { subscriber: Subscriber[T] ⇒
    subscribers.modify { set ⇒
      set.retain(!_.isUnsubscribed)
      if (set.add(subscriber)) { subscriber.onStart() }
    }
  }
  private def notifySubscribers(oldValue:T, newValue:T) = {
    subscribers.read.view.filter(!_.isUnsubscribed).foreach(_.onNext(newValue))
    subscribers.modify(_.retain(!_.isUnsubscribed))
  }
}

object RxVar {
  def apply[T](initialValue:T):RxVar[T] = new RxVar[T](initialValue)
}
