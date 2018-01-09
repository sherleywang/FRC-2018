package org.usfirst.frc.team2585.systems;

import org.usfirst.frc.team2585.robot.Environment;

/**
 * SAM interface with an init method
 */
public interface Initializable {
	/**
	 * Initialize object
	 * @param environment the environment of the systems
	 */
	public void init(Environment environ);
	
}