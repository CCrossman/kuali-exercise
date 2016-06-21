package com.ccrossman.kuali

import org.scalatest.FlatSpec

/**
  * Exercise the elevator class
  */
class ElevatorTest extends FlatSpec {

  "an elevator" should "have an id" in {
    val elevator = Elevator(1)
    assert(elevator.id == 1)
  }

  it should "know what floors it can reach" in {
    val elevator = Elevator(1,5)
    assert(elevator.numFloors == 5)
    assert(elevator.minFloor == 1)
    assert(elevator.maxFloor == 5)
  }

  it should "know what floor its elevator is on" in {
    val elevator = Elevator(1,3)
    assert(elevator.floor == 1)
    elevator.moveElevator(2)
    assert(elevator.floor == 2)
    elevator.moveElevator(3)
    assert(elevator.floor == 3)
  }

  it should "have a door" in {
    val elevator = Elevator(1)
    assert(elevator.door != null)
  }

  it should "not proceed above the top floor" in {
    val elevator = Elevator(1,3)
    assert(elevator.floor <= 3)
    intercept[NoSuchFloorException] {
      elevator.moveElevator(4)
    }
  }

  it should "not proceed below the ground floor" in {
    val elevator = Elevator(1)
    assert(elevator.floor == 1)
    intercept[NoSuchFloorException] {
      elevator.moveElevator(0)
    }
  }

  it should "know how many trips it has made since last serviced" in {
    val elevator = Elevator(1,5)
    assert(elevator.numTrips == 0)

    // move the elevator
    elevator.moveElevator(3)
    assert(elevator.numTrips == 1)
  }

  it should "have a mode" in {
    assert(Elevator(1).mode == ActiveMode)
    assert(Elevator(id = 1, initialMode = MaintenanceMode).mode == MaintenanceMode)
  }

  it should "update to MaintainenceMode after 100 trips" in {
    val elevator = Elevator(1,2)

    // do 99 trips
    Range(1,100).foreach(_ => {
      elevator.moveElevator(1 + (1 - (elevator.floor - 1)))
    })

    // still working...
    assert(elevator.mode == ActiveMode)

    // one more...
    elevator.moveElevator(1 + (1 - (elevator.floor - 1)))

    // ... not working now
    assert(elevator.mode == MaintenanceMode)
  }

  "an elevator door" should "open" in {
    val elevator = Elevator(1)
    assert(elevator.door.isClosed)
    elevator.door.open()
    assert(elevator.door.isOpen)
  }

  it should "close" in {
    val elevator = Elevator(1)
    elevator.door.open()
    assert(elevator.door.isOpen)
    elevator.door.close()
    assert(elevator.door.isClosed)
  }
}
