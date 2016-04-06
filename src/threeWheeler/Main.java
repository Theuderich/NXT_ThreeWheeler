package threeWheeler;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class Main {

	public static void main(String[] args) {
		
		Movement move = new Movement();
		TouchSensor left = new TouchSensor(SensorPort.S4);
		TouchSensor right = new TouchSensor(SensorPort.S3);
		UltrasonicSensor ui = new UltrasonicSensor(SensorPort.S1);
		
//		move.calSteering();
//		move.calSteering();
		
		move.driveStraight(Movement.direction.FORWARD, 200);
	
		while( true ){
			
			float distance = ui.getDistance();
			
			if( distance < 20){
				move.driveStraight(Movement.direction.BACKWARD, 200);
				Delay.msDelay(500);
				move.drivePointTurn(Movement.direction.RIGHT, 60);
				move.driveStraight(Movement.direction.FORWARD, 200);
			}
			
			if( left.isPressed() ){
				move.driveStraight(Movement.direction.BACKWARD, 200);
				Delay.msDelay(500);
				move.drivePointTurn(Movement.direction.RIGHT, 90);
				move.driveStraight(Movement.direction.FORWARD, 200);
			}
						
			if( right.isPressed() ){
				move.driveStraight(Movement.direction.BACKWARD, 200);
				Delay.msDelay(500);
				move.drivePointTurn(Movement.direction.LEFT, 90);
				move.driveStraight(Movement.direction.FORWARD, 200);
			}
			
			if( Button.waitForAnyEvent(1) == Button.ID_ESCAPE )
				break;
			
		}
		
		move.stopMove();
		Globals.logMsg.closeLog();
		
		Button.waitForAnyEvent(0);
	}

}
