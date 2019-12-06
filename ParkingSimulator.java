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
	boolean carSide;
	boolean carExit;
	String counterSpot;
	BufferedImage parkingBackground;
	int carLeft;
	int carRightX;
	int carRightY;
	int carMainY;
	boolean carInQueue = false;
	boolean carEnterance;
	boolean carEnteranceFirst;
	boolean carEnteranceSecond;
	boolean ambulanceSide;
	boolean greenSide;
	boolean greenMain;
	boolean carMain;
	boolean carInParkingLot = false;
	Random random = new Random();
	boolean startMode = false;
	int counter = 0;
	// system values
	boolean gateEnterance;
	boolean gateExit;
	
	
	BufferedImage car;
	BufferedImage ambulance;
	BufferedImage gate;
	
	Thread thread;

	public ParkingSimulator() {
		Thread animationThread = new Thread(new Runnable() {
			public void run() {
				
				// Instantiate a new controller executor
				
				executor = new ControllerExecutor(true, false);

				while (true) {	
					Map<String, String> sysValues;
					if(!startMode) {
						carEnterance = random.nextInt(2) == 0 ? false : true;
						startMode = true;
					}
					else {
						if(gateEnterance) {
							carEnterance = random.nextInt(2) == 0 ? false : true;
						}
						else if(carEnterance && !gateEnterance) {
							carEnterance = carEnterance? true : false;	
						}
						else {
							carEnterance = random.nextInt(2) == 0 ? false : true;
						}
						
						
						
					}
					try {
						
						carExit = false;
						executor.updateState(true);
					} catch (ControllerExecutorException e) {
						e.printStackTrace();
					}
					sysValues = executor.getCurOutputs();
					gateEnterance = sysValues.get("gateEnterance").equals("true") ? true : false;
					System.out.println(gateEnterance);
					gateExit = sysValues.get("gateExit").equals("true") ? true : false;
					System.out.println("gate exit is "+ gateExit);
					counterSpot = sysValues.get("spotsCounter");
					System.out.println(sysValues.get("gateExit"));
					System.out.println("car exit is :" + carExit);

					if(!carInQueue)
					{
						carRightX = 640 + 50;
						carRightY = 159;					
					}
				
					// Paint the street on the screen
					paintParkingLot();
				}

			}
		});
		
		// Load images
		try {
			carRightX = 640 + 50;
			carRightY = 159;
			car = ImageIO.read(new File("img/car.jpg"));
			ambulance = ImageIO.read(new File("img/ambulance.jpg"));
			parkingBackground  = ImageIO.read(new File("img/picccccc.png"));
			gate = ImageIO.read(new File("img/gategatw.png"));
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		animationThread.start();
		repaint();
	}
	
	
	
	private void paintParkingLot() {
		System.out.println("here " + counter++);
		System.out.println("counter is " + counterSpot);
	
		try {			
			Thread.sleep(50);
			// gate opened
			if(gateEnterance)
			{
				if(carInQueue)
				{
					if(!carInParkingLot)
					{
						carRightX = 650;
						for (int i = 0; i < 30; i++) {
							repaint();
							Thread.sleep(50);
							carRightX--;
						}	
						// enters first parking spot
						carInParkingLot = true;
						Thread.sleep(50);
					}
					else
					{
						for (int i = 0; i < 30; i++) {
							repaint();
							Thread.sleep(50);
							carRightX--;
						}
						Thread.sleep(50);
						carInParkingLot = false;
					}
				}
				// car arriving to the gate
				for (int i = 0; i < 100; i++) {
					repaint();
					Thread.sleep(50);
					carRightX--;
				}
				Thread.sleep(50);
				carInQueue = false;
			}
			else
			{
				if(carEnteranceFirst && !carInQueue)
				{
					// car arriving to the gate
					for (int i = 0; i < 40; i++) {
						repaint();
						Thread.sleep(50);
						carRightX--;
					}
					Thread.sleep(50);
					carInQueue = true;
				}
			}
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
		g.drawImage(car, carRightX, carRightY, 60, 60, null);
		if(!gateEnterance)
		{
			g.drawImage(gate, 610, carRightY, 36, 60, null);
		}
		
	}

	public static void main(String args[]) throws Exception {
		JFrame f = new JFrame("Traffic Simulator");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setSize(800, 500);
		ParkingSimulator traffic = new ParkingSimulator();
		f.setContentPane(traffic);
		f.setVisible(true);
	}
}
