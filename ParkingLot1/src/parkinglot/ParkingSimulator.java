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
	boolean gateEntrance = false;
	boolean gateExit;
	boolean gateVipEntrance = false;
	boolean gateVipExit;
	int freeSpot;
	boolean[] spotLight  = new boolean[9];
	boolean[] carInSpot;
	
	
	//draw help vars
	boolean carHorizontal = false;
	boolean carInQWasFound = false;
	boolean carVipInQWasFound = false;
	boolean carVipInExitWasFound = false;
	boolean carInExitWasFound = false;
	
	// pedestrian light vars (sys)
	boolean pedetrianRightLight;
	boolean pedetrianLeftLight;
	
	BufferedImage carVipImage;
	BufferedImage carImage;
	BufferedImage carImageup;
	BufferedImage carVipImageup;
	BufferedImage carVipImageDown;
	BufferedImage carImageDown;
	BufferedImage gateOpen;
	BufferedImage gateClose;
	BufferedImage gateVipOpen;
	BufferedImage gateVipClose;
	BufferedImage gateExitOpen;
	BufferedImage gateExitClose;
	BufferedImage gateVipExitOpen;
	BufferedImage gateVipExitClose;
	BufferedImage greenCrossWalk;
	
	static LinkedList<Car> carsToAdd = new LinkedList<Car>();
	static LinkedList<Car> carsToRemove = new LinkedList<Car>();
	static LinkedList<Car> carsOut = new LinkedList<Car>();
	static LinkedList<Car> carList = new LinkedList<Car>();
	static LinkedList<Pedestrian> pedsList = new LinkedList<Pedestrian>();
	
	// pedestrian vars (env)
	static boolean pedUpRight = false; 
	static boolean pedDownRight = false;
	static boolean pedUpLeft = false;
	static boolean pedDownLeft = false;
	
	static Pedestrian pedestrianUpRight;
	static Pedestrian pedestrianUpLeft;
	static Pedestrian pedestrianDownRight;
	static Pedestrian pedestrianDownLeft;
	static boolean flag = false;
	static boolean flagVip = false;
	
	Thread thread;
	
	
	private static void executeAddPeds()
	{
		for(Pedestrian ped : pedsList)
		{
			int position = ped.position;
			if(position == 0)
			{
				pedUpRight = true;
				pedestrianUpRight = ped;
			}
			else if(position == 1)
			{
				pedDownRight = true;
				pedestrianDownRight = ped;
			}
			else if(position == 2)
			{
				pedUpLeft = true;
				pedestrianUpLeft = ped;
			}
			else
			{
				pedDownLeft = true;
				pedestrianDownLeft = ped;
			}
		}
		
	}
	
	private static void executeRemovePeds()
	{
		LinkedList<Pedestrian> pedsToDelete = new LinkedList<Pedestrian>();
		for(Pedestrian ped: pedsList)
		{
			if(ped.state == Pedestrian.States.CROSSING)
			{
				pedsToDelete.add(ped);
			}
		}
		for(Pedestrian ped : pedsToDelete)
		{
			pedsList.remove(ped);
		}
		pedsToDelete.clear();
	}
	
	// remove car list
	private static void executeRemoveCars()
	{
		for(Car car : carsToRemove)
		{
			if((Car.carsInExitQ == 0 && !car.isVipCar()) || (Car.carsVipInExitQ == 0 && car.isVipCar()))
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
	
	private static void executeAddCars() throws Exception
	{
		LinkedList<Car> lst = new LinkedList<Car>();
		flag = true;
		flagVip = true;
		for(Car car : carsToAdd)
		{
			carList.addLast(car);
			car.updateState(CarStates.INIT);
			Thread.sleep(3000);
			lst.add(car);
		}
		
		flag = false;
		flagVip = false;
		
		for(Car car: lst)
		{
			carsToAdd.remove(car);
		}
		lst.clear();
	}
	
	
	// API
	public static Car addVipCarEnterance() throws Exception
	{
		Car car = new Car(true);

		if(Car.carsVipInQ == 1)
		{
			System.out.println("Too many VIP cars");
		}
		else
		{
			while(flagVip)
			{
				Thread.sleep(1);
			}
			carsToAdd.add(car);
		}
		return car;
	}
	
	public static Car addCarEnterance() throws Exception
	{
		Car car = new Car(false);
		if(Car.carsInQ == 2)
		{
			System.out.println("Too many cars");
		}
		else 
		{
			while(flagVip)
			{
				Thread.sleep(1);
			}
			carsToAdd.addLast(car);
		}
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
	
	public static Pedestrian addPedestrian(int position) throws Exception
	{
		boolean alreadyExist = false;
		Pedestrian ped = new Pedestrian(position);
		for(Pedestrian pedes: pedsList)
		{
			if(pedes.position == position)
			{
				alreadyExist = true;
			}
		}
		if(!alreadyExist)
			pedsList.add(ped);
		return ped;
	}
	
	public ParkingSimulator() {
		Thread animationThread = new Thread(new Runnable() {
			public void run() {
				
				// Instantiate a new controller executor
				
				executor = new ControllerExecutor(true, false);

				while (true) {	
					try {
						executeAddCars();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					executeAddPeds();
					Map<String, String> sysValues;
					carInQWasFound = false;
					carInExitWasFound  = false;
					carVipInQWasFound = false;
					carVipInExitWasFound = false;
					try {
						carInSpot = new boolean [] {false,false,false,false,false,false,false,false};
						for(Car car : carList) {
							
							if(car.getState() == CarStates.IN_QUEUE_FIRST && !gateEntrance) {
								//System.out.println("car entrance true should follow gate entrance true");
								if(car.isVipCar())
								{
									executor.setInputValue("carVipEntrance", "true");
									carVipInQWasFound = true;

								}
								else
								{
									executor.setInputValue("carEntrance", "true");
									carInQWasFound = true;
								}
							}
							if(car.getState() == CarStates.EXITING) {
								if(car.isVipCar())
								{
									executor.setInputValue("carVipExit", "true");
									carVipInExitWasFound = true;
								}
								else
								{
									executor.setInputValue("carExit", "true");
									carInExitWasFound = true;

								}
								
							}
							//System.out.println("car in spot" + car.getParkingSpot());
							if(car.getState() == CarStates.PARKED || car.getState() == CarStates.ENTER_PARKING_LOT) 
							{	
								carInSpot[car.getParkingSpot()]= true;
							}
						}
						if(!carVipInQWasFound)
						{
							executor.setInputValue("carVipEntrance", "false");

						}
						if(!carInQWasFound) 
						{
							executor.setInputValue("carEntrance", "false");
						}
						if(!carInExitWasFound) 
						{
							executor.setInputValue("carExit", "false");
						}
						if(!carVipInExitWasFound)
						{
							executor.setInputValue("carVipExit", "false");

						}
						
						for (int i =0; i<numOfSpots;i++) 
						{
							executor.setInputValue("carInSpot" +"["+i+"]",carInSpot[i] ? "true" : "false");
						}
						
						if(pedUpRight || pedDownRight)
						{
							executor.setInputValue("pedestrianRight", "true");
						}
						else
						{
							executor.setInputValue("pedestrianRight", "false");
						}
						
						if(pedUpLeft || pedDownLeft)
						{
							executor.setInputValue("pedestrianLeft", "true");
						}
						else
						{
							executor.setInputValue("pedestrianLeft", "false");
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
					pedetrianLeftLight = sysValues.get("pedetrianLeftLight").equals("true") ? true : false;
					pedetrianRightLight = sysValues.get("pedetrianRightLight").equals("true") ? true : false;
					gateVipEntrance = sysValues.get("gateVipEntrance").equals("true") ? true : false;
					gateVipExit = sysValues.get("gateVipExit").equals("true") ? true : false;

					try {
						paintParkingLot();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					executeRemoveCars();
					executeRemovePeds();
					deleteOutCars();
					
				}

			}
		});
		
		// Load images
		try {
			parkingBackground  = ImageIO.read(new File("img/background.jpeg"));
			gateOpen = ImageIO.read(new File("img/gateB.png"));
			gateClose = ImageIO.read(new File("img/gateA.png"));
			gateExitOpen = ImageIO.read(new File("img/gateB.png"));
			gateExitClose = ImageIO.read(new File("img/gateA.png"));
			
			gateVipOpen = ImageIO.read(new File("img/gateB.png"));
			gateVipClose = ImageIO.read(new File("img/gateA.png"));
			gateVipExitOpen = ImageIO.read(new File("img/gateB.png"));
			gateVipExitClose = ImageIO.read(new File("img/gateA.png"));
			
			carImage = ImageIO.read(new File("img/car.png"));
			carVipImage = ImageIO.read(new File("img/carVip.png")); 
			greenCrossWalk = ImageIO.read(new File("img/greenCrossWalk.jpeg"));
			
			carImageup = ImageIO.read(new File("img/carUp.png"));
			carVipImageup = ImageIO.read(new File("img/carVipUp.png"));
			carImageDown = ImageIO.read(new File("img/carDown.png"));
			carVipImageDown = ImageIO.read(new File("img/carVipDown.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		animationThread.start();
		repaint();
	}
	
	
	
	private void paintParkingLot() throws Exception{
	
		try {	

			Thread.sleep(50);
			System.out.println("At the start of paint");
//			System.out.println("carEnters:" + carEnters + " carInQueue:"+carInQueue+" carParks" + carParks + " carPrepare" +carPrepareToExit + " carExit" + carExit);
			// paint relevant state by phase of car
			// now we support only 1 car
			for(Car car : carList) {
//				System.out.println("car" + car.getId() + " in state" + car.getState());
				switch (car.getState()) {
				
				case INIT :
					int stepsToMove;
//					System.out.println("Car: "+car.getId() +"enters parking lot..");
					if(Car.carsInQ==1) {
						stepsToMove = 200;
						//System.out.println("first car in line it is");
						car.updateState(CarStates.IN_QUEUE_FIRST);
					}
					else {
						stepsToMove = 50;
						//System.out.println("second car in line it is");
						if(!car.isVipCar())
							car.updateState(CarStates.IN_QUEUE_SECOND);
						else
						{
							stepsToMove = 200;
							car.updateState(CarStates.IN_QUEUE_FIRST);
						}
						
					}
					for (int i = 0; i < stepsToMove; i++) {
						repaint();
						Thread.sleep(10);
						if(car.isVipCar())
						{
							car.setX(car.getX()+1);

						}
						else
						{
							car.setX(car.getX()-1);
						}
					}
		
					//System.out.println("state"+car.getState());
					break;
				
				
				case IN_QUEUE_FIRST:
				
//					System.out.println("Car"+car.getId() +" waits first in queue..");
					// shouldn't move unless the gate is open
					boolean gate;
					if(car.isVipCar())
					{
						gate = gateVipEntrance;
					}
					else
					{
						gate = gateEntrance;
					}
					if (gate)
					{
//						System.out.println("Car is entering!!!");
						for (int i = 0; i < 130; i++) {
							repaint();
							Thread.sleep(10);
							if(car.isVipCar())
							{
								car.setX(car.getX()+1);

							}
							else
							{
								car.setX(car.getX()-1);
							}
						}
						System.out.println("Free spot "  + freeSpot);
						car.updateState(CarStates.ENTER_PARKING_LOT, freeSpot);
					}		
				
					break;
					
				case IN_QUEUE_SECOND:
//					System.out.println("Car"+car.getId() +" waits second in queue..");
					// shouldn't move unless the gate is open
					if(car.isVipCar())
					{
						gate = gateVipEntrance;
					}
					else
					{
						gate = gateEntrance;
					}
					if (gate)
					{
//						System.out.println("Car is moving forward the gate!!!");
						for (int i = 0; i < 150; i++) {
							repaint();
							Thread.sleep(10);
							if(!car.isVipCar())
								car.setX(car.getX()-1);
							else
								car.setX(car.getX()+1);
						}
						car.updateState(CarStates.WAIT);
					}
					break;
				case WAIT:
					car.updateState(CarStates.IN_QUEUE_FIRST);
					break;
					
				case ENTER_PARKING_LOT:
//					System.out.println("Car parks..");
					
					int down = ParkingSpotsConfig.configure.get(car.getParkingSpot())[0];
					int straight = ParkingSpotsConfig.configure.get(car.getParkingSpot())[1];
					int up = ParkingSpotsConfig.configure.get(car.getParkingSpot())[2];
					
					if(car.isVipCar())
					{
						down = ParkingSpotsConfig.configureVip.get(car.getParkingSpot())[0];
						straight = ParkingSpotsConfig.configureVip.get(car.getParkingSpot())[1];
						up = ParkingSpotsConfig.configureVip.get(car.getParkingSpot())[2];
					}
						
					for (int i = 0; i <straight; i++) {
						repaint();
						Thread.sleep(10);
						if(car.isVipCar())
							car.setX(car.getX()+1);
						else
							car.setX(car.getX()-1);
					}
					if(up != 0)
					{
						if(car.isVipCar())
						{
							car.setImg(carVipImageup);
						}	
						else
						{
							car.setImg(carImageup);
						}
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
						if(car.isVipCar())
						{
							car.setImg(carVipImageDown);
						}
						else
							car.setImg(carImageDown);
						car.setHeight(95);
						car.setWidth(51);
						
					}
					for (int i = 0; i < down; i++) {
						repaint();
						Thread.sleep(10);
						car.setY(car.getY()+1);
					}
					car.updateState(CarStates.PARKED);
					break;
					
				case PREPARE_TO_EXIT:
//					System.out.println("Car parks..");
					
					
					int downExit = ParkingSpotsConfig.configure.get(car.getParkingSpot())[2];
					int straightExit = 240 - ParkingSpotsConfig.configure.get(car.getParkingSpot())[1];
					int upExit = ParkingSpotsConfig.configure.get(car.getParkingSpot())[0];
					
					if(car.isVipCar())
					{
						downExit = ParkingSpotsConfig.configureVip.get(car.getParkingSpot())[0];
						straightExit = 300 - ParkingSpotsConfig.configureVip.get(car.getParkingSpot())[1];
						upExit = ParkingSpotsConfig.configureVip.get(car.getParkingSpot())[2];
					}
					
					if(upExit != 0)
					{
						if(car.isVipCar())
						{
							car.setImg(carVipImageup);
								
						}
						else
						{
							car.setImg(carImageDown);
							
						}
						car.setHeight(95);
						car.setWidth(51);
						
					}
					
					for (int i = 0; i < upExit; i++) {
						repaint();
						Thread.sleep(10);
						if(car.isVipCar())
							car.setY(car.getY()+1);
						else
							car.setY(car.getY()-1);
					}
					
					if(downExit != 0)
					{
						if(car.isVipCar())
						{
							car.setImg(carVipImageDown);
						}
						{
							car.setImg(carImageup);
						}
						
						car.setHeight(95);
						car.setWidth(51);
						
					}
					
					for (int i = 0; i < downExit; i++) {
						repaint();
						Thread.sleep(10);
						if(car.isVipCar())
							car.setY(car.getY()-1);
						else
							car.setY(car.getY()+1);
					}
					if(car.isVipCar())
					{
						car.setImg(carVipImage);
					}
					else
						car.setImg(carImage);
					car.setHeight(51);
					car.setWidth(95);
					for (int i = 0; i <straightExit; i++) {
						repaint();
						Thread.sleep(10);
						if(car.isVipCar())
							car.setX(car.getX()+1);
						else
							car.setX(car.getX()-1);
					}
					if(Car.carsInExitQ == 0 && !car.isVipCar())
					{
						car.updateState(CarStates.EXITING);
					}
					else if (Car.carsInExitQ != 0 && !car.isVipCar())
					{
						car.updateState(CarStates.WAIT_BEFORE_EXIT);
					}
					else if(Car.carsVipInExitQ == 0 && car.isVipCar())
					{
						car.updateState(CarStates.EXITING);
					}
					else if (Car.carsVipInExitQ != 0 && car.isVipCar())
					{
						car.updateState(CarStates.WAIT_BEFORE_EXIT);
					}
					break;
				case WAIT_BEFORE_EXIT:
					if(Car.carsInExitQ == 1 && !car.isVipCar())
					{
						car.updateState(CarStates.EXITING);
					}
					if(Car.carsVipInExitQ == 1 && car.isVipCar())
					{
						car.updateState(CarStates.EXITING);
					}
					break;	
				case EXITING:
					if(car.isVipCar())
					{
						gate = gateVipExit;
					}
					else
					{
						gate = gateExit;
					}
					if(gate) {
//						System.out.println("Car is exiting!!");
						for (int i = 0; i < 500; i++) {
							repaint();
							Thread.sleep(10);
							if(car.isVipCar())
								car.setX(car.getX()+1);
							else
								car.setX(car.getX()-1);
						}
						carsOut.add(car);
					}	
				default:
					break;
				}
				
			}// for carsList
			
			// pedestrian graphics
			if(pedUpRight)
			{
				pedUpRight = handlePed(pedestrianUpRight, 0);
			}
			
			if(pedDownRight)
			{
				pedDownRight= handlePed(pedestrianDownRight, 1);
			}
			
			if(pedUpLeft)
			{
				pedUpLeft = handlePed(pedestrianUpLeft, 2);
			}
			if(pedDownLeft)
			{
				pedDownLeft = handlePed(pedestrianDownLeft, 3);
			}
			Thread.sleep(50);
			repaint();
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private boolean handlePed(Pedestrian ped, int pos) throws Exception
	{
		boolean pedLight;
		boolean pedDirUp;
		if(pos == 0 || pos == 1)
		{
			pedLight = pedetrianRightLight;
		}
		else
		{
			pedLight = pedetrianLeftLight;
		}
		if(pos == 0 || pos == 2)
		{
			pedDirUp = true;
		}
		else
		{
			pedDirUp = false;
		}
		if(ped.state == Pedestrian.States.ENETERING)
		{
			for (int i = 0; i < 100; i++) {
				repaint();
				Thread.sleep(10);
				if(pedDirUp)
				{
					ped.y++;
				}
				else 
				{
					ped.y--;
				}
				
			}
			ped.state = Pedestrian.States.WAITING;
		}
		if(ped.state == Pedestrian.States.WAITING)
		{
			if(pedLight)
			{
				for (int i = 0; i < 400; i++) {
					repaint();
					Thread.sleep(10);
					if(pedDirUp)
					{
						ped.y++;
					}
					else
					{
						ped.y--;
					}
					
				}
				ped.state = Pedestrian.States.CROSSING;
				return false;
			}
		}
		return true;
	}

	@Override
	public void paintComponent(Graphics g) {

		g.drawImage(parkingBackground,0,0,null);
		if(pedetrianRightLight)
		{
			g.drawImage( greenCrossWalk, 693, 160, 38, 152, null);
		}
		if(pedetrianLeftLight)
		{
			g.drawImage( greenCrossWalk, 340, 160, 38, 152, null);
		}
		g.setColor(Color.CYAN);
		for(Car car: carList) {
			g.drawImage(car.getImg(),car.getX(),car.getY(), car.getWidth(), car.getHeight(), null);
		}
		if(!gateEntrance)
		{
			g.drawImage(gateClose, 740, 159, 36, 60, null);
		}
		else
		{
			g.drawImage(gateOpen, 740, 159, 36, 60, null);
		}
		if(!gateExit)
		{
			g.drawImage(gateClose, 380, 159, 36, 60, null);
		}
		else
		{
			g.drawImage(gateOpen, 380, 159, 36, 60, null);
		}
		if(!gateVipEntrance)
		{
			g.drawImage(gateVipClose, 320, 245, 36, 60, null);
		}
		else
		{
			g.drawImage(gateVipOpen, 320, 245, 36, 60, null);
		}
		if(!gateVipExit)
		{
			g.drawImage(gateVipClose, 680, 245, 36, 60, null);
		}
		else
		{
			g.drawImage(gateVipOpen, 680, 245, 36, 60, null);
		}
		if(pedUpRight)
		{
			g.drawImage( pedestrianUpRight.img, pedestrianUpRight.x, pedestrianUpRight.y, 30, 30, null);
		}
		if(pedUpLeft)
		{
			g.drawImage( pedestrianUpLeft.img, pedestrianUpLeft.x, pedestrianUpLeft.y, 30, 30, null);
		}
		if(pedDownRight)
		{
			g.drawImage( pedestrianDownRight.img, pedestrianDownRight.x, pedestrianDownRight.y, 30, 30, null);
		}
		if(pedDownLeft)
		{
			g.drawImage( pedestrianDownLeft.img, pedestrianDownLeft.x, pedestrianDownLeft.y, 30, 30, null);
		}
			
	}

	
	// initialize Parking Lot 
	public static void main(String args[]) throws Exception {
		ParkingSpotsConfig.initMap();
		JFrame f = new JFrame("ParkingLot Simulator");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setSize(1093, 490);
		ParkingSimulator parkingLot = new ParkingSimulator();
		f.setContentPane(parkingLot);
		f.setVisible(true);
		Scenarios.createRandomScenario();
//		Scenarios.createForthScenario();
//		Scenarios.createTestScenario();
		
	}

	
}
