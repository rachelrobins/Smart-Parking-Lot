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
	private final int width = 95;
	private final int height = 51;

	public Car(int id)
	{
		try {
			this.setId(id);
			this.setImg(ImageIO.read(new File("img/car.png"))); 
			this.updateState(CarStates.INIT);
			this.setX(690);
			this.setY(159);
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
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}

