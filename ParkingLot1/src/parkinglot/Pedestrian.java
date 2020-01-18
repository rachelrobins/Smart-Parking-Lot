package parkinglot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

/* This class represents pedestrians an an object in the parking lot */ 
/* each pedestrian has an image(selected randomly), state, x,y axis and position regarding cross walk */ 

public class Pedestrian {
	int x;
	int y;
	BufferedImage img;
	Pedestrian.States state;
	int position;
	private static String[] pedImages = new String[] {"img/p1.png", "img/p2.png","img/p3.png", "img/p4.png"};
	
	public Pedestrian(int position) throws Exception
	{
		Random rnd = new Random();
		int imgForPed = rnd.nextInt(4); // pedestrian img is random
		this.img = ImageIO.read(new File(pedImages[imgForPed]));
		this.state = Pedestrian.States.ENETERING;
		this.position = position;
		
		if(position == 0) //on the right and on the top
		{
			this.x = 790;
			this.y = 30;
		}
		else if(position == 1) //on the right and on the bottom
		{
			this.x = 790;
			this.y = 400;
		}
		else if(position == 2) //on the left and on the top
		{
			this.x = 270;
			this.y = 30;
		}
		else //on the left and on the bottom
		{
			this.x = 270;
			this.y = 400;
		}
		
	}
	
	// enum for pedestrian states
	enum States 
	{
		ENETERING,
		WAITING,
		CROSSING,
	}
}


