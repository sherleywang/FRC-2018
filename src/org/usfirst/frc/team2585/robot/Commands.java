package org.usfirst.frc.team2585.robot;

import org.usfirst.frc.team2585.systems.CubeLiftSystem;
import org.usfirst.frc.team2585.systems.WheelSystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A container class for the different commands that can be passed to the autonomous executor
 */
public class Commands {
	
	private static Environment environ;
	private static WheelSystem drivetrain;
	private static CubeLiftSystem cubeLift;
	
	private static final double METERS_PER_SECOND = 0.865; // ROBOT SPEED in  m/s
	private static final double ROBOT_LENGTH = 1.0668; // LENGTH OF THE ROBOT PLUS THE BUMPERS
	
	/**
	 * Constructor that sets the environment and the required systems
	 * @param e the environment of the robot
	 */
	public Commands(Environment env) {
		environ = env;

		drivetrain = (WheelSystem) environ.getSystem(Environment.WHEEL_SYSTEM);
		cubeLift = (CubeLiftSystem) environ.getSystem(Environment.CUBE_LIFT_SYSTEM);
	}
	
	/**
	 * Move the robot forward with no rotation
	 */
	private static void driveForward() {
		drivetrain.driveWithGyro(0.25, 0.0);
	}
	
	/**
	 * Turn the robot to the given angle
	 * @param rotation the angle to rotate to
	 * @return the absolute value of the angle error remaining after rotating
	 */
	private static double turnToAngle(double angle) {
		return Math.abs(drivetrain.rotateToAngle(angle));
	}
	
	private static double turnLeft() {
		return turnToAngle(90.0);
	}
	
	private static double turnRight() {
		return turnToAngle(-90.0);
	}
	
	private static double turnStraight() {
		return turnToAngle(0.0);
	}
	
	/**
	 * Set the forward movement and rotation to 0
	 */
	private static void stop() {
		drivetrain.driveWithGyro(0, 0);
	}
	
	/**
	 * Use the intake system to deposit a cube
	 */
	private static void depositCube() {
		cubeLift.rotateUp();
	}
	
	/**
	 * @param distance in meters
	 * @return time it will take the robot to drive the given distance in milliseconds
	 */
	private static long distanceToTime(double meters) { 
		final double METERS_PER_MILLISECOND = METERS_PER_SECOND * 0.001; // Conversion: m/s * (1 s/1000 ms)
		return Math.round(meters / METERS_PER_MILLISECOND);
	}
	
	/**
	 * Autonomous command that drives according to its position and the side of its switch
	 */
	public class Main extends AutonomousCommand {
		private long delayTime = 2000;
		private long timeToDepositCube = 1500;
		
		private long middleDistanceToSwitch = distanceToTime(3.556-ROBOT_LENGTH);
		private long middleLeftSegment = distanceToTime(2.1336);
		private long middleRightSegment = distanceToTime(0.9144);
		private long sideDistanceToSwitch = distanceToTime(4.2672-(ROBOT_LENGTH/2));
		private long distanceInToSwitchFromSide = distanceToTime(0.9144);
		
		private boolean shouldResetTime = false;
		private boolean onLeftWithSwitch = false; 
		private boolean onRightWithSwitch = false;
		
		private int tasksComplete = 0;
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.robot.AutonomousCommand#updateGameData()
		 */
		@Override
		public void updateGameData() {
			super.updateGameData();
			
			tasksComplete = 0;
			onLeftWithSwitch = gameData.charAt(0) == 'L' && location == 1;
			onRightWithSwitch = gameData.charAt(0) == 'R' && location == 3;
		}
		
		/**
		 * Mark that a task has been complete
		 */
		private void markTaskComplete() {
			tasksComplete++;
			shouldResetTime = true;
			stop();
			SmartDashboard.putNumber("AUTO TASK NUMBER", tasksComplete);
		}
		
		/**
		 * Run straight the distance to the auton line
		 * @param timeElapsed the time elapsed since the last task was completed
		 */
		private void runStraight(long timeElapsed) {
			if (timeElapsed < sideDistanceToSwitch) {
				driveForward();
			} else {
				stop();
			}
		}
		
		/**
		 * Run the auton starting from the middle driverstation
		 * @param timeElapsed the time elapsed since the last task was completed
		 */
		private void runFromMiddle(long timeElapsed){
			/**
			 * All convertToTime inputs should be distances in centimeters
			 */
			boolean leftSwitch = gameData.charAt(0) == 'L';
			boolean rightSwitch = gameData.charAt(0) == 'R';
			
			if(gameData.length() < 1){
				runStraight(timeElapsed);
			} else {
				switch (tasksComplete) {
				case 0: // DELAY
					if (timeElapsed > delayTime) {
						markTaskComplete();
					} 	
					break;
					
				case 1: // MOVE FORWARD 
					if (timeElapsed < middleDistanceToSwitch/2) {
						driveForward();
					} else {
						markTaskComplete();
					}
					break;
					
				case 2: // ROTATE 
					if (leftSwitch) { // Switch on left side
						if (turnLeft() < 0.5) { // Turn left
							markTaskComplete();
						}
					} else if (rightSwitch) { // Switch on right side
						if (turnRight() < 0.5) { // Turn right
							markTaskComplete();
						}
					}
					break;
					
				case 3: // MOVE TOWARDS SIDE OF SWITCH
					if (leftSwitch) {
						if (timeElapsed < middleLeftSegment) {
							driveForward();
						} else {
							markTaskComplete();
						}
					} else if (rightSwitch) {
						if (timeElapsed < middleRightSegment) {
							driveForward();
						} else {
							markTaskComplete();
						}
					}
					break;
					
				case 4: // ROTATE BACK TO STRAIGHT
					if (turnStraight() < 0.5) { // turn back to straight
						markTaskComplete();
					}
					break;
					
				case 5: // MOVE UP TO SWITCH
					if (timeElapsed < middleDistanceToSwitch/2) {
						driveForward();
					} else {
						markTaskComplete();
					}
					break;
					
				case 6: // DROP CUBE 
					if (timeElapsed < timeToDepositCube) {
						depositCube();
					} else {
						markTaskComplete();
					}
					break;
					
				default:
					stop();
					break;
				}
			}				
		}

		/**
		 * Run the auton from the left or right driverstation
		 * @param timeElapsed the time since the last task was completed
		 */
		private void runFromSide(long timeElapsed){
			if (onLeftWithSwitch || onRightWithSwitch) {
				switch(tasksComplete) {
				case 0: // MOVE FORWARD
					SmartDashboard.putString("AUTO STATUS", "MOVE FORWARD");
					if(timeElapsed < sideDistanceToSwitch){
						driveForward();
					} else {
						markTaskComplete();
					}
					break;
					
				case 1: // TURN INWARDS
					SmartDashboard.putString("AUTO STATUS", "TURN IN");
					if (onLeftWithSwitch) {
						if(turnRight() < 0.5){ 
							markTaskComplete();
						}
					} else if (onRightWithSwitch) {
						if(turnLeft() < 0.5){ 
							markTaskComplete();
						}
					}
					break;
					
				case 2: // MOVE INWARDS TOWARDS SWITCH
					SmartDashboard.putString("AUTO STATUS", "MOVE FORWARD AGAIN");
					if(timeElapsed < distanceInToSwitchFromSide){
						driveForward();
					} else {
						markTaskComplete();
					}
					break;
					
				case 3: // DEPOSIT CUBE
					SmartDashboard.putString("AUTO STATUS", "DEPOSIT");
					if(timeElapsed < timeToDepositCube) {
						depositCube();
					} else {
						markTaskComplete();
					}
					break;
				default:
					SmartDashboard.putString("AUTO STATUS", "STOP BY DEFAULT");
					stop();
					break;
				}
			} else { // Switch is on the other side
				runStraight(timeElapsed);
			}
		}
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.AutonomousCommand#execute(long)
		 */
		@Override
		public boolean execute(long timeElapsed) {
			SmartDashboard.putString("AUTO EXECUTOR", "MAIN");
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
	public class Straight extends AutonomousCommand {
		private static final int timeToDriveStraight = 2000;
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.AutonomousCommand#execute(long)
		 */
		@Override
		public boolean execute(long timeElapsed) {
			SmartDashboard.putString("AUTO EXECUTOR", "STRAIGHT");
			if (timeElapsed < timeToDriveStraight) {
				driveForward();
			} else {
				stop();
			}
			return false;
		}
	}
	
	/**
	 * Autonomous command that drives straight for three seconds to find speed of the robot
	 */
	public class SpeedTest extends AutonomousCommand {
		private static final int timeToDriveStraight = 3000;
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.AutonomousCommand#execute(long)
		 */
		@Override
		public boolean execute(long timeElapsed) {
			SmartDashboard.putString("AUTO EXECUTOR", "STRAIGHT");
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
	public class None extends AutonomousCommand {
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.robot.AutonomousCommand#execute(long)
		 */
		@Override
		public boolean execute(long timeElapsed) {
			SmartDashboard.putString("AUTO EXECUTOR", "NONE");
			stop();
			return false;
		}
	}
}
