package parkinglot;

public class API {

	// API
	public static Car addVipCarEnterance() throws Exception
	{
		if(Auxiliary.flagVip)
		{
			return null;
		}
		Car car = new Car(true);

		if(Car.carsVipInQ == 1)
		{
			System.out.println("Too many VIP cars");
			return null;
		}
		else
		{
			Auxiliary.carsToAdd.add(car);
			Auxiliary.flagVip = true;
		}
		return car;
	}
	
	public static Car addCarEnterance() throws Exception
	{
		if(Auxiliary.flag)
		{
			return null;
		}
		Car car = new Car(false);
		if(Car.carsInQ == 2)
		{
			System.out.println("Too many cars");
			return null;
		}
		else 
		{
			Auxiliary.carsToAdd.addLast(car);
			Auxiliary.flag = true;
		}
		
		return car;

	}
	
	public static void removeCarFromParkingLot(int carID)
	{
		
		for(Car car : ParkingSimulator.carList)
		{
			if(car.getId() == carID)
			{
				car.setRemoved(true);
				Auxiliary.carsToRemove.add(car);
				return;	
			}
		}
	}
	
	public static Pedestrian addPedestrian(int position) throws Exception
	{
		boolean alreadyExist = false;
		Pedestrian ped = new Pedestrian(position);
		for(Pedestrian pedes: Auxiliary.pedsList)
		{
			if(pedes.position == position)
			{
				alreadyExist = true;
			}
		}
		if(!alreadyExist)
			Auxiliary.pedsList.add(ped);
		return ped;
	}
	
}
