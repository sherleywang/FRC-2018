package org.usfirst.frc.team2585.input;

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
	
}
