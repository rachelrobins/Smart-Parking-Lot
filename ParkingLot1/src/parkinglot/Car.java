package parkinglot;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/* This class represents cars an an object in the parking lot */ 

public class Car {
	// fields of car
	private int id; // id of car
	private BufferedImage img; // red for VIP and van for regular
	private CarStates state; // as defined in carStates class
	private int X; // x-axis position
	private int Y; // y-axis position
	private int width;
	private int height;
	private boolean isRemoved; // whether the car was removed from parking lot
	private static int counter = 0; // counts how many cars were created- to determine id
	private int parkingSpot = -1; // in which spot the car is parking, default is -1 which means it doesn't park
	private boolean vipCar; // whether the car is VIP
	
	// static variables of car class
	static int carsVipInQ = 0; // how many cars are waiting in vip queue(max 1)
	static int carsInQ = 0; // how many cars are waiting in regular queue(max 2)
	static int carsInExitQ = 0; // how many cars are waiting in regular exit queue(max 1)
	static int carsVipInExitQ = 0; // how many cars are waiting in Vip exit queue(max 1)

	// car constructor
	public Car(boolean vipCar)
	{
		try {
			this.setRemoved(false);
			this.setId(counter);
			this.state  = CarStates.INIT;
			
			if(vipCar)
			{
				this.setImg(ImageIO.read(new File("img/carVip.png"))); 
				this.setX(0);
				this.setY(259);
			}
			else
			{
				this.setImg(ImageIO.read(new File("img/car.png"))); 
				this.setX(970);
				this.setY(159);
			}
			
			this.width = 95;
			this.height = 51;
			counter++;
			this.setVipCar(vipCar);
		}
		catch (Exception e)
		{}
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public CarStates getState() {
		return state;
	}
	
	// here we make sure the car moves to next step, changing variables to adjust to state
	public void updateState(CarStates state) {
		this.state = state;
		
		if(state == CarStates.INIT)
		{
			if(!this.isVipCar())
			{
				carsInQ++;
			}
			else
			{
				carsVipInQ++;
			}
		}
		
		if(state == CarStates.ENTER_PARKING_LOT)
		{
			if(!this.isVipCar())
			{
				carsInQ--;
			}
			else
			{
				carsVipInQ--;
			}
		}
		
		if(state == CarStates.PREPARE_TO_EXIT)
		{
			if(!this.isVipCar())
			{
				carsInExitQ++;
			}
			else
			{
				carsVipInExitQ++;
			}
		}
		
		if(state == CarStates.EXITING)
		{
			if(!this.isVipCar())
			{
				carsInExitQ--;
			}
			else
			{
				carsVipInExitQ--;
			}
		}
	}
	
	// updateState overload - if we get int as well it means we need to update parking spot field
	public void updateState(CarStates state, int parkingSpot) 
	{
		this.updateState(state);
		this.setParkingSpot(parkingSpot);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(int parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	public boolean isVipCar() {
		return vipCar;
	}

	public void setVipCar(boolean vipCar) {
		this.vipCar = vipCar;
	}

	public boolean isRemoved() {
		return isRemoved;
	}

	public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
	
	
}

