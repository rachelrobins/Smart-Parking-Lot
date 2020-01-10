package parkinglot;

import java.util.HashMap;


public class ParkingSpotsConfig {
	
	static HashMap<Integer,Integer[]> configure = new HashMap<Integer,Integer[]>();
	static HashMap<Integer,Integer[]> configureVip = new HashMap<Integer,Integer[]>();

	
	public static void initMap() {
		configure.put(0,new Integer[] {0,70,120});
		configure.put(1,new Integer[] {0,150,120});
		configure.put(2,new Integer[] {0,230,120});
		configure.put(3,new Integer[] {0,310,120});
		configure.put(4,new Integer[] {160,70,0});
		configure.put(5,new Integer[] {160,150,0});
		configure.put(6,new Integer[] {160,230,0});
		configure.put(7,new Integer[] {160,310,0});
		
		configureVip.put(0,new Integer[] {0,395,220});
		configureVip.put(1,new Integer[] {0,310,220});
		configureVip.put(2,new Integer[] {0,220,220});
		configureVip.put(3,new Integer[] {0,145,220});
		configureVip.put(4,new Integer[] {60,395,0});
		configureVip.put(5,new Integer[] {60,310,0});
		configureVip.put(6,new Integer[] {60,220,0});
		configureVip.put(7,new Integer[] {60,145,0});
		
		
	}
	
	

}
