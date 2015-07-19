package npe.sim.road;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.awt.Color;

import npe.sim.Sprite;
import npe.sim.Utils;
import npe.sim.entity.Taxi;
import npe.sim.entity.Vehicle;
import npe.sim.entity.Vehicle.State;
import npe.sim.entity.Vehicle.Intention;
import npe.sim.entity.Entity;
import npe.sim.entity.Pedestrian;


/**
 * An object representing the Taxi Rank on the eastern side of North Terrace
 */

public class TaxiRank {

	///////////////////
	//---VARIABLES---//
	///////////////////
	/**Sprite for the Taxi rank.*/
	protected Sprite sprite = null;
	//Dimensions
	/**The x coordinate of the top left of the rank.*/
	private double x;
	/**The y coordinate of the top left of the rank.*/
	private double y;
	/**The width of the sprite.*/
	public static final int WIDTH = 40;
	/**The height of the sprite.*/
	public static final int HEIGHT = 42;
	/**The lane representing the Taxi rank where Taxis will stop.*/
	private VehicleLane lane;
	
	/**The current taxi stopping at the Taxi rank.*/
	private Vehicle currentTaxi = null;
	/**The coordinates of where the first taxi should stop.*/
	private double stopPointX;
	private double stopPointY;
	/**Variables used to keep taxis stopped at the Taxi rank.*/
	private int t = 0;
	private boolean timer = false;
	private static final int STOP_TIME = (int) Utils.convertTime(5);
	
	/**Queue containing all the taxis entering the taxi rank lane.*/
	LinkedList<Vehicle> taxiQueue;
	
	/////////////////////PASSENGER MEMBER VARIABLES//////////////////
	/**The lane representing where Passengers will exit Taxi.*/
	private PedestrianLane pLaneEast;
	// The lane representing the West side of North Tce. To allow capturing in queue zone
	private PedestrianLane pLaneWest;
	// Pedestrian representing the front seat passenger
	private Pedestrian pas1;
	// Pedestrian representing the rear North seat passenger
	private Pedestrian pas2;
	// Pedestrian representing the rear South seat passenger
	private Pedestrian pas3;
	// Random variables for controlling specific spawn and movement as appropriately named
	private double random_pas_spawn;
	private double random_pas_direction;
	private int random_pas_movement;
	/**A rectangle which defines the area in which pedestrians stop and wait in.*/
	private Rectangle2D loadingArea;
	// Represents the x and y coordinate for start of loadingArea (respectfully)
	double areaStartX;
	double areaStartY;
	// To ensure only first passenger is controlled in the taxi stop tick
	boolean enableFirst = false;
	
	//////////////////
	//---CREATION---//
	//////////////////
	public TaxiRank(double x, double y, VehicleLane lane, PedestrianLane ped_Lane_East, PedestrianLane ped_Lane_West)
	{
		this.x = x;
		this.y = y;
		this.lane = lane;
		this.pLaneEast = ped_Lane_East;
		this.pLaneWest = ped_Lane_West;
		
		//Set the sprite
		setSprite("north_tce/taxiRankSign.gif");
		
		//Initialise taxi Queue
		taxiQueue = new LinkedList<Vehicle>();
		
		//store lane height for nicer code
		double laneHEIGHT = PedestrianLane.LANE_WIDTH;
		
		//Set the stopping points of taxis
		stopPointX = x + Taxi.WIDTH;
		stopPointY = y + HEIGHT + laneHEIGHT + VehicleLane.LANE_WIDTH/2;
		//Set the stopping points of passengers
		areaStartX = x+Taxi.WIDTH;
		areaStartY = y + HEIGHT + laneHEIGHT -(laneHEIGHT/4+10);
		
		//Set start of queue
		loadingArea = new Rectangle2D.Double(areaStartX, areaStartY, 1, laneHEIGHT/4+10);
	}
	
	//////////////
	//---LOOP---//
	//////////////
	public void tick()
	{
		//Get last vehicle in the lane
		Vehicle lastVehicle = lane.getLastVehicle();
		Vehicle lastTaxi = null;
		
		if (!taxiQueue.isEmpty()) {
			//Get the last taxi in the queue
			lastTaxi = taxiQueue.getLast();
		}

		//If the last vehicle in the lane is a taxi and hasn't already been added
		//To the taxi queue, add it to the end
		if (lastVehicle != lastTaxi && lastVehicle instanceof Taxi) {
			//Check if the taxi has already stopped at the taxi rank
			if (!((Taxi)lastVehicle).hasStopped()) {
				taxiQueue.add(lastVehicle);
			}
		}
		
		pLaneEast.stop(loadingArea);
		//Transfer pedestrians to east pLane to allow for capture
		pLaneWest.transferLanes(pLaneEast);
		
		if (t > 0){ //This will optimize code
			Pedestrian p = pLaneEast.getFirstPassenger();
			if (p != null){
				//Only tick first passenger if enabled
				if (enableFirst){
					p.tick();
				}
					
				if (t == 1) {
					enableFirst = true;
					p.setDirection(180);
					p.stop();
					p.start();
				} else if (p.x() < stopPointX-25 && p.x() > stopPointX-40 && t > 5) {
					p.setDirection(135);
				} else if (p.x() < stopPointX-40 && p.y() < stopPointY-15 && t > 5) {
					p.setDirection(90);
				} else if (p.y() > stopPointY-15) {
					pLaneEast.removeFirstPassenger();
					enableFirst = false;
				}
					
				if (t == 5){ //Decrease Area capture zone for moving up queue
					loadingArea.setRect(loadingArea.getX()-Pedestrian.PEDESTRIAN_WIDTH, areaStartY, loadingArea.getWidth(), loadingArea.getHeight());
				} else if (t > 5 && t <= Pedestrian.PEDESTRIAN_WIDTH+5) {
					pLaneEast.resetPassengerQueue();
					//p.setSpeed(Utils.convertSpeed(p.MAX_SPEED*0.8));
					//System.out.println("--------------------------------- The Value of t: " + t);
				}
					
			}
		}
		
		//Conditions for Exitting Passenger Movement
		if (random_pas_direction < 3){ //70% percentage of passengers would realistically head West on North Terrace
			//Passengers heading right (East)
			if (pas1 != null && t == 15){
				if (pas3 != null){
					pas3.setDirection(270);
				}
			} else if (pas1 != null && pas1.y() < areaStartY-10) {
				pas1.setDirection(0);
				if (pas2 != null && pas2.y() < areaStartY-10){
					pas2.setDirection(0);
				}}
			if (pas3 != null && pas3.y() < areaStartY-10){
					pas3.setDirection(0);
				}
			
		} else {
			//Passengers heading left (West)
			if (pas1 != null && t == 15){
				pas1.setDirection(225);
				if (pas2 != null){
					pas2.setDirection(225);
				}
				if (pas3 != null){
					pas3.setDirection(270);
				}
			} else if (pas1 != null) { 
				if (t == random_pas_movement || pas1.y() < areaStartY-10) {
				//System.out.println("--------------------------------- The Value of t: " + t);
				pas1.setDirection(180);
				}
				if (pas2 != null){
					if (t == random_pas_movement || pas2.y() < areaStartY-10) {
						pas2.setDirection(180);
					}
				}
			} if (pas3 != null) {
				if (t == 85 || pas3.y() < areaStartY-10){
					pas3.setDirection(180);
				}
			}
		}
		
		//Get the first taxi in the queue
		currentTaxi = taxiQueue.peek();
	
		//If the vehicle is a taxi and not already passed the rank, tell it to stop
		if (currentTaxi != null) {
			if (!timer) {
				currentTaxi.stop(stopPointX, stopPointY, currentTaxi.getIntent());	
				if (currentTaxi.getState() == State.STOPPED) {
					timer = true;
					//Get random value between: [1,10] for direction randomization below.
					random_pas_direction = Utils.random()*11+1;
					//Get random value between: [16,90] for movement randomization above tied with t var.
					random_pas_movement = (int)(Utils.random()*(STOP_TIME-20)+20);
					//Get random value between: [1,10] for spawn randomization below.
					random_pas_spawn = Utils.random()*11+1;
					//Dereference here to avoid controlling unintended passengers above
					pas1 = null;
					pas2 = null;
					pas3 = null;
					if (random_pas_spawn <= 5){ //50% of values (For realistic taxi passengers)
						pas1 = pLaneEast.addPedestrian(stopPointX-40, stopPointY-15, 270);
					} else if (random_pas_spawn <= 7.5) { //25%
						pas1 = pLaneEast.addPedestrian(stopPointX-40, stopPointY-15, 270);
						pas2 = pLaneEast.addPedestrian(stopPointX-60, stopPointY-15, 270);
					} else if (random_pas_spawn <= 9) { //15% 
						pas1 = pLaneEast.addPedestrian(stopPointX-40, stopPointY-15, 270);
						pas2 = pLaneEast.addPedestrian(stopPointX-60, stopPointY-15, 270);
						pas3 = pLaneEast.addPedestrian(stopPointX-60, stopPointY+15, 180);
						pas3.setSpeed(Utils.convertSpeed(Pedestrian.MAX_SPEED));
					} // remaining 10% holds no passengers
				}
			} else if (t < STOP_TIME) {
				currentTaxi.stop(stopPointX, stopPointY, currentTaxi.getIntent());
			} else {
				((Taxi)currentTaxi).leaveRank();
				taxiQueue.remove();
				currentTaxi = null;
				timer = false;
				t = 0;
			}
		}
		
		//If timer is on, increment time
		if (timer) {
			t++;
		}		
	}

	public void draw(Graphics2D g)
	{
		//g.setColor(Color.WHITE);
		sprite.draw(g, x, y);
		//g.drawRect((int) x, (int) y, (int) WIDTH, (int) HEIGHT);
		//g.drawOval((int) stopPointX, (int) stopPointY, 2, 2);
		//g.drawRect((int) areaStartX, (int) areaStartY, (int) Pedestrian.PEDESTRIAN_WIDTH*10, (int) PedestrianLane.LANE_WIDTH/4+10);
		int southLineY = (int) (areaStartY+PedestrianLane.LANE_WIDTH/4+10);
		int northLineY = (int) (areaStartY-Pedestrian.PEDESTRIAN_HEIGHT*0.1);
		int LinesXend = (int)(areaStartX+Pedestrian.PEDESTRIAN_WIDTH*10);
		g.setColor(Color.DARK_GRAY);
		g.drawLine((int) areaStartX, northLineY, LinesXend, northLineY);
		g.drawLine((int) areaStartX, southLineY, LinesXend, southLineY);
	}
	
	/**Sets the sprite for the entity.
	 * @param spriteName Filename of sprite to use.
	 */
	public void setSprite(String spriteName)
	{
		try {
			sprite = new Sprite(spriteName);
		} catch (Exception e) {
			System.err.println("Sprite '"+spriteName+"' does not exist.");
		}
	}
	
}
