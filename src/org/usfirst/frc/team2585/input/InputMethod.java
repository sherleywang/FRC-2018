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
	
	/**
	 * @return a boolean denoting whether or not the robot should lift cube
	 */
	public boolean shouldLiftCube() {
		return false;
	}
	
	/**
	 * @return the amount that the robot should drive forward
	 */
	public double forwardAmount() {
		return 0;
	}

	/**
	 * @return the amount that the robot should rotate
	 */
	public double rotationAmount() {
		return 0;
	}
	

}
