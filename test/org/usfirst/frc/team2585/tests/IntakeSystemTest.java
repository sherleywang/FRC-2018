package org.usfirst.frc.team2585.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2585.input.InputMethod;
import org.usfirst.frc.team2585.systems.IntakeSystem;

/**
 * Unit Tests for the intake system
 */

public class IntakeSystemTest {
	private TestInput input;
	private TestIntakeSystem intakeSystem;
	
	boolean shouldIntakeInput;
	boolean shouldOuttakeInput;
	
	double motorSpeedOutput;
	
	/**
	 * Create new input and intake system for testing
	 */
	@Before
	public void setUp() {
		input = new TestInput();
		intakeSystem = new TestIntakeSystem();
		intakeSystem.setInput(input);
	}
	
	/**
	 * Tests that the speed of the motor starts as zero when the 
	 * user gives no input
	 */
	@Test
	public void defaultsToZero() {
		shouldIntakeInput = false;
		shouldOuttakeInput = false;
		intakeSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Tests that the speed of the motor is positive when right trigger is pressed 
	 */
	@Test
	public void robotIntakes() {
		shouldIntakeInput = true;
		shouldOuttakeInput = false;
		intakeSystem.run();
		Assert.assertTrue(motorSpeedOutput > 0);
	}
	
	/**
	 * Tests that the speed of the motor is negative when the left trigger is pressed
	 */
	@Test
	public void robotOuttakes() {
		shouldIntakeInput = false;
		shouldOuttakeInput = true;
		intakeSystem.run();
		Assert.assertTrue(motorSpeedOutput < 0);
	}
	
	/**
	 * Tests that the motor doesn't move when both triggers are pressed
	 */
	@Test
	public void bothPressed() {
		shouldIntakeInput = true;
		shouldOuttakeInput = true;
		intakeSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Tests that the motor speed returns to zero when right trigger is released
	 */
	@Test
	public void returnToZeroAfterIntake() {
		shouldIntakeInput = true;
		intakeSystem.run();
		shouldIntakeInput = false;
		intakeSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Tests that the motor speed returns to zero when left trigger is released
	 */
	@Test
	public void returnToZeroAfterOuttake() {
		shouldOuttakeInput = true;
		intakeSystem.run();
		shouldOuttakeInput = false;
		intakeSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Tests that the motor speed returns to zero after both triggers are released
	 */
	@Test
	public void returnToZeroAfterBoth() {
		shouldIntakeInput = true;
		shouldOuttakeInput = true;
		intakeSystem.run();
		shouldIntakeInput = false;
		shouldOuttakeInput = false;
		intakeSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Testable input for the intake system tests
	 */
	private class TestInput extends InputMethod {
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.input.InputMethod#shouldIntake()
		 * @see org.usfirst.frc.team2585.input.InputMethod#shouldOuttake()
		 */
		@Override
		public boolean shouldIntake() {
			return shouldIntakeInput;
		}
		
		@Override
		public boolean shouldOuttake() {
			return shouldOuttakeInput;
		}
	}
	
	/**
	 * A testable intake system
	 */
	private class TestIntakeSystem extends IntakeSystem {
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.systems.IntakeSystem#setMotorSpeed(double)
		 */
		@Override
		public void setMotorSpeed(double newSpeed) {
			motorSpeedOutput = newSpeed;
		}	
	}
}
