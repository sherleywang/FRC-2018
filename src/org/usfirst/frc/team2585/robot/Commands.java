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
		drivetrain.driveWithRotation(0.8, 0.0);
	}
	
	/**
	 * Turn the robot left with no forward movement
	 */
	private static void turnLeft() {
		drivetrain.driveWithRotation(0.0, -0.2);
	}
	
	/**
	 * Turn the robot right with no forward movement
	 */
	private static void turnRight() {
		drivetrain.driveWithRotation(0.0, 0.2);
	}
	
	/**
	 * Set the forward movement and rotation to 0
	 */
	private static void stop() {
		drivetrain.driveWithRotation(0, 0);
	}
	
	
	/**
	 * Autonomous command that that drives the robot forward and then turns left and continues driving forward
	 */
	public class Main implements AutonomousCommand {
		
		private void runFromMiddle() {
		}
		
		private void runFromSide() {
		}
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.AutonomousCommand#execute(long)
		 */
		@Override
		public void execute(long timeElapsed) {
			if (location == 2) {
				runFromMiddle();
			} else {
				runFromSide();
			}
		}
	}
	
	/**
	 * Autonomous command that drives the robot forward and then turns right and drives forward again
	 */
	public class Straight implements AutonomousCommand {
		private static final int timeToDriveStraight = 3100;
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.AutonomousCommand#execute(long)
		 */
		@Override
		public void execute(long timeElapsed) {
			if (timeElapsed < timeToDriveStraight) {
				driveForward();
			} else {
				stop();
			}
		}
	}
	
	public class None implements AutonomousCommand {
		@Override
		public void execute(long timeElapsed) {
			stop();
		}
	}
}
