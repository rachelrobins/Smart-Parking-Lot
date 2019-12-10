package parkinglot;

public class Scenarios {

	public void createFirstSimulation() throws Exception
	{
		ParkingSimulator.addCarEnterance(1);
		Thread.sleep(1000);
		ParkingSimulator.removeCarFromParkingLot(1);
	}
}
