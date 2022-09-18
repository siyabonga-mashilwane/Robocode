package SK;
import robocode.*;
import robocode.AdvancedRobot;

//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Bd - a robot by (your name here)
 */
public class Bd extends AdvancedRobot
{
	double normalizeBearing(double angle) { // Normalize the bearing to be between 180 and -180
	while (angle >  180) angle -= 360;
	while (angle < -180) angle += 360;
	return angle;
	}
	/**
	 * run: Bd's default behavior
	 */
	boolean inWall; // Is true when robot is near the wall.
	boolean movingForward; // Is true when robot needs to move foward
	double opponentDistance;
	double opponentBearing;
	double x; // x position of robot
    double y; // y position of robot
	int turn = 0;
	double buffer; // 20% distance boundry
	public void run() {
		// Initialization of the robot should be put here
		double width = getBattleFieldWidth();
		double height = getBattleFieldHeight();
		buffer = (20/100)*Math.max(width, height);
		x = getX();
		y = getY();
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:
		if (getX() <= 50 || getY() <= 50 || getBattleFieldWidth() - getX() <= 50 || getBattleFieldHeight() - getY() <= 50) {
				inWall = true;
			} else {
			inWall = false;
		}
		
		ahead(40000); // go ahead until you get commanded to do differently
		turnRadarRight(360); // scan until you find your first enemy
		
		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			if (getX() > 60 && getY() > 60 && getBattleFieldWidth() - getX() > 60 && getBattleFieldHeight() - getY() > 60 && inWall == true) { // Avoids walls by turning away before going into the wall
				inWall = false;
			}
			if (getX() <= 60 || getY() <= 60 || getBattleFieldWidth() - getX() <= 60 || getBattleFieldHeight() - getY() <= 60 ) {
				if ( inWall == false){
					turnRight(360);   //Turn the opposite direction when near the wall
					ahead(150);
					inWall = true;
				}
			}
			for(double i = 0; i < 180; i+=20){ // Do a quick scan of the surrounding untill robot is detected
				turnRadarRight(i);
				turnGunRight(i);
			}
			
			// Simple "CRAZY" algorithm for non uniform movement pattern
			// Tell the game we will want to move ahead 40000 -- some large number
			setAhead(40000);
			movingForward = true;
			// Tell the game we will want to turn right 90
			setTurnRight(90);
			// At this point, we have indicated to the game that *when we do something*,
			// we will want to move ahead and turn right.  That's what "set" means.
			// It is important to realize we have not done anything yet!
			// In order to actually move, we'll want to call a method that
			// takes real time, such as waitFor.
			// waitFor actually starts the action -- we start moving and turning.
			// It will not return until we have finished turning.
			waitFor(new TurnCompleteCondition(this));
			// Note:  We are still moving ahead now, but the turn is complete.
			// Now we'll turn the other way...
			setTurnLeft(180);
			// ... and wait for the turn to finish ...
			waitFor(new TurnCompleteCondition(this));
			// ... then the other way ...
			setTurnRight(180);
			// .. and wait for that turn to finish.
			waitFor(new TurnCompleteCondition(this));
			// then back to the top to do it all again
		
			ahead(100);
			
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		double localturn = getHeading() - getGunHeading() + e.getBearing(); // Find direction of the enemy robot in comparison to my robobt gun
		turnGunRight(normalizeBearing(localturn));
		if(getEnergy() > 50){
			if(e.getDistance() < buffer){
				fire(3);
			}else{
				fire(2);
					
			}
		}
		

		fire(1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		ahead(100); // Move away from the line of fire
		turnRight(50); // turn away from line of fire to better position
		
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		turnLeft(50); // turn away from wall to better position
		ahead(100); // Move away from the wall, avoids bouncing off
	}	
}
