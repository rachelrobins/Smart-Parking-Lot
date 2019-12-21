package parkinglot;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
	
	Random random = new Random();
	int numOfSpots = 8;

	// system values
	boolean gateEntrance= false;
	boolean gateExit;
	int freeSpot;
	boolean[] spotLight  = new boolean[9];
	boolean[] carInSpot;
	
	
	//draw help vars
	boolean carHorizontal = false;
	boolean carInQWasFound = false;
	boolean carInExitWasFound = false;
	
	BufferedImage carImage;
	BufferedImage carImageup;
	BufferedImage carImageDown;
	BufferedImage gateOpen;
	BufferedImage gateClose;
	BufferedImage gateExitOpen;
	BufferedImage gateExitClose;
	
	static LinkedList<Car> carsToAdd = new LinkedList<Car>();
	static LinkedList<Car> carsToRemove = new LinkedList<Car>();
	static LinkedList<Car> carsOut = new LinkedList<Car>();
	
	static LinkedList<Car> carList = new LinkedList<Car>();
	Thread thread;
	
	// remove car list
	private static void executeRemoveCars()
	{
		for(Car car : carsToRemove)
		{
			if(Car.carsInExitQ == 0)
			{
				car.updateState(CarStates.PREPARE_TO_EXIT);
				carsToRemove.remove(car);
			}
			
		}
	}
	
	private static void deleteOutCars()
	{
		for(Car car : carsOut)
		{
			carList.remove(car);
		}
		carsOut.clear();
	}
	
	private static void executeAddCars()
	{
		for(Car car : carsToAdd)
		{
			car.updateState(CarStates.INIT);
			carList.addLast(car);
			carsToAdd.remove(car);
		}
	}
	
	// API
	public static Car addCarEnterance()
	{
		Car car = new Car();
		carsToAdd.addLast(car);
		return car;
	}
	
	public static void removeCarFromParkingLot(int carID)
	{
		
		for(Car car : carList)
		{
			if(car.getId() == carID)
			{
				carsToRemove.add(car);
				return;	
			}
		}
	}
	
	
	public ParkingSimulator() {
		Thread animationThread = new Thread(new Runnable() {
			public void run() {
				
				// Instantiate a new controller executor
				
				executor = new ControllerExecutor(true, false);

				while (true) {	
					executeAddCars();
					Map<String, String> sysValues;
					carInQWasFound = false;
					carInExitWasFound  = false;
					try {
						carInSpot = new boolean [] {false,false,false,false,false,false,false,false};
					for(Car car : carList) {
						
						if(car.getState() == CarStates.IN_QUEUE_FIRST && !gateEntrance) {
							//System.out.println("car entrance true shold follow gate entrance true");
							executor.setInputValue("carEntrance", "true");
							carInQWasFound = true;
						}
						if(car.getState() == CarStates.EXITING) {
							
							executor.setInputValue("carExit", "true");

							carInExitWasFound = true;
						}
						//System.out.println("car in spot" + car.getParkingSpot());
						if(car.getState() == CarStates.PARKED) {
							
							carInSpot[car.getParkingSpot()]= true;
						}
					}
						
					if(!carInQWasFound) {
						executor.setInputValue("carEntrance", "false");
					}
					if(!carInExitWasFound) {
							executor.setInputValue("carExit", "false");
					}
					for (int i =0; i<numOfSpots;i++) {
						executor.setInputValue("carInSpot" +"["+i+"]",carInSpot[i] ? "true" : "false");
					}
					
					
					}
					catch (ControllerExecutorException e) {
						e.printStackTrace();
					}
									
					// Paint the street on the screen
					try {
						executor.updateState(true);
					} catch (ControllerExecutorException e) {
						e.printStackTrace();
					}
					sysValues = executor.getCurOutputs();
					//System.out.println(sysValues.get("spotsCounter"));
					gateEntrance = sysValues.get("gateEntrance").equals("true") ? true : false;
					//System.out.println("the gate is"+gateEntrance);
					gateExit = sysValues.get("gateExit").equals("true") ? true : false;
					freeSpot  = Integer.parseInt(sysValues.get("freeSpot"));
					System.out.println("spot Lights:");
					for(int i = 0;i<numOfSpots+1;i++) {
						spotLight[i] = sysValues.get("spotLight"+"["+ i +"]").equals("true") ? true : false;
						System.out.println("spot light " + i + "" + spotLight[i]);
					}
					paintParkingLot();
					executeRemoveCars();
					deleteOutCars();
					//paintParkingLot();
				}

			}
		});
		
		// Load images
		try {
			parkingBackground  = ImageIO.read(new File("img/background.png"));
			gateOpen = ImageIO.read(new File("img/gateB.png"));
			gateClose = ImageIO.read(new File("img/gateA.png"));
			gateExitOpen = ImageIO.read(new File("img/gateB.png"));
			gateExitClose = ImageIO.read(new File("img/gateA.png"));
			carImage = ImageIO.read(new File("img/car.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		animationThread.start();
		repaint();
	}
	
	
	
	private void paintParkingLot(){
	
		try {	

			Thread.sleep(50);
			System.out.println("At the start of paint");
//			System.out.println("carEnters:" + carEnters + " carInQueue:"+carInQueue+" carParks" + carParks + " carPrepare" +carPrepareToExit + " carExit" + carExit);
			// paint relevant state by phase of car
			// now we support only 1 car
			for(Car car : carList) {
				System.out.println("car" + car.getId() + " in state" + car.getState());
				switch (car.getState()) {
				
				case INIT :
					int stepsToMove;
					System.out.println("Car: "+car.getId() +"enters parking lot..");
					if(Car.carsInQ==0) {
						stepsToMove = 40;
						//System.out.println("first car in line it is");
						car.updateState(CarStates.IN_QUEUE_FIRST);
					}
					else {
						stepsToMove = 20;
						//System.out.println("second car in line it is");
						car.updateState(CarStates.IN_QUEUE_SECOND);
					}
					for (int i = 0; i < stepsToMove; i++) {
						repaint();
						Thread.sleep(10);
						car.setX(car.getX()-1);
					}
		
					//System.out.println("state"+car.getState());
					break;
				
				
				case IN_QUEUE_FIRST:
				
					System.out.println("Car"+car.getId() +" waits first in queue..");
					// shouldn't move unless the gate is open
					if (gateEntrance)
					{
						System.out.println("Car is entering!!!");
						for (int i = 0; i < 150; i++) {
							repaint();
							Thread.sleep(10);
							car.setX(car.getX()-1);
						}
						car.updateState(CarStates.ENTER_PARKING_LOT);
					}		
				
					break;
					
				case IN_QUEUE_SECOND:
					System.out.println("Car"+car.getId() +" waits second in queue..");
					// shouldn't move unless the gate is open
					if (gateEntrance)
					{
						System.out.println("Car is moving forward the gate!!!");
						for (int i = 0; i < 20; i++) {
							repaint();
							Thread.sleep(10);
							car.setX(car.getX()-1);
						}
						car.updateState(CarStates.WAIT);
					}
					break;
				case WAIT:
					car.updateState(CarStates.IN_QUEUE_FIRST);
					break;
					
				case ENTER_PARKING_LOT:
					System.out.println("Car parks..");
					try
					{
						carImageup = ImageIO.read(new File("img/carUp.png"));
						carImageDown = ImageIO.read(new File("img/carDown.png"));
					}
					catch (Exception e)
					{
						
					}
					System.out.println("freeSpot " + freeSpot);
					int down = ParkingSpotsConfig.configure.get(freeSpot)[0];
					int straight = ParkingSpotsConfig.configure.get(freeSpot)[1];
					int up = ParkingSpotsConfig.configure.get(freeSpot)[2];
					for (int i = 0; i <straight; i++) {
						repaint();
						Thread.sleep(10);
						car.setX(car.getX()-1);
					}
					if(up != 0)
					{
						car.setImg(carImageup);
						car.setHeight(95);
						car.setWidth(51);
					}
					for (int i = 0; i < up; i++) {
						repaint();
						Thread.sleep(10);
						car.setY(car.getY()-1);
					}
					if(down != 0)
					{
						car.setImg(carImageDown);
						car.setHeight(95);
						car.setWidth(51);
						
					}
					for (int i = 0; i < down; i++) {
						repaint();
						Thread.sleep(10);
						car.setY(car.getY()+1);
					}
					car.updateState(CarStates.PARKED,freeSpot);
					break;
					
				case PREPARE_TO_EXIT:
					System.out.println("Car parks..");
					try
					{
						carImageup = ImageIO.read(new File("img/carUp.png"));
						carImageDown = ImageIO.read(new File("img/carDown.png"));
					}
					catch (Exception e)
					{
						
					}
					
					int downExit = ParkingSpotsConfig.configure.get(car.getParkingSpot())[2];
					int straightExit = 250 - ParkingSpotsConfig.configure.get(car.getParkingSpot())[1];
					int upExit = ParkingSpotsConfig.configure.get(car.getParkingSpot())[0];
					
					if(upExit != 0)
					{
						car.setImg(carImageDown);
						car.setHeight(95);
						car.setWidth(51);
						
					}
					
					for (int i = 0; i < upExit; i++) {
						repaint();
						Thread.sleep(10);
						car.setY(car.getY()-1);
					}
					
					if(downExit != 0)
					{
						car.setImg(carImageup);
						car.setHeight(95);
						car.setWidth(51);
						
					}
					
					for (int i = 0; i < downExit; i++) {
						repaint();
						Thread.sleep(10);
						car.setY(car.getY()+1);
					}
					car.setImg(carImage);
					car.setHeight(51);
					car.setWidth(95);
					for (int i = 0; i <straightExit; i++) {
						repaint();
						Thread.sleep(10);
						car.setX(car.getX()-1);
					}
					if(Car.carsInExitQ == 0)
					{
						car.updateState(CarStates.EXITING);
					}
					else 
					{
						car.updateState(CarStates.WAIT_BEFORE_EXIT);
					}
					break;
				case WAIT_BEFORE_EXIT:
					if(Car.carsInExitQ == 1)
					{
						car.updateState(CarStates.EXITING);
					}
					break;	
				case EXITING:
					if(gateExit) {
						System.out.println("Car is exiting!!");
						for (int i = 0; i < 200; i++) {
							repaint();
							Thread.sleep(10);
							car.setX(car.getX()-1);
						}
						carsOut.add(car);
					}	
				default:
					break;
				}
				
				}
				
			Thread.sleep(50);
			repaint();
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		g.drawImage(parkingBackground,0,0,null);
		g.setColor(Color.CYAN);
		for(Car car: carList) {
			g.drawImage(car.getImg(),car.getX(),car.getY(), car.getWidth(), car.getHeight(), null);
		}
		if(!gateEntrance)
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

	
	// initialize Parking Lot 
	public static void main(String args[]) throws Exception {
		ParkingSpotsConfig.initMap();
		JFrame f = new JFrame("ParkingLot Simulator");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setSize(800, 500);
		ParkingSimulator parkingLot = new ParkingSimulator();
		f.setContentPane(parkingLot);
		f.setVisible(true);
		Scenarios.createRandomScenario();
	}

	
}
