package org.usfirst.frc.team2585.systems;

import org.usfirst.frc.team2585.robot.Environment;
import org.usfirst.frc.team2585.robot.RobotMap;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

//This system intakes the power cubes and loads them on the robot
public class IntakeSystem extends RobotSystem implements Runnable {
	
		SpeedController intakeMotor;
		SpeedController outtakeMotor;
		
		//These numbers need to be adjusted after testing
		static double motorSpeed = 0.9;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.usfirst.frc.team2585.systems.RobotSystem#init(org.usfirst.frc.team2585.
		 * robot.Environment)
		 */
		@Override
		public void init(Environment environ) {
			super.init(environ);

			intakeMotor = new Victor(RobotMap.INTAKE_MOTOR);
			outtakeMotor = new Victor(RobotMap.OUTTAKE_MOTOR);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			// return if both triggers are pressed
			if (input.shouldIntake() && input.shouldOuttake()) {
				return;
			}
			// intake if right trigger is pressed
			if (input.shouldIntake()) {
				setMotorSpeed(motorSpeed);
			}
			// outtake and reverse motors if left trigger is pressed
			else if (input.shouldOuttake()) {
				setMotorSpeed(-motorSpeed);
			}
			else {
				setMotorSpeed(0);
			}
		}

		/**
		 * @param intakeSpeed is the speed to set the motor to
		 */
		public void setMotorSpeed(double intakeSpeed) {
			intakeMotor.set(intakeSpeed);
			outtakeMotor.set(-intakeSpeed);
		}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.impact2585.lib2585.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		if (intakeMotor instanceof PWM) {
			((PWM) intakeMotor).free();
		if (outtakeMotor instanceof PWM) {
			((PWM) outtakeMotor).free();
		}
		}
	}

	// Eclipse forced this piece of code
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}