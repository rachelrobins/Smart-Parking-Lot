package parkinglot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GUI {
	
	static JFrame f = new JFrame("ParkingLot Simulator");
	static 	JButton startBtnArr[] = new JButton[6];
	static JButton addCarBtn, addVipCarBtn, addPedBtn, removeCarBtn, removeVipCarBtn;
	
	public static void createGUI()
	{
		int commandButtonX = 620;
		f.setVisible(true);
		addCarBtn=new JButton("Add Car");
	    addCarBtn.setBounds(commandButtonX,520,120,30);  
	    addCarBtn.addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try {
	    			addCarBtn.setEnabled(false);
					API.addCarEnterance();
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(addCarBtn);
	    
	    addVipCarBtn=new JButton("Add Vip Car");
	    addVipCarBtn.setBounds(commandButtonX + 130,520,120,30);  
	    addVipCarBtn.addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try {
	    			addVipCarBtn.setEnabled(false);

					API.addVipCarEnterance();
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(addVipCarBtn);
	    
	    addPedBtn=new JButton("Add Pedestrian");
	    addPedBtn.setBounds(commandButtonX + 260,520,150,30);  
	    addPedBtn.addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try {
	    			addPedBtn.setEnabled(false);
					API.addPedestrian(0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(addPedBtn);

	    removeCarBtn=new JButton("Remove Car");
	    removeCarBtn.setBounds(commandButtonX + 40,560,150,30);  
	    removeCarBtn.addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try {
	    			for(Car car : ParkingSimulator.carList)
	    			{
	    				if(car.isVipCar())
	    				{
	    					continue;
	    				}
	    				if(car.getState() == CarStates.PARKED)
	    				{
	    					removeCarBtn.setEnabled(false);
	    					API.removeCarFromParkingLot(car.getId());
	    				}
	    				break;
	    			}
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(removeCarBtn);
	    
	    removeVipCarBtn=new JButton("Remove Vip Car");
	    removeVipCarBtn.setBounds(commandButtonX + 200,560,150,30);  
	    removeVipCarBtn.addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try 
	    		{
	    			for(Car car : ParkingSimulator.carList)
	    			{
	    				if(!car.isVipCar())
	    				{
	    					continue;
	    				}
	    				if(car.getState() == CarStates.PARKED)
	    				{
	    					removeVipCarBtn.setEnabled(false);
	    					API.removeCarFromParkingLot(car.getId());
	    				}
	    				break;
	    			}				
	    		} 
	    		catch (Exception e1) 
	    		{
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(removeVipCarBtn);

	    int firstLineScenarioY = 510;
	    int secLineScenarioY = 570;
	    
	    startBtnArr[0]=new JButton("0");
	    startBtnArr[0].setBounds(195,firstLineScenarioY,50,50);  
	    startBtnArr[0].addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try {
	    				disableButtons();
	    				ParkingSimulator.scenarioSwitchFromRandom = true;
						Scenarios.createZeroScenario();
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(startBtnArr[0]);
	    
	    
	    startBtnArr[1]=new JButton("1");
	    startBtnArr[1].setBounds(255,firstLineScenarioY,50,50);  
	    startBtnArr[1].addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try {
    					disableButtons();
    					ParkingSimulator.scenarioSwitchFromRandom = true;			
						Scenarios.createFirstScenario();
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(startBtnArr[1]);
	    
	    
	    
	    startBtnArr[2]=new JButton("2");
	    startBtnArr[2].setBounds(315,firstLineScenarioY,50,50);  
	    startBtnArr[2].addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try {
						disableButtons();
						ParkingSimulator.scenarioSwitchFromRandom = true;
						Scenarios.createSecondScenario();
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(startBtnArr[2]);
	    
	    startBtnArr[3]=new JButton("3");
	    startBtnArr[3].setBounds(195,secLineScenarioY,50,50);  
	    startBtnArr[3].addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try {
						disableButtons();
						ParkingSimulator.scenarioSwitchFromRandom = true;
						Scenarios.createThirdScenario();
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(startBtnArr[3]);
	    
	    startBtnArr[4]=new JButton("4");
	    startBtnArr[4].setBounds(255,secLineScenarioY,50,50);  
	    startBtnArr[4].addActionListener(new ActionListener(){  
	    		public void actionPerformed(ActionEvent e){  
	    		try {
						disableButtons();
						ParkingSimulator.scenarioSwitchFromRandom = true;
						Scenarios.createForthScenario();
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
	        }  
	    });  
	    f.add(startBtnArr[4]);  
	    
	    f.setLayout(null);  
	    f.setVisible(true);
	    

	}
	
	// enable all buttons
	public static void enableButtons()
	{
		for(int i = 0; i < 5; i++)
		{
			startBtnArr[i].setEnabled(true);
		}
		addCarBtn.setEnabled(true);
		addVipCarBtn.setEnabled(true);
		addPedBtn.setEnabled(true);
		removeCarBtn.setEnabled(true);
		removeVipCarBtn.setEnabled(true);
	}
	
	// disable all buttons
	public static void disableButtons()
	{
		for(int i = 0; i < 5; i++)
		{
			startBtnArr[i].setEnabled(false);
		}
		addCarBtn.setEnabled(false);
		addVipCarBtn.setEnabled(false);
		addPedBtn.setEnabled(false);
		removeCarBtn.setEnabled(false);
		removeVipCarBtn.setEnabled(false);
	}
	
	// enable command buttons
	public static void enableCommands()
	{
		removeCarBtn.setEnabled(true);
		removeVipCarBtn.setEnabled(true);	
		addPedBtn.setEnabled(true);
		addCarBtn.setEnabled(true);
		addVipCarBtn.setEnabled(true);
	}	
}
