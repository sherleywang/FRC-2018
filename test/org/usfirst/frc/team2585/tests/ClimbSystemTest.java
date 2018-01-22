package org.usfirst.frc.team2585.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2585.input.InputMethod;
import org.usfirst.frc.team2585.systems.ClimbSystem;

/**
 * Unit Tests for the climb system
 */
public class ClimbSystemTest {
	private TestInput input;
	private TestClimbSystem climbSystem;
	
	boolean shouldClimb;
	boolean shouldRetractArm;
	
	double motorSpeedOutput;
	
	/**
	 * Create new input and climb system test
	 */
	@Before
	public void setUp() {
		input = new TestInput();
		climbSystem = new TestClimbSystem();
		climbSystem.setInput(input);
	}
	
	/**
	 * Tests that the speed of the motor is more than 0 when climbing
	 */
	@Test
	public void motorRunsWhenClimbing() {
		shouldClimb = true;
		climbSystem.run();
		Assert.assertTrue(motorSpeedOutput > 0);
	}
	
	/**
	 * Tests that the speed of the motor is running the other way when retracting
	 */
	@Test
	public void motorRunsWhenRetracting() {
		shouldRetractArm = true;
		climbSystem.run();
		Assert.assertTrue(motorSpeedOutput < 0);
	}
	
	/**
	 * Tests that the speed of the motor is stationary when no buttons are pressed
	 */
	@Test
	public void motorStationary() {
		shouldRetractArm = false;
		shouldClimb = false;
		climbSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Tests that the motor is stationary when both buttons are pressed
	 */
	@Test
	public void motorStationaryWhenBothTrue() {
		shouldRetractArm = true;
		shouldClimb = true;
		climbSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Tests that the motor returns to stationary after retracting
	 */
	@Test
	public void motorStationaryAfterRetracting() {
		shouldRetractArm = true;
		climbSystem.run();
		shouldRetractArm = false;
		climbSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Tests that the motor returns to stationary after climbing
	 */
	@Test
	public void motorStationaryAfterClimbing() {
		shouldClimb = true;
		climbSystem.run();
		shouldClimb = false;
		climbSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Testable input for the climb system tests
	 */
	private class TestInput extends InputMethod {
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.input.InputMethod#shouldClimb()
		 */
		@Override
		public boolean shouldClimb() {
			return shouldClimb;
		}
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.input.InputMethod#shouldRetractArm()
		 */
		@Override
		public boolean shouldRetractArm() {
			return shouldRetractArm;
		}
	}
	
	/**
	 * A testable climb system
	 */
	private class TestClimbSystem extends ClimbSystem {

		@Override
		public void setMotorSpeed(double speed) {
			motorSpeedOutput = speed;
		}
	}
}
