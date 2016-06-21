package com.ccrossman.kuali

import scala.collection.mutable.ListBuffer

case class Building(name: String, numFloors: Int = 1) {
  require(name != null && name != "")

  // private Java-final reference to a mutable list (like ArrayList)
  private val _elevators = ListBuffer[Elevator]()

  // expose list as Seq[Elevator]
  def elevators: Seq[Elevator] = _elevators

  // make a new elevator
  def addElevator(): Elevator = {
    val elevator = Elevator(1 + elevators.size, numFloors)
    _elevators += elevator
    elevator
  }

  private def distance(aFloor: Int, bFloor: Int): Int = {
    Math.abs(aFloor - bFloor)
  }

  def requestElevatorFromFloor(floorId: Int): Unit = {
    if (elevators.isEmpty) throw new IllegalStateException

    // find closest elevator
    val closest: Elevator = elevators.reduce((a,b) => {
      if (distance(floorId,a.floor) < distance(floorId,b.floor)) {
        a
      } else {
        b
      }
    })
    // move that elevator
    closest.moveElevator(floorId)

    // greet the requestor
    closest.door.open()
  }
}
