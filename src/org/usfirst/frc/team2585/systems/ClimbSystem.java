package org.usfirst.frc.team2585.systems;

import org.impact2585.lib2585.RampedSpeedController;
import org.usfirst.frc.team2585.robot.Environment;
import org.usfirst.frc.team2585.robot.RobotMap;

/**
 * This system grabs onto a bar and lifts the robot up.
 */
public class ClimbSystem extends RobotSystem {
	
	RampedSpeedController climbMotor;
	
	static double motorSpeed = 0.5;
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#init(org.usfirst.frc.team2585.robot.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);

		climbMotor = new RampedSpeedController (RobotMap.CLIMB_MOTOR);
	}

	/* (non-Javadoc)
	 * @see org.impact2585.lib2585.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		climbMotor.destroy();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if(input.shouldClimb() && input.shouldRetractArm()){
			//return if both buttons are pressed
			return;
		} else if(input.shouldClimb()){
			//climb by turning motor if Y button is pressed
			setMotorSpeed(motorSpeed);
		} else if(input.shouldRetractArm()){
			//turn the arm the other way in case of a screw-up
			setMotorSpeed(-motorSpeed);
		} else {
			setMotorSpeed(0);
		}
	}
	
	/**
	 * @param speed the speed to ramp the motor to
	 */
	public void setMotorSpeed(double speed) {
		climbMotor.updateWithSpeed(speed);
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#stop()
	 */
	@Override
	public void stop() {
		climbMotor.updateWithSpeed(0);
	}
}