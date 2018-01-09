package org.usfirst.frc.team2585.systems;


import org.impact2585.lib2585.Destroyable;
import org.usfirst.frc.team2585.input.InputMethod;
import org.usfirst.frc.team2585.robot.Environment;

/**
 * Parent class of the systems that will run on the robot
 */
public abstract class RobotSystem implements Destroyable, Initializable, Runnable {
	protected InputMethod input;
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.Initializable#init(org.usfirst.frc.team2585.Environment)
	 */
	public void init(Environment environ) {
		input = environ.getInput();
	}
	
	/**
	 * @param newInput the input to set
	 */
	public synchronized void setInput(InputMethod newInput) {
		input = newInput;
	}
	
	/**
	 * Set all speed controllers to zero
	 */
	public abstract void stop();

}
