package parkinglot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

public class Pedestrian {
	int x;
	int y;
	BufferedImage img;
	Pedestrian.States state;
	int position;
	private static String[] pedImages = new String[] {"img/p1.png", "img/p2.png","img/p3.png"};
	
	public Pedestrian(int position) throws Exception
	{
		Random rnd = new Random();
		int imgForPed = rnd.nextInt(3);
		this.img = ImageIO.read(new File(pedImages[imgForPed]));
		this.state = Pedestrian.States.ENETERING;
		this.position = position;
		if(position == 0)
		{
			this.x = 790;
			this.y = 30;
		}
		else if(position == 1)
		{
			this.x = 790;
			this.y = 400;
		}
		else if(position == 2)
		{
			this.x = 270;
			this.y = 30;
		}
		else
		{
			this.x = 270;
			this.y = 400;
		}
		
	}
	enum States {
		ENETERING,
		WAITING,
		CROSSING,
	}
}


