//package test;

import java.io.*;
import npe.sim.road.*;
import npe.sim.results.*;
import npe.sim.*;
import java.awt.MediaTracker;
import java.io.FileNotFoundException;
import javax.swing.JPanel;
import npe.sim.entity.*;
import npe.sim.entity.Vehicle.Intention;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Scanner;

public class test {

	static boolean run = false;
	static Intersection i;

	public class SomeRunnable implements Runnable {
		private SimWindow sw;
		private StatsCollector sc;
		private SimProperties sp;
		private Intersection i;

		private Road northTce;
		private VehicleLane taxiLane;
		public PedestrianLane pLaneNthWest;
		public PedestrianLane pLaneNthEast;
		public ArrayList<Boolean> found = new ArrayList<Boolean>(50);
		public ArrayList<Pedestrian> loopList = new ArrayList<Pedestrian>(50);
		public ArrayList<Integer> amountPassengers = new ArrayList<Integer>(50);
		public ArrayList<Integer> amountTaxis = new ArrayList<Integer>(50);
		public int amountTests=-1;
		public int taxiCount=0;
		public int iter=0;
		public int ticks=-1;
		public int total_ticks;
		public int iterTests=0;
		public boolean stop=false;
		
		public SomeRunnable(SimWindow sw, StatsCollector sc, SimProperties sp, Intersection i, int the_amountTests, ArrayList<Integer> the_amountPassengers, ArrayList<Integer> the_amountTaxis){
			this.sw = sw;
			this.sc = sc;
			this.sp = sp;
			this.i = i;
			this.northTce = i.northTce;
			this.taxiLane = northTce.getVehicleLanes().get(1);
			this.pLaneNthWest = northTce.getPedestrianLanes().get(0);
			this.pLaneNthEast = northTce.getPedestrianLanes().get(1);
			
			this.amountTests=the_amountTests;
			this.amountPassengers=the_amountPassengers;
			this.amountTaxis=the_amountTaxis;
			/*amountTests = 3;
			amountPassengers.add(4);
			amountPassengers.add(4);
			amountPassengers.add(7);
			amountTaxis.add(4);
			amountTaxis.add(4);
			amountTaxis.add(7);*/
		}

      		public void run(){
		stop = false;
			/**The current animation speed in Frames Per Second.*/
		long fps = 0;
		
		//---START MAIN LOOP---//
		long prevTime = sw.pSim.time(); //Time of previous frame
		long framesSinceFpsCheck = 0; //Number of frames since last frame count
		long timeOfFpsCheck = sw.pSim.time(); //Time of last frame count
		//System.out.print(sw.pSim.running);
		while (sw.pSim.running) {
			/*//CHECK FOR DURATION REACHED
			if (sw.pSim.duration>0 && sc.t()>sw.pSim.duration) {
				//stop test
				System.out.print("Test stopped by duration elapse");
			}*/
			//SIMULATION LOOP
			if (!sw.pSim.paused) {
				if ( i.cTick == 5){
					///////////////////
					// Boundary Cases//
					///////////////////
					double topArea = i.rankY + TaxiRank.HEIGHT + PedestrianLane.LANE_WIDTH - (PedestrianLane.LANE_WIDTH/4+10);
					double bottomArea = topArea + (PedestrianLane.LANE_WIDTH/4+10)-1;
					//From West
					Pedestrian p1 = pLaneNthWest.addPedestrianTest(1220, topArea, 0, Pedestrian.MAX_SPEED);
					Pedestrian p2 = pLaneNthWest.addPedestrianTest(1200, bottomArea, 0, Pedestrian.MIN_SPEED);
					//From East
					Pedestrian p3 = pLaneNthEast.addPedestrianTest(1550, topArea, 180, Pedestrian.MIN_SPEED);
					Pedestrian p4 = pLaneNthEast.addPedestrianTest(1570, bottomArea, 180, Pedestrian.MAX_SPEED);
					
					//i.testingtaxi(200);
					//i.testingtaxi(500);
					//i.testingtaxi(-400);
					
					loopList.add(p1);
					loopList.add(p2);
					loopList.add(p3);
					loopList.add(p4);

					
					for (int x = 0; x<loopList.size(); x++){
						found.add(false);
					}
					/*if (pLaneEast.passengerQueue.size() == 4){
						sc.results.add(true);
					}*/


					//i.testingtaxi(0);

					//sc.results.add(true);
					//System.out.print(northTce.ROAD_LENGTH*3/5);

				} else if ( i.cTick == 280 ) {
					///////////////////////
					// Equivalence Cases //
					///////////////////////
					
					//reset sim
					int aSize = loopList.size();
					for (int x =0; x<aSize; x++) {
						pLaneNthEast.removePedestrian(loopList.get(0));
					}
					sw.stopPressed();
					sw.pBot.reset();
					loopList.clear();
					found.clear();
					sw.startPressed();
					sw.pBot.bStop.setEnabled(true);

					//find points
					double topArea = i.rankY + TaxiRank.HEIGHT + PedestrianLane.LANE_WIDTH - (PedestrianLane.LANE_WIDTH/4+10);
					double bottomArea = topArea + (PedestrianLane.LANE_WIDTH/4+10)-1;
					double center = (bottomArea-topArea)/2+topArea;

					//From West - Max speed
					Pedestrian p1 = pLaneNthWest.addPedestrianTest(1000, center, 0, Pedestrian.MAX_SPEED);
					Pedestrian p2 = pLaneNthWest.addPedestrianTest(1000, center, 0, Pedestrian.MAX_SPEED);
					//From West - Min speed
					Pedestrian p3 = pLaneNthWest.addPedestrianTest(1150, center, 0, Pedestrian.MIN_SPEED);
					Pedestrian p4 = pLaneNthWest.addPedestrianTest(1150, center, 0, Pedestrian.MIN_SPEED);
					//From East - Min speed
					Pedestrian p5 = pLaneNthEast.addPedestrianTest(1600, center, 180, Pedestrian.MIN_SPEED);
					Pedestrian p6 = pLaneNthEast.addPedestrianTest(1600, center, 180, Pedestrian.MIN_SPEED);
					//From East - Max speed
					Pedestrian p7 = pLaneNthEast.addPedestrianTest(1500, center, 180, Pedestrian.MAX_SPEED);
					Pedestrian p8 = pLaneNthEast.addPedestrianTest(1500, center, 180, Pedestrian.MAX_SPEED);

					loopList.add(p1);
					loopList.add(p2);
					loopList.add(p3);
					loopList.add(p4);
					loopList.add(p5);
					loopList.add(p6);
					loopList.add(p7);
					loopList.add(p8);

					for (int x = 0; x<loopList.size(); x++){
						found.add(false);
					}
					//System.out.println(found.size());

				} else if ( i.cTick == 651 ) {
					//////////////////////////////////
					// Taxi Take Passengers Testing //
					//////////////////////////////////
					++i.cTick;

					//reset sim
					int aSize = loopList.size();
					for (int x =0; x<aSize; x++) {
						pLaneNthEast.removePedestrian(loopList.get(0));
					}

					sw.stopPressed();
					sw.pBot.reset();
					//loopList.clear();
					//found.clear();
					sw.startPressed();
					sw.pBot.bStop.setEnabled(true);
					pLaneNthEast.passengerQueue.clear();

					//find points
					double topArea = i.rankY + TaxiRank.HEIGHT + PedestrianLane.LANE_WIDTH - (PedestrianLane.LANE_WIDTH/4+10);
					double bottomArea = topArea + (PedestrianLane.LANE_WIDTH/4+10)-1;
					double center = (bottomArea-topArea)/2+topArea;

					


					//for (int k =0; k<amountTests; k++){
						for (int x =0; x<amountPassengers.get(0); x++){
							double randy = Utils.random()*11+1;
							if ( randy > 5 ) {
								Pedestrian p1 = pLaneNthWest.addPedestrianTest(1300-x*15, center, 0, Pedestrian.MAX_SPEED);
							} else {
								Pedestrian p1 = pLaneNthEast.addPedestrianTest(1450+x*15, center, 180, Pedestrian.MAX_SPEED);
							}
							//i.testingtaxi(-100-x*50);
						}
					//}
					taxiCount = amountTaxis.get(0);
					ticks = 0;

					/*for (int x =0; x<amountTests; x++){
						total_ticks=total_ticks+4*amountTaxis.get(x);
					}
					total_ticks=total_ticks+120*taxiCount+i.cTick;*/

					ticks = ticks+40*amountTaxis.get(0)+i.cTick+amountTaxis.get(0)*160+100;
					//System.out.println(ticks);
					System.out.println("Current test ends at " + ticks + " ticks. Test case: Test No. " + (iterTests+1) + " of " + amountTests);
				}		

				if ( iter != taxiCount && i.cTick % 10 == 0 && !stop){
					i.testingtaxi(taxiCount-14-taxiCount*Taxi.WIDTH);
					iter++;
					//4 ticks per taxi
				} else if (iter == taxiCount && i.cTick == ticks && iterTests != amountTests && !stop) {
					//find points
					double topArea = i.rankY + TaxiRank.HEIGHT + PedestrianLane.LANE_WIDTH - (PedestrianLane.LANE_WIDTH/4+10);
					double bottomArea = topArea + (PedestrianLane.LANE_WIDTH/4+10)-1;
					double center = (bottomArea-topArea)/2+topArea;
					iter = 0;
					if ( pLaneNthEast.passengerQueue.size() == Math.max(0, amountPassengers.get(iterTests) - amountTaxis.get(iterTests))){
						sc.results.add(true);
					} else {
						sc.results.add(false);
					}
					int aSize = amountPassengers.get(iterTests);
					for (int x =0; x<aSize; x++) {
						pLaneNthEast.removePedestrian(loopList.get(0));
					}
					sw.stopPressed();
					sw.pBot.reset();
					sw.startPressed();
					sw.pBot.bStop.setEnabled(true);
					iterTests++;
					for (int x =0; x<amountPassengers.get(iterTests); x++){
							double randy = Utils.random()*11+1;
							if ( randy > 5 ) {
								Pedestrian p1 = pLaneNthWest.addPedestrianTest(1300-x*15, center, 0, Pedestrian.MAX_SPEED);
							} else {
								Pedestrian p1 = pLaneNthEast.addPedestrianTest(1450+x*15, center, 180, Pedestrian.MAX_SPEED);
							}
					}
					taxiCount = amountTaxis.get(iterTests);
					ticks = ticks+40*amountTaxis.get(iterTests)+160*amountTaxis.get(iterTests)+100;
					System.out.println("Current test ends at " + ticks + " ticks. Test case: Test No. " + (iterTests+1) + " of " + amountTests);
					pLaneNthEast.passengerQueue.clear();
				} else if (iterTests == amountTests && i.cTick == ticks && !stop) {
					if ( pLaneNthEast.passengerQueue.size() ==  Math.max(0, amountPassengers.get(iterTests) - amountTaxis.get(iterTests))){
						sc.results.add(true);
					} else {
						sc.results.add(false);
					}
					int aSize = amountPassengers.get(iterTests);
					for (int x =0; x<aSize; x++) {
						pLaneNthEast.removePedestrian(loopList.get(0));
					}
					sw.stopPressed();
					
					sw.pBot.reset();
					run = false;
					i.cTick = 0;
					i.northTce.getPedestrianLanes().clear();
					i.northTce.getVehicleLanes().clear();
					for (int x=0; x<sc.results.size(); x++){
						System.out.println("Test " + (x+1) + ": " + sc.results.get(x));
					}
					sc.results.clear();
					stop = true;
					System.out.println("Testing Complete");
					System.out.println("Restart using stop and start to run same test inputs again");
					System.out.println("----->NOTE: Hard-coded Tests No.10 and No.11 are intended to fail.");
				}

				
				for (int x=0; x<loopList.size(); x++) {
					if (loopList.get(x) != null && found.get(x) == false) {
						if (pLaneNthEast.passengerQueue.contains(loopList.get(x))){
							sc.results.add(true);
							found.set(x, true);
							//System.out.println("Test " + x + ": Passed!");
						} else if ( i.cTick == 279 ) {
							sc.results.add(false);
							//System.out.println("Test " + x + ": Failed!");
						} else if ( i.cTick == 650 ) {
							sc.results.add(false);
						}
					}
				}
				//System.out.print(" " + i.cTick);
				//System.out.print(" " + sc.getCurrentTick());
				System.out.print("");
				//System.out.print(pLaneNthEast.pedList().size());
				//pLaneNthEast.addPedestrianTest(900, 168, 0);
			}
			//CALCULATE TICK RATE
			if (sw.pSim.time() > timeOfFpsCheck+1000) { //If one second has elapsed
				fps = framesSinceFpsCheck;
				framesSinceFpsCheck = 0;
				timeOfFpsCheck = sw.pSim.time();
			} else {
				framesSinceFpsCheck++;
			}
			//GO TO NEXT FRAME
			long timeElapsed = sw.pSim.time() - prevTime;
			int frameTime = (int) (1000 / (sw.pSim.TPS*sw.pSim.speed));
			if (sw.pSim.paused) { //Run loop at constand speed if paused
				frameTime = 1000 / sw.pSim.TPS;
			}
			if (frameTime > timeElapsed) {
				try {
					Thread.sleep(frameTime - timeElapsed);
				} catch (InterruptedException ex) {
					System.err.println("Testing Paused...");
				}
			}
			prevTime = sw.pSim.time();
			//System.out.print(".");
		}


     		 }
 	}

	public static void main(String[] args){
		
		//Initalize Dynamic Taxi Pick-Up Vars
		ArrayList<Integer> amountPassengers = new ArrayList<Integer>(10);
		ArrayList<Integer> amountTaxis = new ArrayList<Integer>(10);
		
		//Notes and instructions
		System.out.println("----->NOTE: Hard-coded Tests No.10 and No.11 are intended to fail.");
		System.out.println("----->      This is due to the design of the queue extension");
		System.out.println();
		System.out.println("----->NOTE: Due to multi-threaded Testing harness:");
		System.out.println("----->      Tests may fail to initalize as intended.");
		System.out.println("----->      Restart if required by pressing stop and start again");
		System.out.println();
		System.out.println("----->NOTE: Simulation can be sped up using slider but results may vary.");
		System.out.println();
		System.out.println("USAGE: Enter input as prompted; Once APP loaded click start to begin Testing");
		System.out.println("----------------------------------------------------------------------------");

		//User input for Arrays
		Scanner scan= new Scanner(System.in);
		System.out.println("Enter amount of (Taxi Pick-Up) Tests: ");
		int amountTests = scan.nextInt();
		scan.nextLine();
		System.out.println("Enter No. of Passengers for Test followed by No. of Taxis separate by whitespace");
		for (int x=0; x<amountTests; x++){
			System.out.println("Test " + (x+1) + ":");
			String Line = scan.nextLine();
			Scanner LineScanner = new Scanner(Line);

   			int pass = LineScanner.nextInt();
			int taxis = LineScanner.nextInt();
    				
			//System.out.println(pass + " " + taxis);
			amountPassengers.add(pass);
			amountTaxis.add(taxis);
			
		}

		//Load all sprites
		try {
			MediaTracker m = new MediaTracker(new JPanel()); //Tracks the loading of images
			//Load entity sprites
			Car.loadSprites(m);
			Bus.loadSprites(m);
			Taxi.loadSprites(m);
			Pedestrian.loadSprites(m);
			//Load road signs
			new Sprite("frome_rd/roadSign.gif", m);
			new Sprite("north_tce/roadSign.gif", m);
			//Load road arrows
			new Sprite("road_arrows/left.gif", m);
			new Sprite("road_arrows/left_straight.gif", m);
			new Sprite("road_arrows/right_straight.gif", m);
			new Sprite("road_arrows/straight.gif", m);
			new Sprite("road_arrows/right.gif", m);
			//Load pedestrian light sprites
			new Sprite("pedestrian_lights/flashing.gif", m);
			new Sprite("pedestrian_lights/stop.gif", m);
			new Sprite("pedestrian_lights/walk.gif", m);
			//Load traffic light sprites
			new Sprite("traffic_lights/greenLeft.gif", m);
			new Sprite("traffic_lights/greenRight.gif", m);
			new Sprite("traffic_lights/greenStraight.gif", m);
			new Sprite("traffic_lights/yellowLeft.gif", m);
			new Sprite("traffic_lights/yellowRight.gif", m);
			new Sprite("traffic_lights/yellowStraight.gif", m);
			new Sprite("traffic_lights/redLeft.gif", m);
			new Sprite("traffic_lights/redRight.gif", m);
			new Sprite("traffic_lights/redStraight.gif", m);
			new Sprite("traffic_lights/offLeft.gif", m);
			new Sprite("traffic_lights/offRight.gif", m);
			new Sprite("traffic_lights/offStraight.gif", m);
			//Wait for loading to finsh
			m.waitForAll();
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: One or more sprites could not be loaded");
		} catch (InterruptedException e) {
			System.err.println("ERROR: Loading process interrupted");
		}


		final SimWindow sw = new SimWindow();
		final StatsCollector sc = new StatsCollector();
		final SimProperties sp = new SimProperties();
		//Intersection i;

		//sc.results.add(true);

		sp.taxiRank = true;
		

		/*Road northTce = i.northTce;
		System.out.println(northTce);

		VehicleLane taxiLane = northTce.getVehicleLanes().get(1);
		PedestrianLane pLaneWest = i.northTce.pLanes.get(1);
		PedestrianLane pLaneEast = i.northTce.getPedestrianLanes().get(1);
		System.out.println(pLaneWest);*/
		//Pedestrian p;

		//taxiLane.getEntryX()
		//taxiLane.getEntryY()

		//System.out.println("Start " +i.cTick);

    		/*while (true) {
			if ( i.cTick == 5){
				//Vehicle newCar = new Taxi(taxiLane.getEntryX(), taxiLane.getEntryY(), taxiLane.dirDeg(), taxiLane, null);
				//newCar.setBox(taxiLane.getBox());
				//Vehicle newCar2 = new Taxi(800, 200, taxiLane.dirDeg(), taxiLane, null);
				//newCar2.setBox(taxiLane.getBox());
				//pLaneWest.addPedestrianTest(1000, 158, 0);
				//p = new Pedestrian(1000, 158, 0, pLaneWest);
				System.out.print("Started " +i.cTick);
			} else {
				//p = new Pedestrian(800, 158, 0, pLaneWest);
				System.out.print(".");
				//pLaneWest.addPedestrian(800, 158, 0);
			}
			try {
				Thread.sleep((long)(sw.pSim.TPS*sw.pSim.speed));
			} catch(InterruptedException ex) {}
       			
    		}*/

		//  open up standard input
		/*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int userName = null;

		//  read the username from the command-line; need to use try/catch with the
		//  readLine() method
		try {
			userName = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}

		System.out.println("Thanks for the name, " + userName);*/
	

		sw.pBot.bStartPause.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {

		if (!run){
			//SomeRunnable someRunnable = new SomeRunnable(sw, sc, sp, i);
 			//Thread someRunnableThread = new Thread(someRunnable);
 			//someRunnableThread.start();

			//Runnable someRunnableThread = new SomeRunnable(sw, sc, sp, i);
			//new Thread(someRunnableThread).start();
			
			if (i.cTick == 0){
				i = new Intersection(sp, sc);
				Thread someRunnableThread = new Thread(new test().new SomeRunnable(sw, sc, sp, i, amountTests, amountPassengers, amountTaxis));
				someRunnableThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

        				public void uncaughtException(Thread t, Throwable e) {
           					//System.out.println(t + " throws exception: " + e);
        				}
    				 });
   				someRunnableThread.start();
			} else {

				Thread someRunnableThread = new Thread(new test().new SomeRunnable(sw, sc, sp, i, amountTests, amountPassengers, amountTaxis));
   				someRunnableThread.start();
			}
			//pLaneEast.addPedestrianTest(900, 168, 0);

			run = true;
			
		} else {
			//someRunnableThread.join();
			run = false;
		}
	
                
            }
        }); 

		sw.pBot.bStop.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
				//System.err.println("Testing Stopped...");
				//someRunnableThread.join();
				run = false;
				i.cTick = 0;
				i.northTce.getPedestrianLanes().clear();
				i.northTce.getVehicleLanes().clear();
				for (int x=0; x<sc.results.size(); x++){
						System.out.println("Test " + (x+1) + ": " + sc.results.get(x));
					}
				
				sc.results.clear();
            }
        }); 
/*
		//  prompt the user to enter their name
		System.out.print("Enter your name: " + sp.taxiRank);

		//  open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String userName = null;

		//  read the username from the command-line; need to use try/catch with the
		//  readLine() method
		try {
			userName = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}

		System.out.println("Thanks for the name, " + userName);*/

	}
}
