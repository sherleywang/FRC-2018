package org.usfirst.frc.team2585.robot;

/**
 * A command to be passed to the autonomous executor
 */
public interface AutonomousCommand {
	/**
	 * @param timeElapsed time passed since the last task was completed
	 * @return whether time should be reset
	 */
	public boolean execute(long timeElapsed);
}
