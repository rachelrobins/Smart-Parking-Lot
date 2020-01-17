package parkinglot;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Car {
	// fields of car
	private int id;
	private BufferedImage img;
	private CarStates state;
	private int X;
	private int Y;
	private int width;
	private int height;
	private boolean isRemoved;
	private static int counter = 0;
	static int carsVipInQ = 0;

	static int carsInQ = 0;
	static int carsInExitQ = 0;
	static int carsVipInExitQ = 0;

	private int parkingSpot = -1;
	private boolean vipCar;
	
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
		{
			
		}
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

	public void updateState(CarStates state) {
//		if(this.state == CarStates.IN_QUEUE_SECOND) 
//		{
//			if(!this.isVipCar())
//				carsInQ--;
//			else
//				carsVipInQ--;
//		}
		
		this.state = state;
		if(state==CarStates.INIT)
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
		
		if(state==CarStates.ENTER_PARKING_LOT)
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
		
		if(state==CarStates.PREPARE_TO_EXIT)
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
		
		if(state==CarStates.EXITING)
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
	
	public void updateState(CarStates state, int parkingSpot) {
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

