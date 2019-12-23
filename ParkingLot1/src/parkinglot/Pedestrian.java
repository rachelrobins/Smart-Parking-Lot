package parkinglot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

public class Pedestrian {
	int x;
	int y;
	BufferedImage img;
	PedestrianStates state;
	private static String[] pedImages = new String[] {"img/p1.png", "img/p2.png","img/p3.png"};
	
	public Pedestrian(int position) throws Exception
	{
		Random rnd = new Random();
		int imgForPed = rnd.nextInt(3);
		this.img = ImageIO.read(new File(pedImages[imgForPed]));
		this.state = PedestrianStates.ENETERING;
		if(position == 0)
		{
			this.x = 570;
			this.y = 30;
		}
		
	}
}
