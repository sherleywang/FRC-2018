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
		return controller.getRawAxis(XboxConstants.RIGHT_TRIGGER) > minTriggerActuation;
	}
	
	/* (non-Javadoc)
	 * @see input.InputMethod#shouldOuttake()
	 */
	@Override
	public boolean shouldOuttake() {
		return controller.getRawAxis(XboxConstants.LEFT_TRIGGER) > minTriggerActuation;
	}

	
}
