package parkinglot;

import java.util.LinkedList;
import java.util.Random;

enum ScenarioStep {
	ADD_CAR,
	ADD_VIP_CAR,
	ADD_PED,
	REMOVE_CAR,
	REMOVE_VIP_CAR,
	ENABLE_MAINT;
}

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
	// Zero Scenario
	// One car enters and exits the parking lot
	public static void createZeroScenario() throws Exception
	{
		ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_CAR);
		ParkingSimulator.scenarioSteps.add(ScenarioStep.REMOVE_CAR);
	}
	
	
	// First Scenario
	// It’s rush hour and the parking lot is full with regular cars.
	public static void createFirstScenario() throws Exception
	{
		for(int i = 0; i < 9; i++)
		{
			ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_CAR);			
		}
	}
	
	// Second Scenario 
	// Regular vehicle and VIP vehicle want to enter the parking lot at the same time.
	// (Note: it also depends on the pedestrian lights)

	public static void createSecondScenario() throws Exception
	{
		for(int i = 0; i < 4; i++)
		{
			ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_VIP_CAR);			
			ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_CAR);
		}
	}
	
	// Third Scenario 
	// Pedestrian cross both cross-walks while cars want to enter the parking lot.
	public static void createThirdScenario() throws Exception
	{
		ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_PED);			
		ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_CAR);
		ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_PED);
		ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_PED);			
		ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_PED);			
		ParkingSimulator.scenarioSteps.add(ScenarioStep.REMOVE_CAR);
		ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_PED);			
	}
	

	
	// Forth Scenario
	// Vehicles enters the parking lot and suddenly a fire bursts.
	public static void createForthScenario() throws Exception
	{
		ParkingSimulator.scenarioSteps.add(ScenarioStep.ENABLE_MAINT);			
		for(int i = 0; i < 4; i++)
		{
			ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_VIP_CAR);			
			ParkingSimulator.scenarioSteps.add(ScenarioStep.ADD_CAR);
		}
	}
	
	
//  Random cars entering and leaving the parking lot
//	Random pedestrians cross the cross-walks
//	Maintenance is disabled.
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
				ParkingSimulator.randomDone = true;
				return;
			}
			// Randomize Pedestrians
			for(int i = 0; i < 4; i++)
			{
				if(!pedsWaiting[i]) 
				{
					// Chances of adding pedestrian is 1 to 10
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
					Car carToRemove = parkedCars.get(exitRandomCar);
					if(!carToRemove.isRemoved())
					{
						ParkingSimulator.removeCarFromParkingLot(carToRemove.getId());
						counter--;
						carsInParkingLot.remove(exitRandomCar);
					}
					
				}
				parkedCars.clear();
			}
			if(ParkingSimulator.scenarioSwitch)
			{
				System.out.println("finished random");
				return;
			}
			Thread.sleep(3000);
		}
	}
}
