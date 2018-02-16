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
	
	private double targetAngle = 0.0;
	
	private final double DEADZONE = 0.2;
	
	private final double FORWARD_MULTIPLIER = 0.65;
	private final double FORWARD_MULTIPLIER_BOOST = 0.8;
	private final double ROTATION_RATE = 4.0;
	private final double DERIVATIVE_MULTIPLIER = 0.1;
	private final double CORRECTION_MULTIPLIER = 0.01;
	private final double MAX_CORRECTION = 0.6;
	
	public static boolean IS_TEST_SYSTEM = false;
	
	private boolean isForwardInverted = false;
		
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.Initializable#init(org.usfirst.frc.team2585.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);

		leftDrive = new RampedSpeedController(new Spark(RobotMap.LEFT_DRIVE_MOTOR));
		rightDrive = new RampedSpeedController(new Spark(RobotMap.RIGHT_DRIVE_MOTOR));
		
		gyro = new ADXRS450_Gyro();
		targetAngle = getGyroAngle();
	}
	
	/**
	 * Pass the user inputs to the drive train to run the motors the appropriate amounts
	 */
	public void run() {
		double forwardInput = -input.forwardAmount(); // reverse direction of driving
		if (isForwardInverted) forwardInput *= -1;
		
		if(input.shouldBoost()){
			forwardInput = (Math.abs(forwardInput) > DEADZONE)? forwardInput * FORWARD_MULTIPLIER_BOOST : 0;
		} else {
			forwardInput = (Math.abs(forwardInput) > DEADZONE)? forwardInput * FORWARD_MULTIPLIER : 0;
		}
		
		double rotationInput = input.rotationAmount();
		rotationInput = (Math.abs(rotationInput) > DEADZONE)? rotationInput : 0;
		
		driveWithGyro(forwardInput, rotationInput);
		
		if (input.shouldCalibrate()) {
			gyro.calibrate();
			resetGyro();
		}
	}
	
	/**
	 * @return the difference between the target angle and the current angle from the gyroscope
	 */
	public double getAngleError() {
		return targetAngle - getGyroAngle();
	}
	
	/**
	 * @return the amount of rotational correction that should be applied to rotate towards the target angle
	 */
	public double getCorrection() {
		double rate = getGyroRate() * DERIVATIVE_MULTIPLIER;
		double correction = (getAngleError() - rate) * CORRECTION_MULTIPLIER;
		
		// Min correction
		if (Math.abs(correction) < 0.001) correction = 0;
		// Max correction
		if (Math.abs(correction) > MAX_CORRECTION) {
			correction = Math.copySign(MAX_CORRECTION, correction);
		}
		
		if (!IS_TEST_SYSTEM) {
			SmartDashboard.putNumber("Gyro Angle",  getGyroAngle());
			SmartDashboard.putNumber("Gyro Target Angle",  targetAngle);
			SmartDashboard.putNumber("Gyro Rate",   getGyroAngle());
		}
		
		return correction;
	}
	
	/**
	 * Drive using the gyro to keep track of the angle
	 * @param forwardInput the amount to drive forward
	 * @param rotationInput the amount to adjust the targetAngle
	 */
	public void driveWithGyro(double forwardInput, double rotationInput) {		
		// Adjust Target Angle
		targetAngle += rotationInput * ROTATION_RATE;
		
		arcadeDrive(forwardInput, getCorrection());
	}
	
	/**
	 * Drive the robot without using input from the gyro
	 * @param forwardInput the amount to drive forward. -1 is backwards, 1 is forwards
	 * @param rotationInput the amount to rotate. -1 is clockwise, 1 is counter-clockwise
	 */
	public void driveWithoutGyro(double forwardInput, double rotationInput) {
		arcadeDrive(forwardInput, rotationInput);
	}
	
	/**
	 * Rotate to a certain targetAngle and get the current error
	 * @param newTargetAngle the target angle to rotate towards
	 * @return the current difference between the target angle and current angle
	 */
	public double rotateToAngle(double newTargetAngle){
		targetAngle = newTargetAngle;
		
		double correction = getCorrection();
		
		arcadeDrive(0.0, correction);
		
		return getAngleError();
	}
	
	/**
	 * @param leftSpeed the speed to set the left side to
	 * @param rightSpeed the speed to set the right side to
	 */
	private void setSideSpeeds(double leftSpeed, double rightSpeed) {
		if (Math.abs(leftSpeed) > 1) leftSpeed = Math.copySign(1, leftSpeed);
		if (Math.abs(rightSpeed) > 1) rightSpeed = Math.copySign(1, rightSpeed);
		
		// Both sides driving in same direction
		// If wiring doesn't explicitly compensate for different sides, must make rightSpeed negative
		leftDrive.updateWithSpeed(leftSpeed);
		rightDrive.updateWithSpeed(rightSpeed);
		
		if (!IS_TEST_SYSTEM) {
			SmartDashboard.putNumber("LEFT SPEED RAW", leftSpeed);
			SmartDashboard.putNumber("RIGHT SPEED RAW", rightSpeed);
			
			SmartDashboard.putBoolean("SPEEDS ARE EQUAl", leftSpeed == rightSpeed);
			SmartDashboard.putBoolean("SPEEDS ARE OPPOSITE", leftSpeed == -rightSpeed);
		}
	}
	
	/**
	 * Drive the robot with rotation. Positive rotation is counter-clockwise
	 * @param forward the amount to drive forward
	 * @param rotation the amount to rotate. Positive rotates counter-clockwise
	 */
	protected void arcadeDrive(double forward, double rotation) {
		double rightSpeed = forward + rotation;
		double leftSpeed = forward - rotation;

		setSideSpeeds(leftSpeed, rightSpeed);
	}
	
	/**
	 * @return the current angle that the robot is facing from the gyroscope
	 */
	protected double getGyroAngle() {
		return -gyro.getAngle(); // Negated because gyro is facing backwards
	}
	
	/**
	 * @return the rate that the robot is rotating from the gyroscope
	 */
	protected double getGyroRate() {
		return -gyro.getRate(); // Negated because gyro is facing backwards
	}
	
	/**
	 * Reset the angle of the gyroscope
	 */
	public void resetGyro() {
		gyro.reset();
		targetAngle = gyro.getAngle();
	}
	
	/**
	 * Set the direction of driving to be backwards
	 */
	public void faceBackwards() {
		isForwardInverted = true;
	}
	
	/**
	 * Set the direction of driving to be forwards
	 */
	public void faceForwards() {
		isForwardInverted = false;
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
