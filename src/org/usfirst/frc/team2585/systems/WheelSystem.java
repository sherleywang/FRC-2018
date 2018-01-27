package org.usfirst.frc.team2585.systems;

import org.usfirst.frc.team2585.robot.Environment;
import org.usfirst.frc.team2585.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This system controls the drivetrain of the robot
 */
public class WheelSystem extends RobotSystem {
	private SpeedController rightDrive;
	private SpeedController leftDrive;
	
	private ADXRS450_Gyro gyro;
	
	private double straightAngle;
	private double angleMultiplier = 0.015;
	
	private final double DEADZONE = 0.15;
	
	private boolean shouldBeRotating = false;
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.Initializable#init(org.usfirst.frc.team2585.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);

		leftDrive = new Spark(RobotMap.LEFT_DRIVE_MOTOR);
		rightDrive = new Spark(RobotMap.RIGHT_DRIVE_MOTOR);
								
		gyro = new ADXRS450_Gyro();
	}
	
	/**
	 * Pass the user inputs to the drive train to run the motors the appropriate amounts
	 */
	public void run() {
		double forward = input.forwardAmount();
		forward = (Math.abs(forward) > DEADZONE)? forward * 0.8 : 0;
		double rotation = input.rotationAmount();
		rotation = (Math.abs(rotation) > DEADZONE)? rotation * 0.8 : 0;
		driveWithRotation(forward, rotation);
	}
	
	/**
	 * @param forward the amount to drive forward
	 * @param rotation the amount to rotate
	 */
	public void driveWithRotation(double forward, double rotation) {
		forward *= -1; // reverse direction
		if (rotation == 0) { // Should be moving straight
			if (shouldBeRotating) { // If it wasn't moving straight before and just started moving straight
				// Record the current angle
				straightAngle = gyro.getAngle();
			}
			shouldBeRotating = false;
			// Use gyro angle to correct movement
			rotation = -(gyro.getAngle() - straightAngle) * angleMultiplier;
			if (rotation < -0.8) rotation = -0.8;
			if (rotation > 0.8) rotation = 0.8;
		} else {
			shouldBeRotating = true;
		}
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		SmartDashboard.putNumber("Gyro Rate", gyro.getRate());
		SmartDashboard.putNumber("Offset Angle", gyro.getAngle() - straightAngle);
		SmartDashboard.putNumber("Forward Amount", forward);
		SmartDashboard.putNumber("RotationAmount", rotation);
		
		arcadeDrive(forward, rotation);
	}
	
	private void setSideSpeeds(double leftSpeed, double rightSpeed) {
		leftDrive.set(leftSpeed);
		rightDrive.set(-rightSpeed);
	}
	
	private void arcadeDrive(double forward, double rotation) {
		double leftSpeed = forward + rotation;
		double rightSpeed = forward - rotation;
		setSideSpeeds(leftSpeed, rightSpeed);
		
		SmartDashboard.putNumber("LEFT SPEED FINAL", leftSpeed);
		SmartDashboard.putNumber("RIGHT SPEED FINAL", rightSpeed);
		
		SmartDashboard.putBoolean("SPEEDS ARE EQUAl", leftSpeed == rightSpeed);
		SmartDashboard.putBoolean("SPEEDS ARE OPPOSITE", leftSpeed == -rightSpeed);
	}
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#stop()
	 */
	@Override
	public void stop() {
		driveWithRotation(0, 0);
	}

	/* (non-Javadoc)
	 * @see org.impact2585.lib2585.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		if (leftDrive instanceof PWM) {
			((PWM) leftDrive).free();
		}
		if (rightDrive instanceof PWM) {
			((PWM) rightDrive).free();
		}
	}
}
