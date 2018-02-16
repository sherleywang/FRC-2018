package org.usfirst.frc.team2585.robot;

import java.util.Collection;
import java.util.HashMap;

import org.impact2585.lib2585.RobotEnvironment;
import org.usfirst.frc.team2585.input.InputMethod;
import org.usfirst.frc.team2585.input.XBoxInput;
import org.usfirst.frc.team2585.systems.CameraSystem;
import org.usfirst.frc.team2585.systems.ClimbSystem;
import org.usfirst.frc.team2585.systems.CubeLiftSystem;
import org.usfirst.frc.team2585.systems.IntakeSystem;
import org.usfirst.frc.team2585.systems.RobotSystem;
import org.usfirst.frc.team2585.systems.WheelSystem;

/**
 * A class containing the robot's systems and input
 */
public class Environment extends RobotEnvironment {
	private static final long serialVersionUID = -5250612316835166409L;
	private InputMethod input;
	private HashMap<String, RobotSystem> systems;
	
	public static final String WHEEL_SYSTEM = "wheelSystem";
	public static final String INTAKE_SYSTEM = "intakeSystem";
	public static final String CUBE_LIFT_SYSTEM = "cubeLiftSystem";
	public static final String CLIMB_SYSTEM = "climbSystem";
	public static final String CAMERA_SYSTEM = "cameraSystem";

	/**
	 * Initializes the systems and input
	 * @param robot the instance of the robot that belongs to the environment
	 */
	public Environment(Robot robot) {
		super(robot);
		input = new XBoxInput();
		
		systems = new HashMap<String, RobotSystem>();
		
		// Add each of the systems to hashMap
		systems.put(Environment.WHEEL_SYSTEM, new WheelSystem());
		systems.put(Environment.INTAKE_SYSTEM, new IntakeSystem());
		systems.put(Environment.CUBE_LIFT_SYSTEM, new CubeLiftSystem());
		systems.put(Environment.CLIMB_SYSTEM, new ClimbSystem());
		systems.put(Environment.CAMERA_SYSTEM, new CameraSystem());
		
		// Initialize each system
		for (RobotSystem system : systems.values()) {
			system.init(this);
		}
	}
	
	/**
	 * @param systemName the name of the system to return 
	 * @return the environment's instance of the RobotSystem with the given name
	 */
	public RobotSystem getSystem(String systemName) {
		return systems.get(systemName);
	}
	
	/**
	 * @return a collection of all of the robot's systems
	 */
	public Collection<RobotSystem> getAllSystems() {
		return systems.values();
	}
	
	/**
	 * @return the input
	 */
	public InputMethod getInput() {
		return input;
	}
	
	/* (non-Javadoc)
	 * @see org.impact2585.lib2585.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		for (RobotSystem system : systems.values()) {
			system.destroy();
		}
	}

	@Override
	public void stopRobot() {
		for (RobotSystem system : systems.values()) {
			system.stop();
		}
		
	}

}
