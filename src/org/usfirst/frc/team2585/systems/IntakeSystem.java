package org.usfirst.frc.team2585.systems;

import org.impact2585.lib2585.RampedSpeedController;
import org.usfirst.frc.team2585.robot.Environment;
import org.usfirst.frc.team2585.robot.RobotMap;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;


/**
 * This system intakes the power cubes and loads them on the robot
 */
public class IntakeSystem extends RobotSystem implements Runnable {

	RampedSpeedController intakeMotorRight;
	RampedSpeedController intakeMotorLeft;
	
	PowerDistributionPanel pdp;

	boolean isRealigningCubeLeft = false;
	boolean isRealigningCubeRight = false;
	double realignCounter = 0.0;
	
	// These numbers need to be adjusted after testing
	static final double motorSpeed = 0.65;
	
	static final double REVERSE_TIME_SECONDS = 1.0;
	static final double MAX_CURRENT = 10.0;


	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#init(org.usfirst.frc.team2585.robot.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);
		
		intakeMotorRight = new RampedSpeedController(new Spark(RobotMap.INTAKE_MOTOR_RIGHT));
		intakeMotorLeft = new RampedSpeedController(new Spark(RobotMap.INTAKE_MOTOR_LEFT));
		
		pdp = new PowerDistributionPanel();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// return if both triggers are pressed
		if (input.shouldIntake() && input.shouldOuttake()) {
			return;
			
		}
		if (input.shouldIntake()) {
			intakeCube();
		} else if (input.shouldOuttake()) {
			outtakeCube();
		} else {
			setLeftSpeed(0);
			setRightSpeed(0);
		}
		
		if (!input.shouldIntake()) {
			isRealigningCubeLeft = false;
			isRealigningCubeRight = false;
		}
 	}
	
	/**
	 * Intake a cube by running the motors in the intake direction
	 */
	public void intakeCube() {
		double leftCurrent = getLeftCurrent();
		double rightCurrent = getRightCurrent();
		
		if (isRealigningCubeLeft || isRealigningCubeRight) {
			if (isRealigningCubeLeft) setLeftSpeed(-motorSpeed);
			if (isRealigningCubeRight) setRightSpeed(-motorSpeed);
			
			if ((realignCounter/50.0) > REVERSE_TIME_SECONDS) { // Divided by 50 b/c executed 50 times/second
				isRealigningCubeLeft = false;
				isRealigningCubeRight = false;
				realignCounter = 0;
			}
	
			realignCounter += 1;
		} else if (leftCurrent > MAX_CURRENT && rightCurrent > MAX_CURRENT) {
			// if both are stalling, flip the one that is pulling more current
			if (leftCurrent > rightCurrent) {
				isRealigningCubeLeft = true;
				setLeftSpeed(-motorSpeed);
			} else {
				isRealigningCubeRight = true;
				setRightSpeed(-motorSpeed);
			}
			realignCounter = 0;
		} else {
			// Normal intake
			setLeftSpeed(motorSpeed);
			setRightSpeed(motorSpeed);
		}		
	}
	
	/**
	 * Deposit a cube by running the motors in the outtake direction
	 */
	public void outtakeCube() {
		setLeftSpeed(-motorSpeed);
		setRightSpeed(-motorSpeed);
	}
	
	/**
	 * @param leftSpeed the speed to set the left side to
	 */
	public void setLeftSpeed(double leftSpeed) {
		intakeMotorLeft.updateWithSpeed(leftSpeed);
	}
	
	/**
	 * @param rightSpeed the speed to set the right side to
	 */
	public void setRightSpeed(double rightSpeed) {
		intakeMotorRight.updateWithSpeed(-rightSpeed);
	}
	
	public double getLeftCurrent() {
		return pdp.getCurrent(RobotMap.LEFT_INTAKE_PDP_SLOT);

	}
	
	public double getRightCurrent() {
		return pdp.getCurrent(RobotMap.RIGHT_INTAKE_PDP_SLOT);
	}

	/* (non-Javadoc)
	 * @see org.impact2585.lib2585.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		intakeMotorRight.destroy();
		intakeMotorLeft.destroy();
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#stop()
	 */
	@Override
	public void stop() {
		intakeMotorRight.updateWithSpeed(0);
		intakeMotorLeft.updateWithSpeed(0);
	}

}