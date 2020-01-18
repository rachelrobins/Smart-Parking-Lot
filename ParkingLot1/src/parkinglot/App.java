package parkinglot;

import javax.swing.WindowConstants;

public class App {
	// initialize Parking Lot 
		public static void main(String args[]) throws Exception 
		{
			ParkingSpotsConfig.initMap();
			GUI.f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			GUI.f.setSize(1113, 673);
			ParkingSimulator parkingLot = new ParkingSimulator();
			GUI.f.setContentPane(parkingLot);
			GUI.createGUI();
			
			while(true)
			{
				if(!ParkingSimulator.randomDone)
				{
					ParkingSimulator.enableMain = false;
					Scenarios.createRandomScenario();
				}
			}
		}
}
