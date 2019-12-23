package parkinglot;

import java.util.LinkedList;
import java.util.Random;

public class Scenarios {

	// First Scenario
	// Car enters the parking lot and then leaves
	public static void createInceptionScenario() throws Exception
	{
		Car car = ParkingSimulator.addCarEnterance();
		int id = car.getId();
		Thread.sleep(6000);
		while(car.getState()!= CarStates.PARKED)
		{
			System.out.println("in while");
			continue;
		}
		ParkingSimulator.removeCarFromParkingLot(id);
		Thread.sleep(50000);
	}
	
	
	// Second Scenario 
	// Rush Hour - a lot of cars enters the parking lot
	public static void createRushHourScenario() throws Exception
	{
		int id = ParkingSimulator.addCarEnterance().getId();
		Thread.sleep(100);
		int id2 = ParkingSimulator.addCarEnterance().getId();
		for(int i = 0; i < 7; i++)
		{
			Thread.sleep(10000);
			ParkingSimulator.addCarEnterance();
			
		}
		Thread.sleep(10000);
		ParkingSimulator.removeCarFromParkingLot(id);
		Thread.sleep(50000);
		ParkingSimulator.removeCarFromParkingLot(id2);
		Thread.sleep(10000);
		ParkingSimulator.addCarEnterance();
			
		Thread.sleep(10000);
	}
	
	// Random cars entering and leaving the parking lot
	public static void createRandomScenario() throws Exception
	{
		LinkedList<Car> carsInParkingLot = new LinkedList<Car>();
		LinkedList<Car> parkedCars= new LinkedList<Car>();
		
		int counter = 0;
		boolean carEntering;
		boolean carExiting;
		int exitRandomCar;
		Random rand = new Random();
		while(true)
		{
			
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
				Car car = ParkingSimulator.addCarEnterance();
				carsInParkingLot.add(car);
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
	
	// Forth Scenario
	// Pedestrian
	public static void createForthScenario() throws Exception
	{
		ParkingSimulator.addPedestrian(0);
		Scenarios.createInceptionScenario();
		
	}
}
