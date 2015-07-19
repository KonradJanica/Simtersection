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

	

	public static void main(String[] args){

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
		//final SimController sCont = new SimController(sp, sc);
		final Intersection i;
		
		

		i = new Intersection(sp, sc);

		//i.test();

		
		//i = new Intersection(sp, sc);

		//sc.results.add(true);

		//sp.taxiRank = true;
		

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
	

		/*sw.pBot.bStartPause.addActionListener(new ActionListener() {
 
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
        }); */

		sw.pBot.bStop.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
				//System.err.println("Testing Stopped...");
				//someRunnableThread.join();
				//run = false;
				//i.cTick = 0;
				//i.northTce.getPedestrianLanes().clear();
				//i.northTce.getVehicleLanes().clear();
				i.reset_cTick();

				sw.stopPressed();
				sw.pBot.reset();
				sw.pBot.bStop.setEnabled(true);
				
				/*for (int x=0; x<sc.results.size(); x++){
						System.out.println("Test " + (x+1) + ": " + sc.results.get(x));
					}*/
				
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
