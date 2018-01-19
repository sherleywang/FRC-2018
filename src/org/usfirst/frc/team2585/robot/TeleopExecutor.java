package org.usfirst.frc.team2585.robot;

import org.impact2585.lib2585.RunnableExecutor;
import org.usfirst.frc.team2585.systems.Initializable;
import org.usfirst.frc.team2585.systems.RobotSystem;

/**
 * Executor for the teleop period that uses input from a controller to control the robot's actions
 */
public class TeleopExecutor extends RunnableExecutor implements Initializable {
	
	private static final long serialVersionUID = 7239880156928339903L;

	/**
	 * Calls init
	 * @param environment the environment to initialize with
	 */
	public TeleopExecutor(Environment environment) {
		init(environment);
	}
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.Initializable#init(org.usfirst.frc.team2585.Environment)
	 */
	@Override
	public void init(Environment environment) {
		for (RobotSystem system : environment.getAllSystems()) {
			getRunnables().add((Runnable) system);
		}
	}

}
