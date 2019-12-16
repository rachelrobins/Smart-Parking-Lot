package parkinglot;

public class Scenarios {

	public static void createFirstSimulation() throws Exception
	{
		int id = ParkingSimulator.addCarEnterance();
		Thread.sleep(100);
		int id2 = ParkingSimulator.addCarEnterance();
		Thread.sleep(10000);
		ParkingSimulator.removeCarFromParkingLot(id);
	}
}
