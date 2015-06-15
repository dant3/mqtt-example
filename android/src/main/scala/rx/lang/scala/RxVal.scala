package rx.lang.scala

trait RxVal[T] extends Observable[T] {
  def get:T
  final def apply() = get
}