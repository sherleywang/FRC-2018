package org.usfirst.frc.team2585.systems;

import org.usfirst.frc.team2585.robot.Environment;
import org.usfirst.frc.team2585.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This system controls the drivetrain of the robot
 */
public class WheelSystem extends RobotSystem {
	private SpeedController rightDrive;
	private SpeedController leftDrive;
	private DifferentialDrive drivetrain;
	
	private ADXRS450_Gyro gyro;
	
	private double straightAngle;
	private double angleMultiplier = 0.03;
	
	private boolean isMovingForward = false;
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.Initializable#init(org.usfirst.frc.team2585.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);

		leftDrive = new Spark(RobotMap.LEFT_DRIVE_MOTOR);
		rightDrive = new Spark(RobotMap.RIGHT_DRIVE_MOTOR);
		
		drivetrain = new DifferentialDrive(leftDrive, rightDrive);
						
		gyro = new ADXRS450_Gyro();
	}
	
	/**
	 * Pass the user inputs to the drive train to run the motors the appropriate amounts
	 */
	public void run() {
		driveWithRotation(-input.forwardAmount(), input.rotationAmount());
	}
	
	/**
	 * @param forward the amount to drive forward
	 * @param rotation the amount to rotate
	 */
	public void driveWithRotation(double forward, double rotation) {
		if (rotation == 0) { // Moving straight
			// Use gyro angle to correct movement
			if (!isMovingForward) {
				// Record the current angle if turning just finished
				straightAngle = gyro.getAngle();
			}
			isMovingForward = true;
			
			rotation = -(gyro.getAngle() - straightAngle) * angleMultiplier;
		} else {
			isMovingForward = false;
		}
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		SmartDashboard.putNumber("Forward Amount", forward);
		SmartDashboard.putNumber("RotationAmount", rotation);
		
		drivetrain.arcadeDrive(forward, rotation, true);
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
