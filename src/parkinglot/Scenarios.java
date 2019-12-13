package parkinglot;

public class Scenarios {

	public static void createFirstSimulation() throws Exception
	{
		int id = ParkingSimulator.addCarEnterance();
		Thread.sleep(1000);
//		ParkingSimulator.removeCarFromParkingLot(id);
	}
}
