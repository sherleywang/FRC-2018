package org.usfirst.frc.team2585.robot;

import org.usfirst.frc.team2585.systems.IntakeSystem;
import org.usfirst.frc.team2585.systems.WheelSystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A container class for the different commands that can be passed to the autonomous executor
 */
public class Commands {
	
	private static Environment environ;
	private static WheelSystem drivetrain;
	private static IntakeSystem intake;
	
	/**
	 * Constructor that sets the environment and the required systems
	 * @param e the environment of the robot
	 */
	public Commands(Environment env) {
		environ = env;

		drivetrain = (WheelSystem) environ.getSystem(Environment.WHEEL_SYSTEM);
		intake = (IntakeSystem) environ.getSystem(Environment.INTAKE_SYSTEM);
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
		intake.depositCube();
	}
	
	/**
	 * Converts distance to time
	 */
	
	private static long convertToTime(double distance) { // distance will be in centimeters
		double robotSpeed = 0.0865; // speed of robot in centimeters per millisecond
		return Math.round(distance/robotSpeed);
	}
	
	/**
	 * Autonomous command that drives according to its position and the side of its switch
	 */
	public class Main extends AutonomousCommand {
		// all convertToTime inputs are distances in centimeters
		private long distanceToDriveStraight = convertToTime(426.72);
		private long timeToDepositCube = 3500;
		private long distanceToSwitchFromSide = convertToTime(91.44);
		
		private int tasksComplete = 0;
		private boolean shouldResetTime = false;
		
		boolean onLeftWithSwitch; 
		boolean onRightWithSwitch;
		
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
			if (timeElapsed < distanceToDriveStraight) {
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
			long delayTime = 2000;
			long distanceToSwitch = convertToTime(355.6);
			long moveLeftSegment = convertToTime(213.36);
			long moveRightSegment = convertToTime(91.44);
			long depositCubeTime = 3500;
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
					if (timeElapsed < distanceToSwitch/2) {
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
						if (timeElapsed < moveLeftSegment) {
							driveForward();
						} else {
							markTaskComplete();
						}
					} else if (rightSwitch) {
						if (timeElapsed < moveRightSegment) {
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
					if (timeElapsed < distanceToSwitch/2) {
						driveForward();
					} else {
						markTaskComplete();
					}
					break;
					
				case 6: // DROP CUBE 
					if (timeElapsed < depositCubeTime) {
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
					if(timeElapsed < distanceToDriveStraight){
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
					if(timeElapsed < distanceToSwitchFromSide){
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
