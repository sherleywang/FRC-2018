package org.usfirst.frc.team2585.robot;

/**
 * A command to be passed to the autonomous executor
 */
public interface AutonomousCommand {
	/**
	 * @param timeElapsed time passed since auton started
	 */
	public void execute(long timeElapsed);
}
