package parkinglot;

import java.util.LinkedList;
import java.util.Random;

public class Scenarios {
	public static LinkedList<Car> safeAddCar(LinkedList<Car> carList) throws Exception{
		Car car = ParkingSimulator.addCarEnterance();
		if (car!=null) 
			carList.add(car);
		return carList;
		
	}
	public static LinkedList<Car> safeAddVipCar(LinkedList<Car> carList) throws Exception{
		Car car = ParkingSimulator.addVipCarEnterance();
		if (car!=null) 
			carList.add(car);
		return carList;
		
	}
	// First Scenario
	// Car enters the parking lot and then leaves
	public static void createZeroScenario() throws Exception
	{
		ParkingSimulator.disableButtons();
		Car car = ParkingSimulator.addCarEnterance();
		int id = car.getId();
		System.out.println("in zero");
		Thread.sleep(8000);
		System.out.println("car state " + car.getState());

		while(car.getState()  != CarStates.PARKED) {
			Thread.sleep(10);
		}
		
		ParkingSimulator.removeCarFromParkingLot(id);
		Thread.sleep(50000);
	}
	
	public static void createFirstScenario() throws Exception
	{
		ParkingSimulator.disableButtons();
		Car car = ParkingSimulator.addCarEnterance();
		Thread.sleep(100);
		car = ParkingSimulator.addCarEnterance();
		Thread.sleep(10000);
		ParkingSimulator.enableButtons();
	}
	
	// Second Scenario 
	// Rush Hour - a lot of regular cars enters the parking lot
	public static void createSecondScenario() throws Exception
	{
		ParkingSimulator.disableButtons();
		int id = ParkingSimulator.addVipCarEnterance().getId();
		Thread.sleep(7000);
		int id2 = ParkingSimulator.addVipCarEnterance().getId();
		for(int i = 0; i < 7; i++)
		{
			Thread.sleep(10000);
			ParkingSimulator.addVipCarEnterance();
			
		}
		Thread.sleep(10000);
		ParkingSimulator.addVipCarEnterance();
		Thread.sleep(10000);
		ParkingSimulator.removeCarFromParkingLot(id);
		Thread.sleep(50000);
		ParkingSimulator.removeCarFromParkingLot(id2);
		Thread.sleep(10000);
		ParkingSimulator.addVipCarEnterance();
		Thread.sleep(10000);
		ParkingSimulator.addVipCarEnterance();
		Thread.sleep(10000);
		ParkingSimulator.addVipCarEnterance();
			
		Thread.sleep(10000);
		
		ParkingSimulator.enableButtons();
	}
	
	// Third Scenario 
	// Rush Hour - a lot of cars, both vip and regular, enters the parking lot
	public static void createThirdScenario() throws Exception
	{
		ParkingSimulator.disableButtons();

		ParkingSimulator.addVipCarEnterance();
		ParkingSimulator.addCarEnterance();
		Thread.sleep(7000);
		ParkingSimulator.addCarEnterance();
		Thread.sleep(7000);
		ParkingSimulator.addCarEnterance();
		Thread.sleep(7000);
		ParkingSimulator.addCarEnterance();
		for(int i = 0; i < 7; i++)
		{
			Thread.sleep(15000);
			ParkingSimulator.addVipCarEnterance();
			ParkingSimulator.addCarEnterance();

		}
		ParkingSimulator.enableButtons();
	}
	

	
	// Forth Scenario
	// Pedestrian
	public static void createForthScenario() throws Exception
	{
		ParkingSimulator.disableButtons();
		ParkingSimulator.addPedestrian(0);
		ParkingSimulator.addPedestrian(1);
		ParkingSimulator.addPedestrian(2);
		ParkingSimulator.addPedestrian(3);
		Scenarios.createZeroScenario();
		for(int i = 0 ; i < 10; i++)
		{
			ParkingSimulator.addPedestrian(3);
		}
		ParkingSimulator.enableButtons();
	}
	
	// Fifth Scenario
	// War between vip and regular vehicles
	public static void createFifthScenario() throws Exception
	{
		ParkingSimulator.disableButtons();
		for(int i = 0; i < 7; i++)
		{
			ParkingSimulator.addVipCarEnterance();
			ParkingSimulator.addCarEnterance();
			Thread.sleep(15000);
		}
		ParkingSimulator.enableButtons();
	}
	
	public static void createTestScenario() throws Exception
	{
		Car cars [] = new Car[8] ;
		for(int i = 0; i < 8; i++)
		{
			cars[i] = ParkingSimulator.addVipCarEnterance();
			Thread.sleep(5000);

		}
		for(int i = 0; i < 8; i++)
		{
			ParkingSimulator.removeCarFromParkingLot(cars[i].getId());
			Thread.sleep(5000);

		}
		ParkingSimulator.enableButtons();
		
	}
	
// Random cars entering and leaving the parking lot
	public static void createRandomScenario() throws Exception
	{
		LinkedList<Car> carsInParkingLot = new LinkedList<Car>();
		LinkedList<Car> parkedCars= new LinkedList<Car>();
		LinkedList<Pedestrian> peds = new LinkedList<Pedestrian>();
		boolean pedsWaiting[] = new boolean [] {false, false,false, false};
		
		int counter = 0;
		boolean carEntering;
		boolean carExiting;
		int exitRandomCar;
		boolean randVip;
		int pedEntering[] = new int [4];
		Random rand = new Random();
		while(true)
		{
			if(ParkingSimulator.scenarioSwitch)
			{
				System.out.println("finished random");
				return;
			}
			// Randomize Pedestrians
			for(int i = 0; i < 4; i++)
			{
				if(!pedsWaiting[i]) 
				{
					pedEntering[i] = rand.nextInt(10);
				}
			}
			for(int i = 0; i < 4; i++)
			{
				if(pedEntering[i] == 0)
				{
					Pedestrian ped = ParkingSimulator.addPedestrian(i);
					peds.add(ped);
					pedEntering[i] = 1;
				}
			}
			for(Pedestrian ped : peds)
			{
				if(ped.state == Pedestrian.States.ENETERING || ped.state == Pedestrian.States.WAITING)
				{
					pedsWaiting[ped.position] = true;
				}
				else
				{
					pedsWaiting[ped.position] = false;
					pedEntering[ped.position] = 2;
				}
			}
			for(int i = 0; i < 4; i++)
			{
				Pedestrian pedToDelete = null;
				boolean del = false;
				if(pedEntering[i] == 2)
				{
					for(Pedestrian ped : peds)
					{
						if(ped.position == i)
						{
							pedToDelete = ped;
							del = true;
						}
					}
				}
				if(del)
				{
					peds.remove(pedToDelete);
				}
				
			}
			
			// Randomize Vehicles
			if(counter == 0)
			{
				carEntering = rand.nextBoolean();
				carExiting = false;
			
			}
			else if(counter == 10)
			{
				carEntering = false;
				carExiting = rand.nextBoolean();
				
			}
			else if (Car.carsInQ == 2)
			{
				carEntering = false;
				carExiting = rand.nextBoolean();
			
			}
			else
			{
				carEntering = rand.nextBoolean();
				carExiting = rand.nextBoolean();
			}
			
			if(carEntering)
			{
				Car car;
				randVip = rand.nextBoolean();
				if(randVip)
				{
					carsInParkingLot = safeAddVipCar(carsInParkingLot);
				}
				else 
				{
					carsInParkingLot = safeAddCar(carsInParkingLot);
				}
				
				counter++;
			}
			if(carExiting)
			{
				
				for(Car car : carsInParkingLot)
				{
					if(car.getState() == CarStates.PARKED)
					{
						parkedCars.add(car);
					}
				}
				if(parkedCars.size() != 0)
				{
					exitRandomCar = rand.nextInt(parkedCars.size());
					ParkingSimulator.removeCarFromParkingLot(parkedCars.get(exitRandomCar).getId());
					counter--;
					carsInParkingLot.remove(exitRandomCar);
				}
				parkedCars.clear();
			}

			Thread.sleep(7000);
		}
	}
}
