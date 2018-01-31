package org.usfirst.frc.team2585.robot;

import org.usfirst.frc.team2585.systems.WheelSystem;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * A container class for the different commands that can be passed to the autonomous executor
 */
public class Commands {
	
	private static Environment environ;
	private static WheelSystem drivetrain;
	
	private String gameData;
	private int location;
	
	/**
	 * Constructor that sets the environment and the required systems
	 * @param e the environment of the robot
	 */
	public Commands(Environment env) {
		environ = env;

		drivetrain = (WheelSystem) environ.getSystem(Environment.WHEEL_SYSTEM);
		
		// Location of the driverStation: 1, 2, 3; L, M, R
		location = DriverStation.getInstance().getLocation(); 
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}
	
	/**
	 * Move the robot forward with no rotation
	 */
	private static void driveForward() {
		drivetrain.driveWithGyro(0.25, 0.0);
	}
	
	/**
	 * Turn the robot left with no forward movement
	 */
	private static void turnLeft() {
		drivetrain.driveWithGyro(0.0, -0.2);
	}
	
	/**
	 * Turn the robot right with no forward movement
	 */
	private static void turnRight() {
		drivetrain.driveWithGyro(0.0, 0.2);
	}
	
	/**
	 * Set the forward movement and rotation to 0
	 */
	private static void stop() {
		drivetrain.driveWithGyro(0, 0);
	}
	
	
	/**
	 * Autonomous command that drives according to its position and the side of its switch
	 */
	public class Main implements AutonomousCommand {
		
		private static final int timeToDriveStraight = 2000;
		private int tasksComplete = 0;
		private boolean shouldResetTime = false;
		
		private void markTaskComplete() {
			tasksComplete ++;
			shouldResetTime = true;
		}
		
		private void runStraight(long timeElapsed) {
			if (timeElapsed < timeToDriveStraight) {
				driveForward();
			} else {
				stop();
			}
		}
		
		private void runFromMiddle(long timeElapsed){
			int delayTime = 2000;
			int timeToSwitch = 2000;
			int moveLeftTime = 1000;
			int moveRightTime = 1100;
			int secondForwardTime = 1000;
			
			if(gameData.length() < 1){
				runStraight(timeElapsed);
			} else {
				switch (tasksComplete) {
				case 0: // DELAY
					if (timeElapsed > delayTime) {
						markTaskComplete();
					} 	
					
				case 1: // MOVE FORWARD 
					if (timeElapsed < timeToSwitch/2) {
						Commands.driveForward();
					} else {
						markTaskComplete();
					}
					
				case 2: // ROTATE 
					if (gameData.charAt(0) == 'L') { // Switch on left side
						
					}
				}
					
//				if(gameData.charAt(0) == 'L'){
//				
//				} else if(gameData.charAt(0) == 'R'){ {
//				
//				}
			}				
		}

		private void runFromSide(long timeElapsed){
			if(gameData.length() < 1){
				runStraight(timeElapsed);
			} if(gameData.charAt(0) == 'L'){

			} else if(gameData.charAt(0) == 'R'){
				
			} else {
				runStraight(timeElapsed);
			}
		}
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.AutonomousCommand#execute(long)
		 */
		@Override
		public boolean execute(long timeElapsed) {
			shouldResetTime = false;
			if (location == 1 || location == 3) {
				runFromSide(timeElapsed);
			} else if (location == 2) {
				runFromMiddle(timeElapsed);
			} else {
				runStraight(timeElapsed);
			}
			return shouldResetTime;
		}
	}
	
	/**
	 * Autonomous command that drives straight no matter what
	 */
	public class Straight implements AutonomousCommand {
		private static final int timeToDriveStraight = 2000;
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.AutonomousCommand#execute(long)
		 */
		@Override
		public boolean execute(long timeElapsed) {
			if (timeElapsed < timeToDriveStraight) {
				driveForward();
			} else {
				stop();
			}
			return false;
		}
	}
	
	/**
	 * Autonomous command that does nothing
	 */
	public class None implements AutonomousCommand {
		@Override
		public boolean execute(long timeElapsed) {
			stop();
			return false;
		}
	}
}
