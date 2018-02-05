package org.usfirst.frc.team2585.robot;

import org.impact2585.lib2585.ExecutorBasedRobot;
import org.usfirst.frc.team2585.systems.WheelSystem;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends ExecutorBasedRobot {

	private static final long serialVersionUID = 3926369073232148453L;

	SendableChooser<AutonomousCommand> chooser; 

	private Environment environ;
	private Commands commands;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser = new SendableChooser<AutonomousCommand>();
		environ = new Environment(this);
		
		commands = new Commands(environ);
		chooser.addDefault("main", commands.new Main());
		chooser.addObject("straight", commands.new Straight());
		chooser.addObject("none", commands.new None());
		SmartDashboard.putData("Auton choices", chooser);
		
		CameraServer.getInstance().startAutomaticCapture();
	}
	
	/**
	 * This is run at the beginning of the autonomous period
	 * It gets the chosen executor from the chooser and starts its execution thread
	 */
	@Override
	public void autonomousInit() {
		resetTargetAngle();
		AutonomousCommand autoSelected = chooser.getSelected();
		AutonomousExecutor executor = new AutonomousExecutor();
		executor.init(environ);
		autoSelected.updateGameData();
		executor.setTask(autoSelected);
		SmartDashboard.putNumber("Auton Time", 0);
		
		setExecutor(executor);
	}
	
	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.IterativeRobot#teleopInit()
	 */
	@Override 
	public void teleopInit() {
		resetTargetAngle();
		setExecutor(new TeleopExecutor(environ));
	}
	
	public void resetTargetAngle() {
		WheelSystem wheels = (WheelSystem) environ.getSystem(Environment.WHEEL_SYSTEM);
		wheels.resetGyro();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
	
}

