package org.usfirst.frc.team2585.robot;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * A command to be passed to the autonomous executor
 */
public abstract class AutonomousCommand {
	int location = 0;
	String gameData = "";
	
	/**
	 * @param timeElapsed time passed since the last task was completed
	 * @return whether time should be reset
	 */
	abstract boolean execute(long timeElapsed);
	
	public void updateGameData() {
		// Location of the driverStation: 1, 2, 3; L, M, R
		location = DriverStation.getInstance().getLocation(); 
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}
}
