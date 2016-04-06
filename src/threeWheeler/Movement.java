package threeWheeler;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassHTSensor;
import lejos.util.Delay;

public class Movement {

	public enum direction{
		FORWARD, BACKWARD, LEFT, RIGHT
	}
	
	// Wheel circumference in millimeter
	private static final float WHEEL_CIRCUM = 172;

	// axis distance in millimeter
	private static final float AXIS_DISTANCE = (float) 112.5;

	private static final float POINT_TURN_ROTATIONS = 200;
	
	// Constant definition of left and right motors
	private static final NXTRegulatedMotor LEFT_MOTOR = Motor.B;
	private static final NXTRegulatedMotor RIGHT_MOTOR = Motor.A;
	
	
	private float steeringOffset;
	
	/*
	 * Konstructor
	 */
	Movement(){
		steeringOffset = 1;
	}
	
	void calSteering(){
		
		float beforeMove;
		float afterMove;
		float errorAngel;
		
		float rMiddle;
		float r1;
		float r2;
		
		float steeringOffset;
		
		CompassHTSensor compass = new CompassHTSensor(SensorPort.S2);
		
		// drive 300 millimeter and measure the angle difference
		beforeMove = compass.getDegrees(); 
		this.driveStraight(direction.FORWARD, 100);
		Globals.logMsg.addFunctionMsg( "Movement.calSteering", "start degree: " + beforeMove );

		// logg movement during the 10 second delay
		for(int i=0; i<10; i++){
			Globals.logMsg.addFunctionMsg( "Movement.calSteering", "current" + i + ": " + compass.getDegrees() );
			Delay.msDelay(1000);
		}
		
		this.stopMove();
		afterMove = compass.getDegrees(); 
		errorAngel = afterMove - beforeMove;
		
		rMiddle = (float) (1000 * 360 / (errorAngel * 2 * Math.PI));
		r1 = rMiddle - AXIS_DISTANCE / 2;
		r2 = rMiddle + AXIS_DISTANCE / 2;
		
		this.steeringOffset = r2 / r1;
		
		Globals.logMsg.addFunctionMsg( "Movement.calSteering", "stop degree: " + afterMove );
		Globals.logMsg.addFunctionMsg( "Movement.calSteering", "error degree: " + errorAngel );
		Globals.logMsg.addFunctionMsg( "Movement.calSteering", "r middle: " + rMiddle );
		Globals.logMsg.addFunctionMsg( "Movement.calSteering", "steering offset: " + this.steeringOffset );
		
	}
	
	/*
	 * Drive Straight
	 * 
	 * adjusts a certain speed for both motors
	 * speed is given in millimeter per sec
	 */
	void driveStraight(direction dir, int speed){

		// Calculate rotation per seconds
		float roundsPerSec = speed / WHEEL_CIRCUM;
		float degreePerSec = roundsPerSec * 360;
		
		// Declare for left and right motor
		float degreePerSecLeft = degreePerSec;
		float degreePerSecRight = degreePerSec;
		
		// Scale rotation with steering offset 
		if(this.steeringOffset < 1)
			degreePerSecRight = degreePerSecRight * this.steeringOffset;
		else
			degreePerSecLeft = degreePerSecLeft / this.steeringOffset;
		
		// log calculated values
		Globals.logMsg.addFunctionMsg( "Movement.setSpeed", "rotation: " + degreePerSec );
		Globals.logMsg.addFunctionMsg( "Movement.setSpeed", "current steering offset: " + this.steeringOffset );
		Globals.logMsg.addFunctionMsg( "Movement.setSpeed", "left: " + degreePerSecLeft );
		Globals.logMsg.addFunctionMsg( "Movement.setSpeed", "right: " + degreePerSecRight );
		
		if( dir == direction.LEFT || dir == direction.RIGHT)
			return;
		
		// write rotation to motors
		LEFT_MOTOR.setSpeed(degreePerSecLeft);
		RIGHT_MOTOR.setSpeed(degreePerSecRight);
		
		// Activate direction
		switch (dir){
			case FORWARD:
				LEFT_MOTOR.forward();
				RIGHT_MOTOR.forward();
				Globals.logMsg.addFunctionMsg( "Movement.setSpeed", "drive forward" );
				break;
				
			case BACKWARD:
				LEFT_MOTOR.backward();
				RIGHT_MOTOR.backward();
				Globals.logMsg.addFunctionMsg( "Movement.setSpeed", "drive backward" );
				break;
		}
		
	}
	
	/*
	 * Drive Point Turn
	 * 
	 * Performs a turn of the robot at the place
	 * Cure radius equals 0 
	 */
	void drivePointTurn(direction dir, float angel){
		
		float arcToDrive = (float) (2 * Math.PI * AXIS_DISTANCE / 2 * angel / 360);
		float arcPerMSec = WHEEL_CIRCUM * POINT_TURN_ROTATIONS / 360 / 1000; 
		float timeForTurnMSec = arcToDrive / arcPerMSec;
		
		Globals.logMsg.addFunctionMsg( "Movement.drivePointTurn", "angel: " + angel );
		Globals.logMsg.addFunctionMsg( "Movement.drivePointTurn", "arc to drive: " + arcToDrive );
		Globals.logMsg.addFunctionMsg( "Movement.drivePointTurn", "arc per msec: " + arcPerMSec );
		Globals.logMsg.addFunctionMsg( "Movement.drivePointTurn", "time to turn: " + timeForTurnMSec );
		
		LEFT_MOTOR.setSpeed(POINT_TURN_ROTATIONS);
		RIGHT_MOTOR.setSpeed(POINT_TURN_ROTATIONS);
		
		if( dir == direction.FORWARD || dir == direction.BACKWARD )
			return;
		
		switch (dir){
			case LEFT:
				LEFT_MOTOR.backward();
				RIGHT_MOTOR.forward();
				break;
				
			case RIGHT:
				LEFT_MOTOR.forward();
				RIGHT_MOTOR.backward();
				break;
		}

		Delay.msDelay((long) timeForTurnMSec);
		this.stopMove();
	}
	
	/*
	 * Stop Move
	 * 
	 * Stops both motors
	 */
	void stopMove(){
		LEFT_MOTOR.stop(true);
		RIGHT_MOTOR.stop(true);
		Globals.logMsg.addFunctionMsg( "Movement.stopMove", "motors stopped" );
		Delay.msDelay(500);
	}
}
