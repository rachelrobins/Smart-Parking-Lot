package parkinglot;

public class Scenarios {

	public static void createFirstSimulation() throws Exception
	{
		int id = ParkingSimulator.addCarEnterance();
		Thread.sleep(100);
		int id2 = ParkingSimulator.addCarEnterance();
		for(int i = 0; i < 6; i++)
		{
			Thread.sleep(10000);
			id = ParkingSimulator.addCarEnterance();
			
		}
		Thread.sleep(10000);
		ParkingSimulator.removeCarFromParkingLot(id);
		Thread.sleep(10000);
		ParkingSimulator.removeCarFromParkingLot(id2);
	
		Thread.sleep(10000);
		//ParkingSimulator.removeCarFromParkingLot(id);
	}
}
