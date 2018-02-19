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
	
	double leftSpeedOutput;
	double rightSpeedOutput;
	
	double leftCurrentIn;
	double rightCurrentIn;
	
	/**
	 * Create new input and intake system for testing
	 */
	@Before
	public void setUp() {
		input = new TestInput();
		intakeSystem = new TestIntakeSystem();
		intakeSystem.setInput(input);
		
		leftCurrentIn = 0;
		rightCurrentIn = 0;
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
		Assert.assertTrue(leftSpeedOutput == 0);
		Assert.assertTrue(rightSpeedOutput == 0);
	}
	
	/**
	 * Tests that the speed of the motor is positive when right trigger is pressed 
	 */
	@Test
	public void robotIntakes() {
		shouldIntakeInput = true;
		shouldOuttakeInput = false;
		intakeSystem.run();
		Assert.assertTrue(leftSpeedOutput > 0);
		Assert.assertTrue(rightSpeedOutput > 0);
	}
	
	/**
	 * Tests that the speed of the motor is negative when the left trigger is pressed
	 */
	@Test
	public void robotOuttakes() {
		shouldIntakeInput = false;
		shouldOuttakeInput = true;
		intakeSystem.run();
		Assert.assertTrue(leftSpeedOutput < 0);
		Assert.assertTrue(rightSpeedOutput < 0);
	}
	
	/**
	 * Tests that the motor doesn't move when both triggers are pressed
	 */
	@Test
	public void bothPressed() {
		shouldIntakeInput = true;
		shouldOuttakeInput = true;
		intakeSystem.run();
		Assert.assertTrue(leftSpeedOutput == 0);
		Assert.assertTrue(rightSpeedOutput == 0);
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
		Assert.assertTrue(leftSpeedOutput == 0);
		Assert.assertTrue(rightSpeedOutput == 0);
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
		Assert.assertTrue(leftSpeedOutput == 0);
		Assert.assertTrue(rightSpeedOutput == 0);
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
		Assert.assertTrue(leftSpeedOutput == 0);
		Assert.assertTrue(rightSpeedOutput == 0);
	}
	
	/**
	 * Tests that the intake automatically rotates the cube left when the current draw is too high
	 */
	@Test
	public void automaticallyRotatesLeft() {
		shouldIntakeInput = true;
		intakeSystem.run();
		leftCurrentIn = 15;
		rightCurrentIn = 12;
		// left current higher, should rotate left side backwards
		for (int i=0; i<3; i++) {
			intakeSystem.run();
			Assert.assertTrue(leftSpeedOutput < 0);
			Assert.assertTrue(rightSpeedOutput > 0);
		}
		
		// after 50 runs, should return to normal intake
		leftCurrentIn = 0;
		rightCurrentIn = 0;
		for (int i=0; i<50; i++) {
			intakeSystem.run();
		}
		
		for (int i=0; i<3; i++) {
			intakeSystem.run();
			Assert.assertTrue(leftSpeedOutput > 0);
			Assert.assertTrue(rightSpeedOutput > 0);
		}
	}
	
	/**
	 * Tests that the intake automatically rotates the cube right when the current draw is too high
	 */
	@Test
	public void automaticallyRotatesRight() {
		shouldIntakeInput = true;
		intakeSystem.run();
		leftCurrentIn = 12;
		rightCurrentIn = 15;
		// left current higher, should rotate left side backwards
		for (int i=0; i<3; i++) {
			intakeSystem.run();
			Assert.assertTrue(leftSpeedOutput > 0);
			Assert.assertTrue(rightSpeedOutput < 0);
		}
		
		// after 50 runs, should return to normal intake
		leftCurrentIn = 0;
		rightCurrentIn = 0;
		for (int i=0; i<50; i++) {
			intakeSystem.run();
		}
		
		for (int i=0; i<3; i++) {
			intakeSystem.run();
			Assert.assertTrue(leftSpeedOutput > 0);
			Assert.assertTrue(rightSpeedOutput > 0);
		}
	}
	
	@Test
	public void stopsRotatingAfterIntake() {
		shouldIntakeInput = true;
		intakeSystem.run();
		leftCurrentIn = 12;
		rightCurrentIn = 15;
		// left current higher, should rotate left side backwards
		for (int i=0; i<3; i++) {
			intakeSystem.run();
			Assert.assertTrue(leftSpeedOutput > 0);
			Assert.assertTrue(rightSpeedOutput < 0);
		}
		
		shouldIntakeInput = false;
		intakeSystem.run();
		
		Assert.assertTrue(leftSpeedOutput == 0);
		Assert.assertTrue(rightSpeedOutput == 0);
		
		shouldIntakeInput = true;
		leftCurrentIn = 0;
		rightCurrentIn = 0;
		intakeSystem.run();
		Assert.assertTrue(leftSpeedOutput > 0);
		Assert.assertTrue(rightSpeedOutput > 0);
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
		 * @see org.usfirst.frc.team2585.systems.IntakeSystem#setLeftSpeed(double)
		 */
		@Override
		public void setLeftSpeed(double newSpeed) {
			leftSpeedOutput = newSpeed;
		}	
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.systems.IntakeSystem#setRightSpeed(double)
		 */
		@Override
		public void setRightSpeed(double newSpeed) {
			rightSpeedOutput = newSpeed;
		}	
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.systems.IntakeSystem#getLeftCurrent()
		 */
		@Override
		public double getLeftCurrent() {
			return leftCurrentIn;
		}
		
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.systems.IntakeSystem#getRightCurrent()
		 */
		@Override
		public double getRightCurrent() {
			return rightCurrentIn;
		}
	}
}
