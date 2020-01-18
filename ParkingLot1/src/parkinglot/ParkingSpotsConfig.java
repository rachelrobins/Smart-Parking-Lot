package parkinglot;

import java.util.HashMap;

//represents maps to configure car movement when entering a spot

public class ParkingSpotsConfig {
	
	static HashMap<Integer,Integer[]> parkingLotSpotLight = new HashMap<Integer,Integer[]>();
	static HashMap<Integer,Integer[]> parkingLotConf = new HashMap<Integer,Integer[]>();
	static HashMap<Integer,Integer[]> configure = new HashMap<Integer,Integer[]>();
	static HashMap<Integer,Integer[]> configureVip = new HashMap<Integer,Integer[]>();

	// this map defines for each parking spot how to enter it 
	public static void initMap() {
		// for each spot in map, first coefficient is how many steps car must go left, second 
		// coefficient is how many steps car must go straight, and third is how many steps car must go right
				
		// if car is regular
		configure.put(0,new Integer[] {0,70,120});
		configure.put(1,new Integer[] {0,150,120});
		configure.put(2,new Integer[] {0,230,120});
		configure.put(3,new Integer[] {0,310,120});
		configure.put(4,new Integer[] {160,70,0});
		configure.put(5,new Integer[] {160,150,0});
		configure.put(6,new Integer[] {160,230,0});
		configure.put(7,new Integer[] {160,310,0});

		//if car is VIP
		configureVip.put(0,new Integer[] {0,395,220});
		configureVip.put(1,new Integer[] {0,310,220});
		configureVip.put(2,new Integer[] {0,220,220});
		configureVip.put(3,new Integer[] {0,145,220});
		configureVip.put(4,new Integer[] {60,395,0});
		configureVip.put(5,new Integer[] {60,310,0});
		configureVip.put(6,new Integer[] {60,220,0});
		configureVip.put(7,new Integer[] {60,145,0});
		
		// spot maint axis
		parkingLotConf.put(0,new Integer[] {630,50});
		parkingLotConf.put(1,new Integer[] {550,50});
		parkingLotConf.put(2,new Integer[] {470,50});
		parkingLotConf.put(3,new Integer[] {380,50});
		parkingLotConf.put(4,new Integer[] {630,350});
		parkingLotConf.put(5,new Integer[] {550,350});
		parkingLotConf.put(6,new Integer[] {470,350});
		parkingLotConf.put(7,new Integer[] {380,350});
		
		// spot light axis
		parkingLotSpotLight.put(0,new Integer[] {640,5});
		parkingLotSpotLight.put(1,new Integer[] {550,5});
		parkingLotSpotLight.put(2,new Integer[] {470,5});
		parkingLotSpotLight.put(3,new Integer[] {390,5});
		parkingLotSpotLight.put(4,new Integer[] {640,410});
		parkingLotSpotLight.put(5,new Integer[] {550,410});
		parkingLotSpotLight.put(6,new Integer[] {470,410});
		parkingLotSpotLight.put(7,new Integer[] {390,410});
	}
}
