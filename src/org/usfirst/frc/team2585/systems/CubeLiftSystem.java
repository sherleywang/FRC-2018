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
		SmartDashboard.putBoolean("Switch-TOP", isTopSwitchPressed());
		SmartDashboard.putBoolean("Switch-BOTTOM", isBottomSwitchPressed());
		
		
		if (input.shouldRotateUp() && input.shouldRotateDown()) {
			setMotorSpeed(0);
		} else if(input.shouldRotateUp()){
			if(isTopSwitchPressed()){
				setMotorSpeed(0);
			} else {
				setMotorSpeed(motorSpeed);
			}
		} else if(input.shouldRotateDown()){
			if(isBottomSwitchPressed()){
				setMotorSpeed(0);
			} else {
				setMotorSpeed(-motorSpeed);
			}
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