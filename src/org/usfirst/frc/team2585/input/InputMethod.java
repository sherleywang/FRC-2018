package org.usfirst.frc.team2585.input;

import org.impact2585.lib2585.XboxConstants;

/**
 * Operator controls for the robot
 */
public abstract class InputMethod {
	/**
	 * @return a boolean denoting whether or not the robot should intake
	 */
	public boolean shouldIntake() {
		return false;
	}

	/**
	 * @return a boolean denoting whether or not the robot should outtake
	 */
	public boolean shouldOuttake() {
		return false;
	}
	
	/**
	 * @return a boolean denoting whether or not the robot should "throw" the cube
	 */
	public boolean shouldRotateUp() {
		return false;
	}
	
	/**
	 * @return a boolean denoting whether or not the robot should turn the cubeCollector
	 */
	public boolean shouldRotateDown() {
		return false;
	}
	
	/**
	 * @return a boolean denoting whether or not the robot should start climbing
	 */
	public boolean shouldClimb() {
		return false;
	}
	
	/**
	 * @return a boolean denoting whether or not the robot's climb arm should return to its original position
	 */
	public boolean shouldRetractArm() {
		return false;
	}
	
	/**
	 * @return the amount that the robot should drive forward
	 */
	public double forwardAmount() {
		return 0;
	}

	/**
	 * @return the amount that the robot should rotate. +1 is counter-clockwise. -1 is clockwise
	 */
	public double rotationAmount() {
		return 0;
	}
	
	/**
	 * @return whether the drive train should move faster(boost)
	 */
	public boolean shouldBoost() {
		return false;
	}
	
	/**
	 * @return whether the camera view should switch
	 */
	public boolean shouldSwitchCamera() {
		return false;
	}
	
	/**
	 * @return whether the robot gyro should recalibrate
	 */
	public boolean shouldCalibrate() {
	 		return false;
	 	}

}
