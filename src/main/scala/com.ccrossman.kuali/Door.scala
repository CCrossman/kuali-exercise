package com.ccrossman.kuali

/**
  * A Door is part of an Elevator
  */
case class Door(owner: Elevator) {
  private var _open: Boolean = false

  /**
    * isOpen (getter method)
    *
    * @return - the value stored in _open
    */
  final def isOpen = _open

  /**
    * isClosed (getter method)
    *
    * @return - whether or not the door is closed
    */
  final def isClosed = !isOpen

  /**
    * close (method)
    *   closes the door (if it is open)
    *   reports state change
    */
  final def close(): Unit = {
    if (isOpen) {
      _open = false
      EventBus.publish(DoorClosedEvent(this))
    }
  }

  /**
    * open (method)
    *   opens the door (if it is closed)
    *   reports state change
    */
  final def open(): Unit = {
    if (isClosed) {
      _open = true
      EventBus.publish(DoorOpenEvent(this))
    }
  }
}

sealed trait DoorEvent extends Event[Door]
case class DoorClosedEvent(source: Door) extends DoorEvent
case class DoorOpenEvent(source: Door) extends DoorEvent