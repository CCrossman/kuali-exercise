package com.ccrossman.kuali

/**
  * An elevator moves between floors
  */
case class Elevator(id: Int, numFloors: Int = 1, initialMode: ElevatorMode = ActiveMode) {
  private var _elevatorPos: Int = 1

  // how many times the elevator has moved
  private var _numTrips = 0

  // tracks if the elevator is working or not
  private var _mode: ElevatorMode = initialMode

  // tracks if the elevator is moving
  private var _moving = false

  /**
    * @return - the number of trips made by this elevator
    */
  def numTrips = _numTrips

  /**
    * @return - what floor the elevator is currently on
    */
  def floor = _elevatorPos

  /**
    * @return - the elevator's current mode
    */
  def mode = _mode

  /**
    * @return - true if the elevator is moving, false otherwise
    */
  def moving = _moving

  /**
    * @return - the bottom floor's id
    */
  val minFloor = 1

  /**
    * @return - the top floor's id
    */
  val maxFloor = numFloors

  /**
    * create a Door object for this elevator.
    * 'val' means it's Java-final
    */
  val door = Door(this)

  def moveElevator(floorId: Int): Unit = {
    if (floorId > maxFloor || floorId < minFloor) {
      throw NoSuchFloorException(this, floorId)
    }
    // don't need to work if already at the floor
    if (floorId != floor) {

      // close the elevator door before moving
      door.close()

      // move the elevator, floor by floor
      toRange(_elevatorPos, floorId).foreach(floor => {
        if (floor != _elevatorPos) {
          // if/when the floor actually changes, mark as moving
          _moving = true

          // update floor marker one at a time
          val _oldPos = _elevatorPos
          _elevatorPos = floor
          EventBus.publish(ElevatorMoved(this, _oldPos, floor))
        }
      })
      _moving = false

      // update trip counter
      _numTrips += 1
      if (_numTrips >= 100) {
        _mode = MaintenanceMode
      }
    }
  }

  // make a range that counts up or counts down as appropriate
  private def toRange(startFloor: Int, endFloor: Int): Range = {
    if (startFloor < endFloor) {
      Range.inclusive(startFloor, endFloor)
    } else {
      toRange(endFloor, startFloor).reverse
    }
  }
}

case class ElevatorMoved(source: Elevator, oldFloor: Int, newFloor: Int) extends Event[Elevator]

case class NoSuchFloorException(shaft: Elevator, floor: Int) extends IllegalStateException

sealed trait ElevatorMode

case object ActiveMode extends ElevatorMode

case object MaintenanceMode extends ElevatorMode
