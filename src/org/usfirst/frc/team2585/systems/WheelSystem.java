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
	
	private double leftPercentage = 0.65;
	private double rightPercentage = 0.65;
	
	private final double angleChangeWeight = 1.0;
	private double previousAngle;
	private double currentAngle;
	
	private boolean isMovingForward = false;
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.Initializable#init(org.usfirst.frc.team2585.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);

		leftDrive = new Spark(RobotMap.LEFT_DRIVE_MOTOR);
		rightDrive = new Spark(RobotMap.RIGHT_DRIVE_MOTOR);
						
		gyro = new ADXRS450_Gyro();
		currentAngle = gyro.getAngle();
	}
	
	/**
	 * Pass the user inputs to the drive train to run the motors the appropriate amounts
	 */
	public void run() {
		calibrate();
		driveWithRotation(input.forwardAmount(), input.rotationAmount());
	}
	
	/**
	 * @param forward the amount to drive forward
	 * @param rotation the amount to rotate
	 */
	public void driveWithRotation(double forward, double rotation) {
		if (forward > 0 && rotation == 0) {
			isMovingForward = true;
		} else {
			isMovingForward = false;
		}
		
		leftDrive.set(-leftPercentage * (forward - rotation));
		rightDrive.set(rightPercentage * (forward + rotation));
	}
	
	/**
	 * Calibrate the right and left sides based on the gyroscope data
	 */
	private void calibrate() {
		previousAngle = currentAngle;
		currentAngle = -gyro.getAngle();
		SmartDashboard.putNumber("Gyro Angle", currentAngle);
		
		if (isMovingForward) {
			double angleChange = currentAngle - previousAngle;
			// Positive: veering left
			// Negative: veering right
			double weightedAngleChange = angleChange * angleChangeWeight;
			if ((leftPercentage + weightedAngleChange < 1) && (rightPercentage - weightedAngleChange > 0)) {
				leftPercentage = leftPercentage + weightedAngleChange;
				rightPercentage = rightPercentage - weightedAngleChange;
						
			}
			SmartDashboard.putNumber("RawAngleChange", angleChange);
		}
		SmartDashboard.putNumber("Left Percentage", leftPercentage);
		SmartDashboard.putNumber("Right Percentage", rightPercentage);
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
