package org.usfirst.frc.team2585.systems;

import org.impact2585.lib2585.RampedSpeedController;
import org.usfirst.frc.team2585.robot.Environment;
import org.usfirst.frc.team2585.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This system controls the drivetrain of the robot
 */
public class WheelSystem extends RobotSystem {
	private RampedSpeedController rightDrive;
	private RampedSpeedController leftDrive;
	
	private ADXRS450_Gyro gyro;
	
	private double targetAngle;
	
	private final double DEADZONE = 0.15;
	private final double CORRECTION_MULTIPLIER = 0.015;
	
	private final double FORWARD_MULTIPLIER = 0.65;
	private final double ROTATION_RATE = 0.5;
	
	public static boolean IS_TEST_SYSTEM = false;
	
		
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.Initializable#init(org.usfirst.frc.team2585.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);

		leftDrive = new RampedSpeedController(new Spark(RobotMap.LEFT_DRIVE_MOTOR));
		rightDrive = new RampedSpeedController(new Spark(RobotMap.RIGHT_DRIVE_MOTOR));
								
		targetAngle = getGyroAngle();
		gyro = new ADXRS450_Gyro();
	}
	
	/**
	 * Pass the user inputs to the drive train to run the motors the appropriate amounts
	 */
	public void run() {
		double forward = -input.forwardAmount(); // reverse direction of driving
		forward = (Math.abs(forward) > DEADZONE)? forward * FORWARD_MULTIPLIER : 0;
		double rotation = input.rotationAmount();
		rotation = (Math.abs(rotation) > DEADZONE)? rotation : 0;
		driveWithGyro(forward, rotation);
	}
	
	/**
	 * Drive using the gyro to drive straight
	 * @param forwardInput the amount to drive forward
	 * @param rotationInput the amount to rotate
	 */
	public void driveWithGyro(double forwardInput, double rotationInput) {		
		// Adjust Target Angle
		targetAngle -= rotationInput * ROTATION_RATE;
		
		// Rotate towards target
		double correction = (getGyroAngle() - targetAngle) * CORRECTION_MULTIPLIER;
		
		arcadeDrive(forwardInput, rotationInput - correction);
		
		if (!IS_TEST_SYSTEM) {
			SmartDashboard.putNumber("Gyro Angle",  getGyroAngle());
			SmartDashboard.putNumber("Gyro Rate", getGyroRate());
			SmartDashboard.putNumber("Offset Angle", getGyroAngle() - targetAngle);
			SmartDashboard.putNumber("Forward Amount", forwardInput);
			SmartDashboard.putNumber("RotationAmount", rotationInput);
		}
	}
	
	private void setSideSpeeds(double leftSpeed, double rightSpeed) {
		if (Math.abs(leftSpeed) > 1) leftSpeed = Math.copySign(1, leftSpeed);
		if (Math.abs(rightSpeed) > 1) rightSpeed = Math.copySign(1, rightSpeed);
		
		leftDrive.updateWithSpeed(leftSpeed);
		rightDrive.updateWithSpeed(-rightSpeed);
		
		if (!IS_TEST_SYSTEM) {
			SmartDashboard.putNumber("LEFT SPEED RAW", leftSpeed);
			SmartDashboard.putNumber("RIGHT SPEED RAW", rightSpeed);
			
			SmartDashboard.putBoolean("SPEEDS ARE EQUAl", leftSpeed == rightSpeed);
			SmartDashboard.putBoolean("SPEEDS ARE OPPOSITE", leftSpeed == -rightSpeed);
		}
	}
	
	public void arcadeDrive(double forward, double rotation) {
		double leftSpeed = forward + rotation;
		double rightSpeed = forward - rotation;
		setSideSpeeds(leftSpeed, rightSpeed);
	}
	
	public double getGyroAngle() {
		return gyro.getAngle();
	}
	
	public double getGyroRate() {
		return gyro.getRate();
	}
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#stop()
	 */
	@Override
	public void stop() {
		setSideSpeeds(0.0, 0.0);
	}

	/* (non-Javadoc)
	 * @see org.impact2585.lib2585.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		leftDrive.destroy();
		rightDrive.destroy();
	}
}
