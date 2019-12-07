package parkinglot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.util.Map;
import java.util.Random;

import tau.smlab.syntech.executor.ControllerExecutor;
import tau.smlab.syntech.executor.ControllerExecutorException;

public class ParkingSimulator extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ControllerExecutor executor;
	
	String counterSpot;
	BufferedImage parkingBackground;
	int carLeft;
	int carRightX;
	int carRightY;
	boolean carInQueueExit = false;

	boolean carEnteranceFirst;
	boolean carEnteranceSecond;
	boolean carInParkingLot = false;
	Random random = new Random();
	boolean startMode = false;
	boolean carEnterance;
	
	// phases of car
	boolean carEnters;
	boolean carInQueue = false;
	boolean carParks = false;
	boolean carPrepareToExit = false;
	boolean carExit;
	boolean alreadyP = false;
	
	// system values
	boolean gateEnterance;
	boolean gateExit;
	
	//draw help vars
	boolean carHorizontal = false;
	
	
	BufferedImage car;
	BufferedImage ambulance;
	BufferedImage gateOpen;
	BufferedImage gateClose;
	BufferedImage gateExitOpen;
	BufferedImage gateExitClose;
	
	Thread thread;
	
	// this is going to be the API
	private static void addCarEnterance()
	{
		// TODO we need to create Car class and update it
//		carEnterance = true;
	}
	
	// here we will add which car to remove
	// TODO we need to receive id of car to remove
	private static void removeCarFromParkingLot()
	{
//		carExit = true;
	}
	
	
	public ParkingSimulator() {
		Thread animationThread = new Thread(new Runnable() {
			public void run() {
				
				// Instantiate a new controller executor
				
				executor = new ControllerExecutor(true, false);

				while (true) {	
					Map<String, String> sysValues;
					//first simulation - car enters, parks and then exits
					if(!startMode) {
						carEnters = true;
						carEnterance = false;
						carExit = false;
//						carEnterance = random.nextInt(2) == 0 ? false : true;
						startMode = true;
					}
					else {
						if (gateEnterance && carInQueue)
						{
							carEnterance = false;
						}
						else
						{
							carEnterance = carEnterance ? true : false;
						}
						if (gateExit && carExit)
						{
							carExit = carExit ? true : false;
						}
						// we will make car leave
						else
						{
							carPrepareToExit = alreadyP ? false : true;
							carExit = carExit ? true : false;
							
							// TODO - the line below is the one needed but we put true only for simulation
//							carExit = carExit ? true : false;
						}
//						//first simulation - car enters, parks and then exits
//						if(!carEnterance) 
//						{
//							carEnterance = true;
//							carExit = false;	
//						}
//						else if(carExit)
//						{
//							carEnterance = true;
//							carExit = true;
//						}
//						else 
//						{
//							carEnterance = false;
//							carExit = true;
//						}
						
//						if(gateEnterance) {
//							carEnterance = random.nextInt(2) == 0 ? false : true;
//						}
//						else if(carEnterance && !gateEnterance) {
//							carEnterance = carEnterance ? true : false;	
//						}
//						else {
//							carEnterance = random.nextInt(2) == 0 ? false : true;
//						}
//						
//						if(gateExit)
//						{
//							carExit = random.nextInt(2) == 0 ? false : true;
//						}
//						else
//						{
//							if(carExit)
//							{
//								carExit = true;
//							}
//							else 
//							{
//								carExit = random.nextInt(2) == 0 ? false : true;
//							}
//							
//						}
						
						
					}
					try {
						executor.updateState(true);
					} catch (ControllerExecutorException e) {
						e.printStackTrace();
					}
					sysValues = executor.getCurOutputs();
					gateEnterance = sysValues.get("gateEnterance").equals("true") ? true : false;
					System.out.println("gate enterance " + gateEnterance);
					gateExit = sysValues.get("gateExit").equals("true") ? true : false;
					counterSpot = sysValues.get("spotsCounter");
//					System.out.println(sysValues.get("gateExit"));
					System.out.println("gate exit is :" + gateExit);
//					System.out.println("car exit is :" + carExit);

//					if(!carInQueue)
//					{
//						carRightX = 640 + 50;
//						carRightY = 159;					
//					}
				
					// Paint the street on the screen
					paintParkingLot();
				}

			}
		});
		
		// Load images
		try {
			carRightX = 640 + 50;
			carRightY = 159;
			car = ImageIO.read(new File("img/car.png"));
			ambulance = ImageIO.read(new File("img/ambulance.jpg"));
			parkingBackground  = ImageIO.read(new File("img/background.png"));
			gateOpen = ImageIO.read(new File("img/gateOpen.png"));
			gateClose = ImageIO.read(new File("img/gateClosed.png"));
			gateExitOpen = ImageIO.read(new File("img/gateOpen.png"));
			gateExitClose = ImageIO.read(new File("img/gateClosed.png"));
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		animationThread.start();
		repaint();
	}
	
	
	
	private void paintParkingLot() {
		System.out.println("counter is " + counterSpot);
	
		try {			
			Thread.sleep(50);
			System.out.println("At the start of paint");
			System.out.println("carEnters:" + carEnters + " carInQueue:"+carInQueue+" carParks" + carParks + " carPrepare" +carPrepareToExit + " carExit" + carExit);
			// paint relevant state by phase of car
			// now we support only 1 car
			if(carEnters)
			{
				System.out.println("Car enters parking lot..");
				
				for (int i = 0; i < 40; i++) {
					repaint();
					Thread.sleep(10);
					carRightX--;
				}
				carEnters = false;
				carInQueue = true;
			}
			else if(carInQueue)
			{
				System.out.println("Car waits in queue..");
				// shouldn't move unless the gate is open
				if (gateEnterance)
				{
					System.out.println("Car is entering!!!");
					for (int i = 0; i < 150; i++) {
						repaint();
						Thread.sleep(10);
						carRightX--;
					}
					carInQueue = false;
					carParks = true;
				}
				else
				{
					carEnterance = true;
				}
				
			}
			else if(carParks)
			{
				carHorizontal = true;
				System.out.println("Car parks..");
				// TODO park the car for real
				try
				{
					car = ImageIO.read(new File("img/carParks.png"));	
				}
				catch (Exception e)
				{
					//TODO
				}
				
				
				for (int i = 0; i < 120; i++) {
					repaint();
					Thread.sleep(10);
					carRightY--;
				}
				carParks = false;
			}
			else if(carPrepareToExit)
			{
				
				carHorizontal = true;

				System.out.println("Car prepares to leave..");
				for (int i = 0; i < 120; i++) {
					repaint();
					Thread.sleep(10);
					carRightY++;
				}
				try
				{
					car = ImageIO.read(new File("img/car.png"));
					carHorizontal = false;

				}
				catch (Exception e)
				{
//					//TODO
				}
				for (int i = 0; i < 255; i++) {
					repaint();
					Thread.sleep(10);
					carRightX--;
				}
				Thread.sleep(50);
				carPrepareToExit = false;
				alreadyP = true;
				carExit = true;
			}
			else if(carExit)
			{
				System.out.println("Car waits to exit parking lot..");
				// when gate is open the car exits
				if(gateExit)
				{
					System.out.println("Car is exiting!!");
					for (int i = 0; i < 190; i++) {
						repaint();
						Thread.sleep(10);
						carRightX--;
					}
					carExit = false;
				}
				
				Thread.sleep(50);
				
			}
//			// gate opened
//			if(gateEnterance)
//			{
//				// car is waiting in queue
//				if(carInQueue)
//				{
//					// car isn't parking
//					if(!carInParkingLot)
//					{
////						carRightX = 650;
//						for (int i = 0; i < 30; i++) {
//							repaint();
//							Thread.sleep(50);
//							carRightX--;
//						}	
//						// enters first parking spot
//						carInParkingLot = true;
//						Thread.sleep(50);
//					}
//					// car is parking
//					else
//					{
//						for (int i = 0; i < 200; i++) {
//							repaint();
//							Thread.sleep(50);
//							carRightX--;
//						}
//						Thread.sleep(50);
//						
//						carInParkingLot = false;
//					}
//				}
//				// car is not in queue == new car is arriving
//				else
//				{
//					// car arriving to the gate
//					for (int i = 0; i < 100; i++) {
//						repaint();
//						Thread.sleep(50);
//						carRightX--;
//					}
//					Thread.sleep(50);
//					carInQueue = true;					
//				}
//				
//			}
//			// entrance gate is closed
//			else
//			{
//				// new car is arriving
//				if(carEnterance && !carInQueue)
//				{
//					// car arriving to the gate
//					for (int i = 0; i < 40; i++) {
//						repaint();
//						Thread.sleep(50);
//						carRightX--;
//					}
//					Thread.sleep(50);
//					carInQueue = true;
//				}
//			}
//			//if car wants to exit and gate is open
//			if(carExit && gateExit)
//			{
//				//car exits
//				carRightX = 420;
//				for (int i = 0; i < 300; i++) {
//					repaint();
//					Thread.sleep(50);
//					carRightX--;
//				}
//				Thread.sleep(50);
//				
//			}
			repaint();
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		g.drawImage(parkingBackground,0,0,null);
		g.setColor(Color.CYAN);
		if(!carHorizontal)
		{
			g.drawImage(car, carRightX, carRightY, 95, 51, null);
		}
		else
		{
			g.drawImage(car, carRightX, carRightY, 51, 94, null);
		}
		
		if(!gateEnterance)
		{
			g.drawImage(gateClose, 610, 159, 36, 60, null);
		}
		else
		{
			g.drawImage(gateOpen, 610, 159, 36, 60, null);
		}
		if(!gateExit)
		{
			g.drawImage(gateClose, 210, 159, 36, 60, null);
		}
		else
		{
			g.drawImage(gateOpen, 210, 159, 36, 60, null);
		}
		
	}

	private static void firstSimulation() throws Exception
	{
		addCarEnterance();
		Thread.sleep(1000);
		removeCarFromParkingLot();
	}
	
	public static void main(String args[]) throws Exception {
		JFrame f = new JFrame("Traffic Simulator");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setSize(800, 500);
		ParkingSimulator traffic = new ParkingSimulator();
		f.setContentPane(traffic);
		f.setVisible(true);
		firstSimulation();
	}
}
