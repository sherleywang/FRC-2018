package org.usfirst.frc.team2585.systems;

import org.impact2585.lib2585.RampedSpeedController;
import org.usfirst.frc.team2585.robot.Environment;
import org.usfirst.frc.team2585.robot.RobotMap;

/**
 * This system lifts cubes
 */
public class CubeLiftSystem extends RobotSystem {
	
	RampedSpeedController liftMotor;
	
	static double motorSpeed = 0.8;
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#init(org.usfirst.frc.team2585.robot.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);

		liftMotor = new RampedSpeedController (RobotMap.CUBE_LIFT_MOTOR);
	}

	/* (non-Javadoc)
	 * @see org.impact2585.lib2585.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		liftMotor.destroy();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (input.shouldLiftCube()) {
			setMotorSpeed(motorSpeed);
		} else {
			setMotorSpeed(0);
		}
	}
	
	/**
	 * @param speed the speed to ramp the motor to
	 */
	public void setMotorSpeed(double speed) {
		liftMotor.updateWithSpeed(motorSpeed);
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#stop()
	 */
	@Override
	public void stop() {
		liftMotor.updateWithSpeed(0);
	}
}