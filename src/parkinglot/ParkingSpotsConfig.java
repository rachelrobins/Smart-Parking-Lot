package parkinglot;

import java.util.HashMap;


public class ParkingSpotsConfig {
	
	static HashMap<Integer,Integer[]> configure = new HashMap<Integer,Integer[]>();

	
	public static void initMap() {
		configure.put(0,new Integer[] {0,0,120});
		configure.put(1,new Integer[] {0,30,120});
		configure.put(2,new Integer[] {0,60,120});
		configure.put(3,new Integer[] {0,90,120});
		configure.put(4,new Integer[] {120,0,0});
		configure.put(5,new Integer[] {120,30,0});
		configure.put(6,new Integer[] {120,60,0});
		configure.put(7,new Integer[] {120,90,0});
		
	}
	
	

}
