package parkinglot;

import java.util.HashMap;


public class ParkingSpotsConfig {
	
	static HashMap<Integer,Integer[]> configure = new HashMap<Integer,Integer[]>();
	static HashMap<Integer,Integer[]> configureVip = new HashMap<Integer,Integer[]>();

	
	public static void initMap() {
		configure.put(0,new Integer[] {0,0,120});
		configure.put(1,new Integer[] {0,85,120});
		configure.put(2,new Integer[] {0,160,120});
		configure.put(3,new Integer[] {0,235,120});
		configure.put(4,new Integer[] {160,0,0});
		configure.put(5,new Integer[] {160,85,0});
		configure.put(6,new Integer[] {160,160,0});
		configure.put(7,new Integer[] {160,235,0});
		
		configureVip.put(0,new Integer[] {0,300,220});
		configureVip.put(1,new Integer[] {0,225,220});
		configureVip.put(2,new Integer[] {0,150,220});
		configureVip.put(3,new Integer[] {0,75,220});
		configureVip.put(4,new Integer[] {60,300,0});
		configureVip.put(5,new Integer[] {60,225,0});
		configureVip.put(6,new Integer[] {60,150,0});
		configureVip.put(7,new Integer[] {60,75,0});
		
		
	}
	
	

}
