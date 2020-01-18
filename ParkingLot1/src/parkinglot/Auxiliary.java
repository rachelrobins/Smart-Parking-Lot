package parkinglot;

import java.util.LinkedList;

public class Auxiliary {
	
	static LinkedList<Car> carsToAdd = new LinkedList<Car>();
	static LinkedList<Car> carsToRemove = new LinkedList<Car>();
	static LinkedList<Car> carsOut = new LinkedList<Car>();
	static LinkedList<Pedestrian> pedsList = new LinkedList<Pedestrian>();

	// auxiliary flags to prevent overload
	static boolean flag = false;
	static boolean flagVip = false;
		

	/* 								 Auxiliary Methods	 						     */
	/* they are used to prevent overload and changing lists while iterating over them*/
	
	public static void executeAddPeds()
	{
		for(Pedestrian ped : pedsList)
		{
			int position = ped.position;
			if(position == 0)
			{
				ParkingSimulator.pedUpRight = true;
				ParkingSimulator.pedestrianUpRight = ped;
			}
			else if(position == 1)
			{
				ParkingSimulator.pedDownRight = true;
				ParkingSimulator.pedestrianDownRight = ped;
			}
			else if(position == 2)
			{
				ParkingSimulator.pedUpLeft = true;
				ParkingSimulator.pedestrianUpLeft = ped;
			}
			else
			{
				ParkingSimulator.pedDownLeft = true;
				ParkingSimulator.pedestrianDownLeft = ped;
			}
		}
		
	}
	
	public static void executeRemovePeds()
	{
		LinkedList<Pedestrian> pedsToDelete = new LinkedList<Pedestrian>();
		for(Pedestrian ped: pedsList)
		{
			if(ped.state == Pedestrian.States.CROSSING)
			{
				pedsToDelete.add(ped);
			}
		}
		for(Pedestrian ped : pedsToDelete)
		{
			pedsList.remove(ped);
		}
		pedsToDelete.clear();
	}
	
	public static void executeRemoveCars()
	{
		LinkedList<Car> tmp = new LinkedList<Car>();
		for(Car car : carsToRemove)
		{
			if((Car.carsInExitQ == 0 && !car.isVipCar()) || (Car.carsVipInExitQ == 0 && car.isVipCar()))
			{
				car.updateState(CarStates.PREPARE_TO_EXIT);
				tmp.add(car);
			}
		}
		for(Car car : tmp)
		{
			carsToRemove.remove(car);
		}
		tmp.clear();
	}
	
	public static void deleteOutCars()
	{
		for(Car car : carsOut)
		{
			ParkingSimulator.carList.remove(car);
		}
		carsOut.clear();
	}
	
	public static void executeAddCars() throws Exception
	{
		LinkedList<Car> lst = new LinkedList<Car>();
		flagVip = true;
		flag = true;
		for(Car car : carsToAdd)
		{
			ParkingSimulator.carList.addLast(car);
			car.updateState(CarStates.INIT);
			Thread.sleep(3000);
			lst.add(car);
		}
		
		flag = false;
		flagVip = false;
		
		for(Car car: lst)
		{
			carsToAdd.remove(car);
		}
		lst.clear();
	}
	
	// reset board before switching scenarios and after parkingLotMaintenance
	public static void resetBoard()
	{
		flagVip = false;
		flag = false;
		Car.carsInQ = 0;
		Car.carsVipInQ = 0;
		ParkingSimulator.pedDownRight = false;
		ParkingSimulator.pedDownLeft = false;
		ParkingSimulator.pedUpRight = false;
		ParkingSimulator.pedUpLeft = false;
		pedsList.clear();
		ParkingSimulator.carList.clear();
		carsToAdd.clear();
		carsToRemove.clear();
		
		if(!ParkingSimulator.parkingLotMaintainance)
		{
			ParkingSimulator.enableMain = false;
		}
	}
}
