package parkinglot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.util.LinkedList;
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
	int dum;
	
	Random random = new Random();
	int numOfSpots = 8;
	public static boolean scenarioSwitchFromRandom = false;
	public static boolean scenarioSwitchToRandom = false;

	// system variables
	boolean gateEntrance = false;
	boolean gateExit;
	boolean gateVipEntrance = false;
	boolean gateVipExit;
	int freeSpot;
	boolean[] spotLight  = new boolean[9];
	boolean[] mainSpot = new boolean[9];
	static boolean parkingLotMaintainance = false; 
	boolean pedetrianRightLight;
	boolean pedetrianLeftLight;
	

	// environment variables
	boolean[] carInSpot;
	static boolean enableMain = false;
	static boolean pedUpRight = false; 
	static boolean pedDownRight = false;
	static boolean pedUpLeft = false;
	static boolean pedDownLeft = false;

		
	// auxiliary variables
	boolean backToRandom = false;
	boolean carInQWasFound = false;
	boolean carVipInQWasFound = false;
	boolean carVipInExitWasFound = false;
	boolean carInExitWasFound = false;
	static volatile boolean randomDone = false;
	static int scenarioResetCnt = 0;
	static Pedestrian pedestrianUpRight;
	static Pedestrian pedestrianUpLeft;
	static Pedestrian pedestrianDownRight;
	static Pedestrian pedestrianDownLeft;
	
	
	// simulator data bases
	static LinkedList<Car> carList = new LinkedList<Car>();
	
	
	// scenario list
	static LinkedList<ScenarioStep> scenarioSteps = new LinkedList<ScenarioStep>();
	
	// scenario cars list
		LinkedList<Car> scenarioList = new LinkedList<Car>();
	
	Thread thread;
	
	// Images
	BufferedImage parkingBackground;
	BufferedImage parkingBackgroundFire;

	BufferedImage carImage;
	BufferedImage carImageup;
	BufferedImage carImageDown;

	BufferedImage carVipImage;
	BufferedImage carVipImageup;
	BufferedImage carVipImageDown;
	
	BufferedImage carOnFire;
	
	BufferedImage regGate;
	BufferedImage regGateClosed;
	BufferedImage vipGate;
	BufferedImage vipGateClosed;
	
	BufferedImage [] freeSpots = new BufferedImage[8];
	
	BufferedImage greenCrossWalk;
	BufferedImage spotMaintain;
	
	BufferedImage greenSpotLight;
	BufferedImage redSpotLight;
	
	// constructor of Parking Simulator
	// deals with executor
	public ParkingSimulator() {
		Thread animationThread = new Thread(new Runnable() {
			public void run() {
				
				// Instantiate a new controller executor
				while(true)
				{
					// every time we switch from random scenario to real scenario or vise versa
					// we reset the executor
					executor = new ControllerExecutor(true, false);
					while (true) {	
						
						// finished scenario
						if(scenarioSteps.size() == 0 && backToRandom)
						{
							scenarioResetCnt++;
							if(scenarioResetCnt == 4)
							{
								randomDone = false;
								scenarioResetCnt = 0;
								scenarioSwitchToRandom = true;
								backToRandom = false;
								scenarioList.clear();
							}
						}
						
						// if we are not in scenario mode
						if(scenarioSteps.size() == 0 && !backToRandom)
						{
							GUI.enableCommands();
						}
						
						// if we need to switch to random
						if(scenarioSwitchToRandom)
						{
							Auxiliary.resetBoard();
							scenarioSwitchToRandom = false;
							GUI.enableButtons();
							break;
						}
						
						// if we need to switch to a scenario
						if(scenarioSwitchFromRandom)
						{
							scenarioResetCnt = 0;
							Auxiliary.resetBoard();

							while(!randomDone) { dum = 1; };	
							scenarioSwitchFromRandom = false;
							break;
						}
						
						// implement scenario
						if(scenarioSteps.size() > 0)
						{
							GUI.disableButtons();
							Car car = null;
							ScenarioStep step = scenarioSteps.get(0);
							switch (step)
							{
							case ENABLE_MAINT:
								enableMain = true;
								scenarioSteps.remove(0);
								break;
							case ADD_CAR:
								try {
									car = API.addCarEnterance(); 
									if(car == null)
									{
										break;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								repaint();
								scenarioList.add(car);
								scenarioSteps.remove(0);
								break;
							case ADD_VIP_CAR:
								try {
									car = API.addVipCarEnterance(); 
									if(car == null)
									{
										break;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								repaint();
								scenarioList.add(car);
								scenarioSteps.remove(0);
								break;
							case ADD_PED:
								int randPed = random.nextInt(4);
								try {
									API.addPedestrian(randPed);
								} catch (Exception e) {
									e.printStackTrace();
								}
								scenarioSteps.remove(0);
								break;
							case REMOVE_CAR:
								repaint();
								Car carR = null;
								// we are the only ones to use this
								// remove first car
								for (Car carToRemove: scenarioList)
								{
									if(!carToRemove.isVipCar())
									{
										if(!carToRemove.isRemoved())
										{
											carR = carToRemove;
											break;
										}
									}
								}
								if(carR != null)
								{
									if(carR.getState() == CarStates.PARKED)
									{
										API.removeCarFromParkingLot(carR.getId());
										scenarioList.remove(carR);
										scenarioSteps.remove(0);
									}
								}
								
								break;
								
							case REMOVE_VIP_CAR:
								repaint();
								// we are the only ones to use this
								// remove first car
								for (Car carToRemove: scenarioList)
								{
									if(carToRemove.isVipCar())
									{
										if(!carToRemove.isRemoved())
										{
											car = carToRemove;
											break;
										}
									}
								}
								if(car.getState() == CarStates.PARKED)
								{
									API.removeCarFromParkingLot(car.getId());
									scenarioList.remove(car);
									scenarioSteps.remove(0);
								}
								break;
							}
							if(scenarioSteps.size() == 0)
							{
								backToRandom = true;
							}
						}

						
						try {
							Auxiliary.executeAddCars();
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						Auxiliary.executeAddPeds();
						
						
						// dealing with executor and spectra variables
						Map<String, String> sysValues;
						carInQWasFound = false;
						carInExitWasFound  = false;
						carVipInQWasFound = false;
						carVipInExitWasFound = false;
						try {
							executor.setInputValue("enableMain", enableMain ? "true" : "false");
							if(parkingLotMaintainance)
							{
								executor.setInputValue("carEntrance", "false");
								executor.setInputValue("carExit", "false");
								executor.setInputValue("carVipExit", "false");
								executor.setInputValue("carVipEntrance", "false");
								for (int i = 0; i < numOfSpots;i++) 
								{
									carInSpot[i] = false;
								}
								Auxiliary.resetBoard();
							}
							else
							{
								carInSpot = new boolean [] {false,false,false,false,false,false,false,false};
								for(Car car : carList) {
									
									if(car.getState() == CarStates.IN_QUEUE_FIRST && !gateEntrance) {
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
										
						// Update executor state
						try {
							executor.updateState(true);
						} catch (ControllerExecutorException e) {
							e.printStackTrace();
						}
						
						// receive system variables
						sysValues = executor.getCurOutputs();
						
						gateEntrance = sysValues.get("gateEntrance").equals("true") ? true : false;
						gateExit = sysValues.get("gateExit").equals("true") ? true : false;
						freeSpot  = Integer.parseInt(sysValues.get("freeSpot"));
						pedetrianLeftLight = sysValues.get("pedetrianLeftLight").equals("true") ? true : false;
						pedetrianRightLight = sysValues.get("pedetrianRightLight").equals("true") ? true : false;
						gateVipEntrance = sysValues.get("gateVipEntrance").equals("true") ? true : false;
						gateVipExit = sysValues.get("gateVipExit").equals("true") ? true : false;
						parkingLotMaintainance = sysValues.get("parkingLotMaintenance").equals("true") ? true : false;
						for (int i =0; i<numOfSpots;i++) 
						{
							mainSpot[i] = sysValues.get("spotMaintenance" +"["+i+"]").equals("true") ? true : false;
							spotLight[i] = sysValues.get("spotLight" +"["+i+"]").equals("true") ? true : false;
 
						}
						
						// paint parking lot
						try {
							paintParkingLot();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						Auxiliary.executeRemoveCars();
						Auxiliary.executeRemovePeds();
						Auxiliary.deleteOutCars();
					}
				}
			}
			});
		
		// Load images
		try {
			parkingBackground  = ImageIO.read(new File("img/backgroundImage.jpg"));
			parkingBackgroundFire  = ImageIO.read(new File("img/backgroundImageFire.png"));
			
			regGate = ImageIO.read(new File("img/normal_gate.png"));
			regGateClosed =  ImageIO.read(new File("img/real_gate.png"));
			vipGate =  ImageIO.read(new File("img/vip_gate.png"));
			vipGateClosed = ImageIO.read(new File("img/vip_gate_rreal.png"));
			
			carImage = ImageIO.read(new File("img/car.png"));
			carVipImage = ImageIO.read(new File("img/carVip.png")); 
			greenCrossWalk = ImageIO.read(new File("img/greenCrossWalk.jpeg"));
			
			carImageup = ImageIO.read(new File("img/carUp.png"));
			carVipImageup = ImageIO.read(new File("img/carVipUp.png"));
			carImageDown = ImageIO.read(new File("img/carDown.png"));
			carVipImageDown = ImageIO.read(new File("img/carVipDown.png"));
			carOnFire = ImageIO.read(new File("img/carOnFire.png"));
			spotMaintain = ImageIO.read(new File("img/maint.png"));
			
			greenSpotLight = ImageIO.read(new File("img/green.png"));
			redSpotLight = ImageIO.read(new File("img/red.png"));
			
			for(int i = 0; i < 8; i++)
			{
				freeSpots[i] = ImageIO.read(new File("img/spot"+i+".png"));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		animationThread.start();
		repaint();

	}
	
	
	private void paintParkingLot() throws Exception
	{
		try {
			Thread.sleep(50);
			repaint();

			// paint relevant state by phase of car
			if(parkingLotMaintainance) {
				for(Car car : carList) {
					car.setImg(carOnFire);
				}
				repaint();
				Thread.sleep(1000);
				carList.clear();
			}
			// for every car in list
			for(Car car : carList) {
				
				switch (car.getState())
				{
				
				case INIT :
					int stepsToMove;
					if(Car.carsInQ==1) {
						stepsToMove = 125;
						car.updateState(CarStates.IN_QUEUE_FIRST);
					}
					else {
						stepsToMove = 20;
						if(!car.isVipCar())
							car.updateState(CarStates.IN_QUEUE_SECOND);
						else
						{
							stepsToMove = 125;
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
					break;
				
				
				case IN_QUEUE_FIRST:
				
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
						car.updateState(CarStates.ENTER_PARKING_LOT, freeSpot);
					}		
				
					break;
					
				case IN_QUEUE_SECOND:
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
						for (int i = 0; i < 105; i++) {
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
						{
							car.setImg(carImageDown);
						}
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
					int downExit = ParkingSpotsConfig.configure.get(car.getParkingSpot())[2];
					int straightExit = 400 - ParkingSpotsConfig.configure.get(car.getParkingSpot())[1];
					int upExit = ParkingSpotsConfig.configure.get(car.getParkingSpot())[0];
					
					if(car.isVipCar())
					{
						downExit = ParkingSpotsConfig.configureVip.get(car.getParkingSpot())[0];
						straightExit = 400 - ParkingSpotsConfig.configureVip.get(car.getParkingSpot())[1];
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
						else
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
						for (int i = 0; i < 500; i++) {
							repaint();
							Thread.sleep(10);
							if(car.isVipCar())
								car.setX(car.getX()+1);
							else
								car.setX(car.getX()-1);
						}
						Auxiliary.carsOut.add(car);
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
	
	// handles pedestrian's graphics
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
				for (int i = 0; i < 300; i++) {
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
		
		if(parkingLotMaintainance) 
			g.drawImage(parkingBackgroundFire,0,0,null);
		else
			g.drawImage(parkingBackground,0,0,null);
		
		if(pedetrianRightLight)
		{
			g.drawImage( greenCrossWalk, 785, 160, 38, 152, null);
		}
		
		if(pedetrianLeftLight)
		{
			g.drawImage( greenCrossWalk, 260, 160, 38, 152, null);
		}
		
		g.setColor(Color.CYAN);
		
		for(Car car: carList) {
			g.drawImage(car.getImg(),car.getX(),car.getY(), car.getWidth(), car.getHeight(), null);
		}
		
		if(!gateEntrance)
		{
			g.drawImage(regGateClosed, 836, 159, 8, 55, null);
			g.drawImage(regGate, 835, 210, 10, 10, null);
		}
		else
		{
			g.drawImage(regGate, 835, 210, 10, 10, null);
			g.drawImage(freeSpots[freeSpot], 720, 117, 30, 30, null);
			
		}
		
		if(!gateExit)
		{
			g.drawImage(regGateClosed, 301, 159, 8, 55, null);
			g.drawImage(regGate, 300, 210, 10, 10, null);
		}
		else
		{
			g.drawImage(regGate, 300, 210, 10, 10, null);
		}
		
		if(!gateVipEntrance)
		{
			g.drawImage(vipGateClosed, 241, 255, 8, 55, null);
			g.drawImage(vipGate, 240, 247, 10, 10, null);
		}
		else
		{
			g.drawImage(vipGate, 240, 247, 10, 10, null);
			g.drawImage(freeSpots[freeSpot], 320, 312, 30, 30, null);
		}
		
		if(!gateVipExit)
		{
			g.drawImage(vipGateClosed, 761, 255, 8, 55, null);
			g.drawImage(vipGate, 760, 245, 10, 10, null);
		}
		else
		{
			g.drawImage(vipGate, 760, 245, 10, 10, null);
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
		
		for (int i = 0; i < 8; i++) 
		{
			if(mainSpot[i]) 
			{
				g.drawImage(spotMaintain, ParkingSpotsConfig.parkingLotConf.get(i)[0],
							ParkingSpotsConfig.parkingLotConf.get(i)[1], 80, 80, null);
			}
			
			if(!spotLight[i])
			{
				g.drawImage(greenSpotLight, ParkingSpotsConfig.parkingLotSpotLight.get(i)[0],ParkingSpotsConfig.parkingLotSpotLight.get(i)[1], 55, 35, null);
			}
			else
			{
				g.drawImage(redSpotLight, ParkingSpotsConfig.parkingLotSpotLight.get(i)[0],ParkingSpotsConfig.parkingLotSpotLight.get(i)[1], 55, 35, null);
			}
		}
	}
}
