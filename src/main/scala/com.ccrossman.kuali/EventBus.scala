package com.ccrossman.kuali

/**
  * An event bus for receiving and reporting events
  */
object EventBus {
  def publish(event: Event[_]) = {
    println("received event %s".format(event))
  }
}

trait Event[T] {
  def source: T
}
