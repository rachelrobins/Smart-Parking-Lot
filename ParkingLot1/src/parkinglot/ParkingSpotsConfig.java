package parkinglot;

import java.util.HashMap;


public class ParkingSpotsConfig {
	
	static HashMap<Integer,Integer[]> configure = new HashMap<Integer,Integer[]>();

	
	public static void initMap() {
		configure.put(0,new Integer[] {0,0,120});
		configure.put(1,new Integer[] {0,75,120});
		configure.put(2,new Integer[] {0,150,120});
		configure.put(3,new Integer[] {0,225,120});
		configure.put(4,new Integer[] {160,0,0});
		configure.put(5,new Integer[] {160,75,0});
		configure.put(6,new Integer[] {160,150,0});
		configure.put(7,new Integer[] {160,225,0});
		
	}
	
	

}
