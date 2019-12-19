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
	private static int counter = 0;
	static int carsInQ = 0;
	static int carsInExitQ = 0;
	private int parkingSpot = -1;

	public Car()
	{
		try {
			this.setId(counter);
			this.setImg(ImageIO.read(new File("img/car.png"))); 
			this.updateState(CarStates.INIT);
			this.setX(690);
			this.setY(159);
			this.width = 95;
			this.height = 51;
			counter++;
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
		if(this.state == CarStates.IN_QUEUE_SECOND) {
			carsInQ--;
		}
		this.state = state;
		if(state==CarStates.IN_QUEUE_FIRST || state==CarStates.IN_QUEUE_SECOND )
			carsInQ++;
		if(state==CarStates.ENTER_PARKING_LOT)
			carsInQ--;
		if(state==CarStates.PREPARE_TO_EXIT)
			carsInExitQ++;
		if(state==CarStates.EXITING)
			carsInExitQ--;
			
	}
	
	public void updateState(CarStates state, int parkingSpot) {
		this.state = state;
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
	
	
}

