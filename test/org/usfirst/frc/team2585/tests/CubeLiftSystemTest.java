package org.usfirst.frc.team2585.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2585.input.InputMethod;
import org.usfirst.frc.team2585.systems.CubeLiftSystem;

/**
 * Unit Tests for the candy shooter system
 */
public class CubeLiftSystemTest {
	private TestInput input;
	private TestCubeLiftSystem cubeLiftSystem;
	
	boolean shouldLiftCube;
	
	double motorSpeedOutput;
	
	/**
	 * Create new input and candy shooter system for testing
	 */
	@Before
	public void setUp() {
		input = new TestInput();
		cubeLiftSystem = new TestCubeLiftSystem();
		cubeLiftSystem.setInput(input);
	}
	
	/**
	 * Tests that the speed of the motor is more than 0
	 * when the shoot button is pressed
	 */
	@Test
	public void motorRunsWhenLifting() {
		shouldLiftCube = true;
		cubeLiftSystem.run();
		Assert.assertTrue(motorSpeedOutput > 0);
	}
	
	/**
	 * Tests that the speed of the motor starts as zero when the 
	 * user gives no input
	 */
	@Test
	public void defaultsToZero() {
		shouldLiftCube = false;
		cubeLiftSystem.run();
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Tests that the output returns to zero after releasing the lift button
	 */
	@Test
	public void returnsToZeroAfterRunning() {
		shouldLiftCube = true;
		cubeLiftSystem.run();
		shouldLiftCube = false;
		cubeLiftSystem.run();
		
		Assert.assertTrue(motorSpeedOutput == 0);
	}
	
	/**
	 * Testable input for the high five system tests
	 */
	private class TestInput extends InputMethod {
		/* (non-Javadoc)
		 * @see org.usfirst.frc.team2585.input.InputMethod#shouldLiftCube()
		 */
		@Override
		public boolean shouldLiftCube() {
			return shouldLiftCube;
		}
		
	}
	
	/**
	 * A testable High Five System
	 */
	private class TestCubeLiftSystem extends CubeLiftSystem {

		@Override
		public void setMotorSpeed(double motorSpeed) {
			motorSpeedOutput = motorSpeed;
		}
	}
}
