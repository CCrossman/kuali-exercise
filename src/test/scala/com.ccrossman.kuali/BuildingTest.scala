package com.ccrossman.kuali

import org.scalatest.FlatSpec

/**
  * A Building class represents multiple shafts across several floors.
  */
class BuildingTest extends FlatSpec {

  "a building" should "have a name" in {
    val building = Building("Fancy Co")
    assert(building.name == "Fancy Co")

    intercept[IllegalArgumentException] {
      Building("")
    }
    intercept[IllegalArgumentException] {
      Building(null)
    }
  }

  it should "have shafts" in {
    val building = Building("Fancy Co")
    assert(building.elevators.isInstanceOf[Seq[Elevator]])
  }

  it should "have a number of floors" in {
    val building = Building("Fancy Co", 5)
    assert(building.numFloors == 5)
    assert(Building("Empty").numFloors == 1)
  }

  it should "throw an exception if there are no shafts when a request is made" in {
    val building = Building("Empty Co")
    intercept[IllegalStateException] {
      building.requestElevatorFromFloor(1)
    }
  }

  it should "let you add shafts" in {
    val building = Building("Twin Shafts")
    val shaft = building.addElevator()
    assert(shaft.isInstanceOf[Elevator])

    val anotherShaft = building.addElevator()
    assert(shaft != anotherShaft)
  }

  it should "let you request an elevator from a floor" in {
    val building = Building("Fancy Co",10)
    val elevator = building.addElevator()
    assert(elevator.floor == 1)

    // since there's only one elevator, that elevator will move
    building.requestElevatorFromFloor(10)

    assert(elevator.floor == 10)

    // door should open to greet requestor
    assert(elevator.door.isOpen)
  }

  it should "move the closest elevator" in {
    val building = Building("Twin Shafts", 5)
    val a = building.addElevator()
    a.moveElevator(4)

    val b = building.addElevator()

    assert(a.floor == 4)
    assert(b.floor == 1)

    building.requestElevatorFromFloor(5)

    assert(a.floor == 5)
    assert(b.floor == 1)
  }

  "an elevator" should "be 'the closest' if already on that floor" in {
    val building = Building("Twin Shafts", 5)
    val a = building.addElevator()
    val b = building.addElevator()
    b.moveElevator(4)

    assert(a.floor == 1)
    assert(a.numTrips == 0)
    assert(a.door.isClosed)

    assert(b.floor == 4)
    assert(b.numTrips == 1) // b.moveElevator counts
    assert(b.door.isClosed)

    // act
    building.requestElevatorFromFloor(4)

    assert(a.floor == 1)
    assert(a.numTrips == 0)
    assert(a.door.isClosed)

    assert(b.floor == 4)
    assert(b.numTrips == 1)
    assert(b.door.isOpen)   // greet the requestor

  }
}
