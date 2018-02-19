package org.usfirst.frc.team2585.systems;

import org.impact2585.lib2585.RampedSpeedController;
import org.usfirst.frc.team2585.robot.Environment;
import org.usfirst.frc.team2585.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This system lifts cubes
 */
public class CubeLiftSystem extends RobotSystem {
	
	DigitalInput limitSwitchTop;
	DigitalInput limitSwitchBottom;
	RampedSpeedController liftMotor;
	
	static double motorSpeed = 0.65;
	public static boolean IS_TEST_SYSTEM = false;

	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#init(org.usfirst.frc.team2585.robot.Environment)
	 */
	@Override
	public void init(Environment environ) {
		super.init(environ);

		limitSwitchTop = new DigitalInput(RobotMap.LIMIT_SWITCH_TOP);
		limitSwitchBottom = new DigitalInput(RobotMap.LIMIT_SWITCH_BOTTOM);
		
		SpeedController controller = new Spark(RobotMap.CUBE_LIFT_MOTOR);
		liftMotor = new RampedSpeedController(controller);
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
		if (input.shouldRotateUp() && input.shouldRotateDown()) {
			setMotorSpeed(0);
		} else if(input.shouldRotateUp()){
			rotateUp();
		} else if(input.shouldRotateDown()){
			rotateDown();
		} else {
			setMotorSpeed(0);
		}
		
		if (!IS_TEST_SYSTEM) {
			SmartDashboard.putBoolean("Switch-TOP", isTopSwitchPressed());
			SmartDashboard.putBoolean("Switch-BOTTOM", isBottomSwitchPressed());
		}
	}
	
	/**
	 * @param speed the speed to ramp the motor to
	 */
	public void setMotorSpeed(double speed) {
		liftMotor.updateWithSpeed(motorSpeed);
	}
	
	/**
	 * Rotate the arm up if the switch isn't pressed yet
	 */
	public void rotateUp() {
		if(isTopSwitchPressed()){
			setMotorSpeed(0);
		} else {
			setMotorSpeed(motorSpeed);
		}
	}
	
	/**
	 * Rotate the arm down if the switch isn't pressed yet
	 */
	public void rotateDown() {
		if(isBottomSwitchPressed()){
			setMotorSpeed(0);
		} else {
			setMotorSpeed(-motorSpeed);
		}
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.systems.RobotSystem#stop()
	 */
	@Override
	public void stop() {
		liftMotor.updateWithSpeed(0);
	}
	
	 /**
     * @return whether the top limit switch is pressed
     */
    public boolean isTopSwitchPressed() {
    		return !limitSwitchTop.get();
    }
    /**
     * @return whether the bottom limit switch is pressed
     */
    public boolean isBottomSwitchPressed() {
        return !limitSwitchBottom.get();
    }
}