package org.usfirst.frc.team2585.input;

import org.impact2585.lib2585.XboxConstants;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Input from a single XBox controller
 */
public class XBoxInput extends InputMethod {
	private static double minTriggerActuation = 0.1;
	private Joystick controller;
	
	/**
	 * Initializes the controller
	 */
	public XBoxInput() {
		// 0 is the port # of the driver station the joystick is plugged into
		controller = new Joystick(0);
	}
	
	/* (non-Javadoc)
	 * @see input.InputMethod#shouldIntake()
	 */
	@Override
	public boolean shouldIntake() {
		return controller.getRawAxis(XboxConstants.LEFT_TRIGGER) > minTriggerActuation;
	}
	
	/* (non-Javadoc)
	 * @see input.InputMethod#shouldOuttake()
	 */
	@Override
	public boolean shouldOuttake() {
		return controller.getRawAxis(XboxConstants.RIGHT_TRIGGER) > minTriggerActuation;
	}
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.input.InputMethod#shouldLiftCube()
	 */
	@Override
	public boolean shouldLiftCube(){
		return controller.getRawButton(XboxConstants.A_BUTTON);
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.input.InputMethod#shouldClimb()
	 */
	@Override
	public boolean shouldClimb(){
		return controller.getRawButton(XboxConstants.Y_BUTTON);
	}
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.input.InputMethod#shouldRetractArm()
	 */
	@Override
	public boolean shouldRetractArm(){
		return controller.getRawButton(XboxConstants.X_BUTTON);
	}
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.input.InputMethod#forwardAmount()
	 */
	@Override
	public double forwardAmount() {
		return controller.getRawAxis(XboxConstants.LEFT_Y_AXIS);
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2585.input.InputMethod#rotationAmount()
	 */
	@Override
	public double rotationAmount() {
		// +1 is left, -1 is right
		return -controller.getRawAxis(XboxConstants.RIGHT_X_AXIS);
	}
}
